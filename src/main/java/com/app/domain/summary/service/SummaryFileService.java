package com.app.domain.summary.service;


import com.app.domain.problem.value.S3FileInformation;
import com.app.domain.summary.dto.SummaryFile.Request.AiGenerateSummaryByFileDto;
import com.app.domain.summary.dto.SummaryFile.AiRequest.TypeConvertSummaryDto;
import com.app.domain.summary.dto.SummaryFile.Request.AiGenerateSummaryByTextDto;
import com.app.domain.summary.dto.SummaryFile.Response.AiGenerateSummaryResponseDto;
import com.app.domain.summary.entity.AiGeneratedSummarys;
import com.app.domain.summary.entity.SummaryFiles;
import com.app.domain.summary.repository.AiGeneratedSummarysRepository;
import com.app.domain.summary.repository.SummaryFilesRepository;
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
public class SummaryFileService { //Service 추후 분할 예정

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private SummaryFilesRepository summaryFilesRepository;
    @Autowired
    private AiGeneratedSummarysRepository aiGeneratedSummarysRepository;
    @Autowired
    private S3Service s3Service;


    public void AiGenerateSummaryFileByText(String token, AiGenerateSummaryByTextDto aiGenerateSummaryByTextDto) {
        String text = aiGenerateSummaryByTextDto.getText();
        Amount amount = aiGenerateSummaryByTextDto.getAmount();
        String fileName = aiGenerateSummaryByTextDto.getFileName();

        String url = "http://localhost:5000/create/summary/mcq";;
        AiGenerateSummaryResponseDto aiGenerateSummaryResponseDto;

        if(summaryFilesRepository.findByFileName(fileName).isPresent()){ // 이미 파일이름이 존재하는 경우 에러
            throw new BusinessException(ErrorCode.ALREADY_EXISTS_NAME);
        }


        String jsonBody;   // AI 문제 DTO를 JSON 문자열로 변환
        try {
            HttpHeaders headers = new HttpHeaders();  // HTTP 요청 헤더 설정
            headers.setContentType(MediaType.APPLICATION_JSON);

            ObjectMapper objectMapper = new ObjectMapper();  // JSON 데이터로 변환하기 위한 ObjectMapper 생성
            jsonBody = objectMapper.writeValueAsString(aiGenerateSummaryByTextDto); //HTTP BODY 생성

            HttpEntity<String> request = new HttpEntity<>(jsonBody, headers); // // HTTP 요청 전송
            aiGenerateSummaryResponseDto = restTemplate.postForObject(url, request, AiGenerateSummaryResponseDto.class); // http 응답 받아옴
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.NOT_SENT_HTTP);
        }

        UploadS3AndSaveFile(aiGenerateSummaryResponseDto, token, aiGenerateSummaryByTextDto.toTextDto2());


    }


    public void AiGenerateSummaryFileByFile(String token, List<MultipartFile> File, AiGenerateSummaryByFileDto aiGenerateSummaryByFileDto, FileType fileType) {
        String url = "http://localhost:5000/create/summary/mcq";
        AiGenerateSummaryResponseDto aiGenerateSummaryResponseDto;


        switch (fileType){  //File 타입 체크
            case PDF: // PDF 타입일경우
                try {
                    String pdfText = convertPdfToString(File.get(0));
                    String jsonBody;   // AI 문제 DTO를 JSON 문자열로 변환

                    HttpHeaders headers = new HttpHeaders();  // HTTP 요청 헤더 설정
                    headers.setContentType(MediaType.APPLICATION_JSON);

                    ObjectMapper objectMapper = new ObjectMapper();  // JSON 데이터로 변환하기 위한 ObjectMapper 생성
                    jsonBody = objectMapper.writeValueAsString(aiGenerateSummaryByFileDto.toTextDto(pdfText)); //HTTP BODY 생성 (FILE -> TEXTDto 변환)

                    HttpEntity<String> request = new HttpEntity<>(jsonBody, headers); // // HTTP 요청 전송
                    aiGenerateSummaryResponseDto = restTemplate.postForObject(url, request, AiGenerateSummaryResponseDto.class); // http 응답 받아옴

                    UploadS3AndSaveFile(aiGenerateSummaryResponseDto, token, aiGenerateSummaryByFileDto.toTextDto2());
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

                jsonBody.add("amount",aiGenerateSummaryByFileDto.getAmount());

                for(MultipartFile file : File)
                    jsonBody.add("file",file);

                HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(jsonBody, headers); // // HTTP 요청 전송
                aiGenerateSummaryResponseDto = restTemplate.postForObject(url, request, AiGenerateSummaryResponseDto.class); // http 응답 받아옴

                UploadS3AndSaveFile(aiGenerateSummaryResponseDto, token, aiGenerateSummaryByFileDto.toTextDto2());
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


    public void UploadS3AndSaveFile(AiGenerateSummaryResponseDto aiGenerateSummaryResponseDto, String token, TypeConvertSummaryDto typeConvertSummaryDto) {
        File tempFile = null;

        try { // 문제 PDF 생성
            String fileName = token+"_"+ typeConvertSummaryDto.getFileName()+"_summary.pdf";// 예시로 앞글자를 사용한 .txt 파일 이름 생성 (추후 변경 예정)
            tempFile = CreateTempFile(fileName, aiGenerateSummaryResponseDto,PdfType.SUMMARY); // 파일 생성

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
            String fileName = token+"_"+ typeConvertSummaryDto.getFileName()+"_answer.pdf";// 예시로 앞글자를 사용한 .txt 파일 이름 생성 (추후 변경 예정)
            tempFile = CreateTempFile(fileName, aiGenerateSummaryResponseDto,PdfType.ANSWER); // 파일 생성

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

        SummaryFiles summaryFile = SaveSummaryFile(token, typeConvertSummaryDto); //PROBLEM_FILE 테이블 저장

        SaveSummarys(summaryFile, aiGenerateSummaryResponseDto); // AI_GENERATED_PROBLEMS 테이블 및 객관식 보기 저장
    }

    private File CreateTempFile(String fileName, AiGenerateSummaryResponseDto aiGenerateSummaryResponseDtoArray,PdfType pdfType)  throws IOException { // String 기반으로 File 생성

        File tempFile = File.createTempFile(fileName, ".pdf");
        String content = ConvertToStringBySummary(aiGenerateSummaryResponseDtoArray); //파일 내용 변환


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


    public static String ConvertToStringBySummary(AiGenerateSummaryResponseDto aiGenerateSummaryResponseDtoArray) { // 파일의 내용 변환 함수
        if (aiGenerateSummaryResponseDtoArray == null ) {
            return ""; // 빈 문자열 반환 또는 예외 처리 등을 수행할 수 있습니다.
        }

        StringBuffer stringBuffer = new StringBuffer();

        int summaryNumber =1;

        stringBuffer.append(summaryNumber+++". ").append(aiGenerateSummaryResponseDtoArray.getSummaryName()).append("\n\n");   //요점정리 이름
        stringBuffer.append(" ").append(aiGenerateSummaryResponseDtoArray.getSummaryCommentary()).append("\n\n");   //문제 이름



        return stringBuffer.toString();
    }


    public static String ConvertToStringByAnswer(AiGenerateSummaryResponseDto[] aiGenerateSummaryResponseDtoArray) { // 파일의 내용 변환 함수
        if (aiGenerateSummaryResponseDtoArray == null || aiGenerateSummaryResponseDtoArray.length == 0) {
            return ""; // 빈 문자열 반환 또는 예외 처리 등을 수행할 수 있습니다.
        }

        StringBuffer stringBuffer = new StringBuffer();

        int summaryNumber =1;

        for (AiGenerateSummaryResponseDto aiDto : aiGenerateSummaryResponseDtoArray) {

            stringBuffer.append(summaryNumber+++". "+aiDto.getSummaryCommentary()); // 문제 정답
            stringBuffer.append("\n\n");
        }

        return stringBuffer.toString();
    }


    public SummaryFiles SaveSummaryFile(String token , TypeConvertSummaryDto typeConvertSummaryDto){
        SummaryFiles summaryFiles = SummaryFiles.builder()
                .memberId(token)   //추후에 member 토큰으로 변경해야함.(추후 변경 예정)
                .fileName(typeConvertSummaryDto.getFileName()) //추후에 member가 지정한 이름으로 변경해야함.
                .fileKey(typeConvertSummaryDto.getFileName())
                .dtype(DType.SUMMARY)
                .summaryAmount(typeConvertSummaryDto.getAmount())
                .build();

        summaryFilesRepository.save(summaryFiles);
        return summaryFiles;
    }

    public void SaveSummarys (SummaryFiles summaryFiles, AiGenerateSummaryResponseDto aiGenerateSummaryResponseDto){
        AiGeneratedSummarys aiGeneratedSummarys = AiGeneratedSummarys.builder() // 문제생성
                .summaryFiles(summaryFiles)
                .summaryTitle(aiGenerateSummaryResponseDto.getSummaryName())
                .summaryContent(aiGenerateSummaryResponseDto.getSummaryCommentary())
                .build();

        aiGeneratedSummarysRepository.save(aiGeneratedSummarys);

        }
}