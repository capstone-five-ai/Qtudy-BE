package com.app.domain.problem.service;


import com.app.domain.problem.dto.ProblemFile.AiRequest.TypeConvertProblemDto;
import com.app.domain.problem.dto.ProblemFile.Request.AiGenerateProblemByFileDto;
import com.app.domain.problem.dto.ProblemFile.Request.AiGenerateProblemByTextDto;
import com.app.domain.problem.dto.ProblemFile.AiRequest.AiGenerateProblemToAiDto;
import com.app.domain.problem.entity.AiGeneratedProblem;
import com.app.domain.problem.entity.ProblemFile;
import com.app.domain.problem.repository.AiGeneratedProblemRepository;
import com.app.domain.problem.repository.ProblemFileRepository;
import com.app.domain.problem.value.S3FileInformation;
import com.app.global.config.ENUM.*;
import com.app.global.config.S3.S3Service;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.BusinessException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class ProblemFileService { //Service 추후 분할 예정

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ProblemFileRepository problemFileRepository;
    @Autowired
    private AiGeneratedProblemRepository aiGeneratedProblemRepository;
    @Autowired
    private S3Service s3Service;


    public void AiGenerateProblemFileByText(String token, AiGenerateProblemByTextDto aiGenerateProblemByTextDto) {
        String text = aiGenerateProblemByTextDto.getText();
        ProblemType type = aiGenerateProblemByTextDto.getType();
        Amount amount = aiGenerateProblemByTextDto.getAmount();
        ProblemDifficulty difficulty = aiGenerateProblemByTextDto.getDifficulty();
        String fileName = aiGenerateProblemByTextDto.getFileName();

        String url;
        AiGenerateProblemToAiDto[] aiGenerateProblemToAiDto;
        String jsonBody;   // AI 문제 DTO를 JSON 문자열로 변환

        if(problemFileRepository.findByFileName(fileName).isPresent()){ // 이미 파일이름이 존재하는 경우 에러
            throw new BusinessException(ErrorCode.ALREADY_EXISTS_NAME);
        }

        switch (aiGenerateProblemByTextDto.getType()){  //Problem 타입 체크
            case MULTIPLE:
                url = "http://localhost:5000/create/problem/mcq";
                break;
            case SUBJECTIVE:
                url = "http://localhost:5000/create/problem/saq";
                break;
            default:
                throw new BusinessException(ErrorCode.NOT_GENERATE_PROBLEM);
        }

        try {
            HttpHeaders headers = new HttpHeaders();  // HTTP 요청 헤더 설정
            headers.setContentType(MediaType.APPLICATION_JSON);

            ObjectMapper objectMapper = new ObjectMapper();  // JSON 데이터로 변환하기 위한 ObjectMapper 생성
            jsonBody = objectMapper.writeValueAsString(aiGenerateProblemByTextDto); //HTTP BODY 생성

            HttpEntity<String> request = new HttpEntity<>(jsonBody, headers); // // HTTP 요청 전송
            aiGenerateProblemToAiDto = restTemplate.postForObject(url, request, AiGenerateProblemToAiDto[].class); // http 응답 받아옴
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.NOT_SENT_HTTP);
        }

        UploadS3AndSaveFile(aiGenerateProblemToAiDto, token, aiGenerateProblemByTextDto.toTextDto2());
    }


    public void AiGenerateProblemFileByFile(String token,List<MultipartFile> File, AiGenerateProblemByFileDto aiGenerateProblemByFileDto, FileType fileType) {
        String url;
        AiGenerateProblemToAiDto[] aiGenerateProblemToAiDto;

        switch (aiGenerateProblemByFileDto.getType()){  //Problem 타입 체크
            case MULTIPLE:
                url = "http://localhost:5000/create/problem/mcq";
                break;
            case SUBJECTIVE:
                url = "http://localhost:5000/create/problem/saq";
                break;
            default:
                throw new BusinessException(ErrorCode.NOT_GENERATE_PROBLEM);
        }

        switch (fileType){  //File 타입 체크
            case PDF: // PDF 타입일경우
                try {
                    String pdfText = convertPdfToString(File.get(0));
                    String jsonBody;   // AI 문제 DTO를 JSON 문자열로 변환

                    HttpHeaders headers = new HttpHeaders();  // HTTP 요청 헤더 설정
                    headers.setContentType(MediaType.APPLICATION_JSON);

                    ObjectMapper objectMapper = new ObjectMapper();  // JSON 데이터로 변환하기 위한 ObjectMapper 생성
                    jsonBody = objectMapper.writeValueAsString(aiGenerateProblemByFileDto.toTextDto(pdfText)); //HTTP BODY 생성 (FILE -> TEXTDto 변환)

                    HttpEntity<String> request = new HttpEntity<>(jsonBody, headers); // // HTTP 요청 전송
                    aiGenerateProblemToAiDto = restTemplate.postForObject(url, request, AiGenerateProblemToAiDto[].class); // http 응답 받아옴

                    UploadS3AndSaveFile(aiGenerateProblemToAiDto, token, aiGenerateProblemByFileDto.toTextDto2());
                } catch (JsonProcessingException e) {
                    throw new BusinessException(ErrorCode.NOT_SENT_HTTP);
                } catch (IOException e) {
                    throw new RuntimeException(e); // 추후 변경예정 (convertPdf)
                }
                break;
            case JPG: // JPG 타입일경우
                url+="/jpg";
                HttpHeaders headers = new HttpHeaders();  // HTTP 요청 헤더 설정
                headers.setContentType(MediaType.MULTIPART_FORM_DATA);

                MultiValueMap<String, Object> jsonBody = new LinkedMultiValueMap<>();

                jsonBody.add("type",aiGenerateProblemByFileDto.getType());
                jsonBody.add("amount",aiGenerateProblemByFileDto.getAmount());
                jsonBody.add("difiiculty",aiGenerateProblemByFileDto.getDifficulty());

                for(MultipartFile file : File)
                    jsonBody.add("file",file);

                HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(jsonBody, headers); // // HTTP 요청 전송
                aiGenerateProblemToAiDto = restTemplate.postForObject(url, request, AiGenerateProblemToAiDto[].class); // http 응답 받아옴

                UploadS3AndSaveFile(aiGenerateProblemToAiDto, token, aiGenerateProblemByFileDto.toTextDto2());
                break;
            default:
                throw new BusinessException(ErrorCode.NOT_GENERATE_PROBLEM);
        }


    }






    public static String convertPdfToString(MultipartFile pdfFile) throws IOException { // PDF파일을 String으로 변환
        try (InputStream is = pdfFile.getInputStream()) {
            PDDocument document = PDDocument.load(is);
            PDFTextStripper textStripper = new PDFTextStripper();
            String text = textStripper.getText(document);
            document.close();
            return text;
        }
    }


    public void UploadS3AndSaveFile(AiGenerateProblemToAiDto[] aiGenerateProblemToAiDto, String token, TypeConvertProblemDto typeConvertProblemDto) {
        File tempFile = null;

        try { // 문제 PDF 생성
            String fileName = token+"_"+ typeConvertProblemDto.getFileName()+"_problem.pdf";// 예시로 앞글자를 사용한 .txt 파일 이름 생성 (추후 변경 예정)
            tempFile = CreateTempFile(fileName, aiGenerateProblemToAiDto,PdfType.PROBLEM); // 파일 생성

            // 파일을 MultipartFile로 변환
            FileSystemResource resource = new FileSystemResource(tempFile);
            MultipartFile multipartFile = new MockMultipartFile(fileName, resource.getFilename(), "application/pdf", resource.getInputStream());

            //생성된 파일을 S3에 업로드
            S3FileInformation fileInfo = s3Service.uploadFile(multipartFile);


        } catch (IOException e) {
            throw new BusinessException(ErrorCode.NOT_UPLOAD_PROBLEM);
        } finally {
            if (tempFile != null) {
                tempFile.delete();  // 방금 생성한 파일 삭제
            }
        }




        try { // 정답 PDF 생성
            String fileName = token+"_"+ typeConvertProblemDto.getFileName()+"_answer.pdf";// 예시로 앞글자를 사용한 .txt 파일 이름 생성 (추후 변경 예정)
            tempFile = CreateTempFile(fileName, aiGenerateProblemToAiDto,PdfType.ANSWER); // 파일 생성

            // 파일을 MultipartFile로 변환
            FileSystemResource resource = new FileSystemResource(tempFile);
            MultipartFile multipartFile = new MockMultipartFile(fileName, resource.getFilename(), "application/pdf", resource.getInputStream());

            //생성된 파일을 S3에 업로드
            S3FileInformation fileInfo = s3Service.uploadFile(multipartFile);

        } catch (IOException e) {
            throw new BusinessException(ErrorCode.NOT_UPLOAD_PROBLEM);
        } finally {
            if (tempFile != null) {
                tempFile.delete();  // 방금 생성한 파일 삭제
            }
        }

        ProblemFile problemFile = SaveProblemFile(token, typeConvertProblemDto); //PROBLEM_FILE 테이블 저장

        SaveProblems(problemFile, aiGenerateProblemToAiDto); // AI_GENERATED_PROBLEMS 테이블 및 객관식 보기 저장
    }

    private File CreateTempFile(String fileName, AiGenerateProblemToAiDto[] aiGenerateProblemToAiDtoArray, PdfType pdfType)  throws IOException { // String 기반으로 File 생성

        if(pdfType == PdfType.PROBLEM) {

            File tempFile = File.createTempFile(fileName, ".pdf");
            String content = ConvertToStringByProblem(aiGenerateProblemToAiDtoArray); //파일 내용 변환


            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage();
                document.addPage(page);
                try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.COURIER, 12);
                    contentStream.newLineAtOffset(10, 700);

                    String[] lines = content.split("\n");
                    for (String line : lines) {
                        contentStream.showText(line);
                        contentStream.newLineAtOffset(0, -12); // 12는 폰트 크기에 따라 조절
                    }
                    contentStream.endText();
                }
                document.save(tempFile);
            }
            return tempFile;
        }
        else if(pdfType == PdfType.ANSWER){


            File tempFile = File.createTempFile(fileName, ".pdf");
            String content = ConvertToStringByAnswer(aiGenerateProblemToAiDtoArray); //파일 내용 변환


            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage();
                document.addPage(page);
                try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.COURIER, 12);
                    contentStream.newLineAtOffset(10, 700);

                    String[] lines = content.split("\n");
                    for (String line : lines) {
                        contentStream.showText(line);
                        contentStream.newLineAtOffset(0, -12); // 12는 폰트 크기에 따라 조절
                    }
                    contentStream.endText();
                }
                document.save(tempFile);
            }
            return tempFile;

        }
        return null;
    }


    public static String ConvertToStringByProblem(AiGenerateProblemToAiDto[] aiGenerateProblemToAiDtoArray) { // 파일의 내용 변환 함수
        if (aiGenerateProblemToAiDtoArray == null || aiGenerateProblemToAiDtoArray.length == 0) {
            return ""; // 빈 문자열 반환 또는 예외 처리 등을 수행할 수 있습니다.
        }

        StringBuffer stringBuffer = new StringBuffer();

        int problemNumber =1;

        for (AiGenerateProblemToAiDto aiDto : aiGenerateProblemToAiDtoArray) {
            stringBuffer.append(problemNumber+++". ").append(aiDto.getProblemName()).append("\n");   //문제 이름

            List<String> choices = aiDto.getProblemChoices(); // 문제 보기
            if (choices != null && !choices.isEmpty()) {
                for (int i = 0; i < choices.size(); i++) {
                    stringBuffer.append(" ").append(i + 1).append(" ").append(choices.get(i)).append("\n");
                }
            }

            stringBuffer.append(String.valueOf(aiDto.getProblemCommentary())).append("\n"); // 문제 정답
            stringBuffer.append("\n\n\n");
        }

        return stringBuffer.toString();
    }


    public static String ConvertToStringByAnswer(AiGenerateProblemToAiDto[] aiGenerateProblemToAiDtoArray) { // 파일의 내용 변환 함수
        if (aiGenerateProblemToAiDtoArray == null || aiGenerateProblemToAiDtoArray.length == 0) {
            return ""; // 빈 문자열 반환 또는 예외 처리 등을 수행할 수 있습니다.
        }

        StringBuffer stringBuffer = new StringBuffer();

        int problemNumber =1;

        for (AiGenerateProblemToAiDto aiDto : aiGenerateProblemToAiDtoArray) {

            if(aiDto.getProblemAnswer() != null) {
                stringBuffer.append(problemNumber + ". " + aiDto.getProblemAnswer()); // 문제 정답 (객관식인 경우에만)
            }
            stringBuffer.append(" "+String.valueOf(aiDto.getProblemCommentary())); // 해설은 객관식,주관식 둘다 필수로 있음
            stringBuffer.append("\n\n");
        }

        return stringBuffer.toString();
    }


    public ProblemFile SaveProblemFile(String token , TypeConvertProblemDto typeConvertProblemDto){
        ProblemFile problemFile = ProblemFile.builder()
                .memberId(token)   //추후에 member 토큰으로 변경해야함.(추후 변경 예정)
                .fileName(typeConvertProblemDto.getFileName()) //추후에 member가 지정한 이름으로 변경해야함.
                .fileKey(typeConvertProblemDto.getFileName())
                .dtype(DType.PROBLEM)
                .problemDifficulty(typeConvertProblemDto.getDifficulty())
                .problemAmount(typeConvertProblemDto.getAmount())
                .problemType(typeConvertProblemDto.getType())
                .build();

        problemFileRepository.save(problemFile);
        return problemFile;
    }

    public void SaveProblems (ProblemFile problemFile, AiGenerateProblemToAiDto[] aiGenerateProblemToAiDtoArray){
        for(AiGenerateProblemToAiDto aiDto : aiGenerateProblemToAiDtoArray){
            AiGeneratedProblem aiGeneratedProblem = AiGeneratedProblem.builder() // 문제생성
                    .problemFile(problemFile)
                    .problemName(aiDto.getProblemName())
                    .problemChoices(aiDto.getProblemChoices())
                    .problemCommentary(aiDto.getProblemCommentary())
                    .problemType(problemFile.getProblemType())
                    .build();

            aiGeneratedProblemRepository.save(aiGeneratedProblem);

        }
    }


}