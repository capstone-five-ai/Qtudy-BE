package com.app.domain.summary.service;


import com.app.domain.file.dto.Response.FileListResponseDto;
import com.app.domain.problem.dto.ProblemFile.AiRequest.AiGenerateProblemFromAiDto;
import com.app.domain.problem.value.S3FileInformation;
import com.app.domain.summary.dto.SummaryFile.Request.AiGenerateSummaryDto;
import com.app.domain.summary.dto.SummaryFile.AiRequest.AiGenerateSummaryFromAiDto;
import com.app.domain.summary.entity.AiGeneratedSummary;
import com.app.domain.summary.entity.SummaryFile;
import com.app.domain.summary.repository.AiGeneratedSummaryRepository;
import com.app.domain.summary.repository.SummaryFileRepository;
import com.app.global.config.ENUM.*;
import com.app.global.config.S3.S3Service;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.BusinessException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.text.PDFTextStripper;
import org.jboss.jandex.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
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
import java.util.stream.Collectors;

@Service
public class SummaryFileService { //Service 추후 분할 예정

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private SummaryFileRepository summaryFileRepository;
    @Autowired
    private AiGeneratedSummaryRepository aiGeneratedSummaryRepository;
    @Autowired
    private S3Service s3Service;


    public AiGeneratedSummary AiGenerateSummaryFileByText(String token, AiGenerateSummaryDto aiGenerateSummaryDto) {
        String text = aiGenerateSummaryDto.getText();
        Amount amount = aiGenerateSummaryDto.getAmount();
        String fileName = aiGenerateSummaryDto.getFileName();

        String url = "http://localhost:5000/create/summary";;
        AiGenerateSummaryFromAiDto aiGenerateSummaryFromAiDto;
        AiGeneratedSummary aiGeneratedSummary = null;

        if(summaryFileRepository.findByFileName(fileName).isPresent()){ // 이미 파일이름이 존재하는 경우 에러
            throw new BusinessException(ErrorCode.ALREADY_EXISTS_NAME);
        }


        String jsonBody;   // AI 문제 DTO를 JSON 문자열로 변환
        try {
            HttpHeaders headers = new HttpHeaders();  // HTTP 요청 헤더 설정
            headers.setContentType(MediaType.APPLICATION_JSON);

            ObjectMapper objectMapper = new ObjectMapper();  // JSON 데이터로 변환하기 위한 ObjectMapper 생성
            jsonBody = objectMapper.writeValueAsString(aiGenerateSummaryDto); //HTTP BODY 생성

            HttpEntity<String> request = new HttpEntity<>(jsonBody, headers); // // HTTP 요청 전송
            aiGenerateSummaryFromAiDto = restTemplate.postForObject(url, request, AiGenerateSummaryFromAiDto.class); // http 응답 받아옴
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.NOT_SENT_HTTP);
        }

        UploadS3AndSaveFile(aiGenerateSummaryFromAiDto, token, aiGenerateSummaryDto);

        SummaryFile summaryFile = SaveSummaryFile(token, aiGenerateSummaryDto); //PROBLEM_FILE 테이블 저장

        aiGeneratedSummary = SaveSummarys(summaryFile, aiGenerateSummaryFromAiDto); // AI_GENERATED_PROBLEMS 테이블 및 객관식 보기 저장

        return aiGeneratedSummary;
    }


    public AiGeneratedSummary AiGenerateSummaryFileByFile(String token, List<MultipartFile> File, AiGenerateSummaryDto aiGenerateSummaryDto, FileType fileType) {
        String url = "http://localhost:5000/create/summary";
        AiGenerateSummaryFromAiDto aiGenerateSummaryFromAiDto;
        AiGeneratedSummary aiGeneratedSummary = null;


        switch (fileType){  //File 타입 체크
            case PDF: // PDF 타입일경우
                try {
                    String pdfText = convertPdfToString(File.get(0));
                    String jsonBody;   // AI 문제 DTO를 JSON 문자열로 변환

                    HttpHeaders headers = new HttpHeaders();  // HTTP 요청 헤더 설정
                    headers.setContentType(MediaType.APPLICATION_JSON);

                    ObjectMapper objectMapper = new ObjectMapper();  // JSON 데이터로 변환하기 위한 ObjectMapper 생성
                    jsonBody = objectMapper.writeValueAsString(aiGenerateSummaryDto.toTextDto(pdfText)); //HTTP BODY 생성 (FILE -> TEXTDto 변환)

                    HttpEntity<String> request = new HttpEntity<>(jsonBody, headers); // // HTTP 요청 전송
                    aiGenerateSummaryFromAiDto = restTemplate.postForObject(url, request, AiGenerateSummaryFromAiDto.class); // http 응답 받아옴

                    UploadS3AndSaveFile(aiGenerateSummaryFromAiDto, token, aiGenerateSummaryDto);
                } catch (JsonProcessingException e) {
                    throw new BusinessException(ErrorCode.NOT_SENT_HTTP);
                } catch (IOException e) {
                    throw new RuntimeException(e); // 추후 변경예정 (convertPdf)
                }
                break;

            case JPG: // JPG 타입일경우
                url+="/jpg";

                MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
                for (MultipartFile file : File) {
                    try {
                        map.add("files", new ByteArrayResource(file.getBytes())); // 파일리스트 추가
                    } catch (IOException e) {
                        throw new RuntimeException(e); // 추후 에러처리
                    }
                }

                // 다른 필드도 MultiValueMap에 추가
                map.add("amount", aiGenerateSummaryDto.getAmount().toString());

                HttpHeaders headers = new HttpHeaders();  // HTTP 요청 헤더 설정
                headers.setContentType(MediaType.MULTIPART_FORM_DATA);

                HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers); // // HTTP 요청 전송
                aiGenerateSummaryFromAiDto = restTemplate.postForObject(url, request, AiGenerateSummaryFromAiDto.class); // http 응답 받아옴

                UploadS3AndSaveFile(aiGenerateSummaryFromAiDto, token, aiGenerateSummaryDto);
                break;
            default:
                throw new BusinessException(ErrorCode.NOT_GENERATE_PROBLEM);
        }

        SummaryFile summaryFile = SaveSummaryFile(token, aiGenerateSummaryDto); //PROBLEM_FILE 테이블 저장

        aiGeneratedSummary = SaveSummarys(summaryFile, aiGenerateSummaryFromAiDto); // AI_GENERATED_PROBLEMS 테이블 및 객관식 보기 저장

        return aiGeneratedSummary;
    }


    public void UploadS3AndSaveFile(AiGenerateSummaryFromAiDto aiGenerateSummaryFromAiDto, String token, AiGenerateSummaryDto aiGenerateSummaryDto) {
        File tempFile = null;

        try { // 문제 PDF 생성
            String fileName = token+"_"+ aiGenerateSummaryDto.getFileName()+"_summary.pdf";// 예시로 앞글자를 사용한 .txt 파일 이름 생성 (추후 변경 예정)
            tempFile = CreateTempFile(fileName, aiGenerateSummaryFromAiDto,PdfType.SUMMARY); // 파일 생성

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
            String fileName = token+"_"+ aiGenerateSummaryDto.getFileName()+"_answer.pdf";// 예시로 앞글자를 사용한 .txt 파일 이름 생성 (추후 변경 예정)
            tempFile = CreateTempFile(fileName, aiGenerateSummaryFromAiDto,PdfType.ANSWER); // 파일 생성

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
    }


    private File CreateTempFile(String fileName, AiGenerateSummaryFromAiDto aiGenerateSummaryFromAiDto, PdfType pdfType)  throws IOException { // String 기반으로 File 생성

        File tempFile = File.createTempFile(fileName, ".pdf");
        String content = ConvertToStringBySummary(aiGenerateSummaryFromAiDto); //파일 내용 변환


        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.beginText();

                PDType0Font font = PDType0Font.load(document, Main.class.getResourceAsStream("/fonts/malgun.ttf"));
                contentStream.setFont(font, 12);
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


    public static String convertPdfToString(MultipartFile pdfFile) throws IOException { // PDF파일을 String으로 변환
        try (InputStream is = pdfFile.getInputStream()) {
            PDDocument document = PDDocument.load(is);
            PDFTextStripper textStripper = new PDFTextStripper();
            String text = textStripper.getText(document);
            document.close();
            return text;
        }
    }



    public static String ConvertToStringBySummary(AiGenerateSummaryFromAiDto aiGenerateSummaryFromAiDtoArray) { // 파일의 내용 변환 함수
        if (aiGenerateSummaryFromAiDtoArray == null ) {
            return ""; // 빈 문자열 반환 또는 예외 처리 등을 수행할 수 있습니다.
        }

        StringBuffer stringBuffer = new StringBuffer();

        int summaryNumber =1;

        stringBuffer.append(summaryNumber+++". ").append(aiGenerateSummaryFromAiDtoArray.getSummaryTitle()).append("\n\n");   //요점정리 이름
        stringBuffer.append(" ").append(aiGenerateSummaryFromAiDtoArray.getSummaryContent()).append("\n\n");   //문제 이름



        return stringBuffer.toString();
    }




    public SummaryFile SaveSummaryFile(String token , AiGenerateSummaryDto aiGenerateSummaryDto){
        SummaryFile summaryFile = SummaryFile.builder()
                .memberId(token)   //추후에 member 토큰으로 변경해야함.(추후 변경 예정)
                .fileName(aiGenerateSummaryDto.getFileName()) //추후에 member가 지정한 이름으로 변경해야함.
                .fileKey(aiGenerateSummaryDto.getFileName())
                .dtype(DType.SUMMARY)
                .summaryAmount(aiGenerateSummaryDto.getAmount())
                .build();

        summaryFileRepository.save(summaryFile);
        return summaryFile;
    }

    public AiGeneratedSummary SaveSummarys (SummaryFile summaryFile, AiGenerateSummaryFromAiDto aiGenerateSummaryFromAiDto){
        AiGeneratedSummary aiGeneratedSummary = AiGeneratedSummary.builder() // 문제생성
                .summaryFile(summaryFile)
                .summaryTitle(aiGenerateSummaryFromAiDto.getSummaryTitle())
                .summaryContent(aiGenerateSummaryFromAiDto.getSummaryContent())
                .build();

        aiGeneratedSummaryRepository.save(aiGeneratedSummary);

        return aiGeneratedSummary;
        }



    public List<FileListResponseDto> allAiSummaryFileList(String token){ //사용자가 생성한 모든 요점정리파일 리스트 가져오기

        List<SummaryFile> fileList = summaryFileRepository.findByMemberId(token); // 요점정리파일 이름 가져오기

        List<FileListResponseDto> fileListResponseDtoList = fileList.stream()
                .map(file -> new FileListResponseDto(
                        file.getFileId(),
                        file.getFileName(),
                        file.getDtype(),
                        file.getCreateTime()
                ))
                .collect(Collectors.toList());
        return fileListResponseDtoList;
    }
}