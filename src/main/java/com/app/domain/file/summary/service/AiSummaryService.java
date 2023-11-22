package com.app.domain.file.summary.service;

import com.app.domain.file.common.ENUM.DType;
import com.app.domain.file.common.service.S3Service;
import com.app.domain.file.problem.value.S3FileInformation;
import com.app.domain.file.summary.dto.AiSummaryDto;
import com.app.domain.file.summary.dto.AiSummaryResponseDto;
import com.app.domain.file.summary.entity.AiGeneratedSummarys;
import com.app.domain.file.summary.entity.SummaryFiles;
import com.app.domain.file.summary.repository.AiGeneratedSummarysRepository;
import com.app.domain.file.summary.repository.SummaryFilesRepository;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.BusinessException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Service
public class AiSummaryService {


    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private SummaryFilesRepository summaryFilesRepository;
    @Autowired
    private AiGeneratedSummarysRepository aiGeneratedSummarysRepository;
    @Autowired
    private S3Service s3Service;

    public ResponseEntity<String> generateSummary(String token, AiSummaryDto aiSummaryDto) {

        String url = "http://localhost:5000/create/summary" ;
        AiSummaryResponseDto aiSummaryResponseDto;


        String jsonBody;   // AI 문제 DTO를 JSON 문자열로 변환
        try {
            HttpHeaders headers = new HttpHeaders();  // HTTP 요청 헤더 설정
            headers.setContentType(MediaType.APPLICATION_JSON);

            ObjectMapper objectMapper = new ObjectMapper();  // JSON 데이터로 변환하기 위한 ObjectMapper 생성
            jsonBody = objectMapper.writeValueAsString(aiSummaryDto); //HTTP BODY 생성

            HttpEntity<String> request = new HttpEntity<>(jsonBody, headers); // // HTTP 요청 전송
            aiSummaryResponseDto = restTemplate.postForObject(url, request, AiSummaryResponseDto.class); // http 응답 받아옴
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.NOT_SENT_HTTP);
        }


        File tempFile = null;


        try {

            String fileName = aiSummaryResponseDto.getSummaryTitle().substring(0,5);// 예시로 앞글자를 사용한 .txt 파일 이름 생성 (추후 변경 예정)
            tempFile = CreateTempFile(fileName, aiSummaryResponseDto); // 파일 생성

            // 파일을 MultipartFile로 변환
            FileSystemResource resource = new FileSystemResource(tempFile);
            MultipartFile multipartFile = new MockMultipartFile(fileName, resource.getFilename(), "application/pdf", resource.getInputStream());

            //생성된 파일을 S3에 업로드
            S3FileInformation fileInfo = s3Service.uploadFile(multipartFile);

            SummaryFiles summaryFiles = SaveSummaryFile(token, fileInfo, aiSummaryDto); //PROBLEM_FILE 테이블 저장

            SaveSummarys(summaryFiles, aiSummaryResponseDto); // AI_GENERATED_PROBLEMS 테이블 및 객관식 보기 저장

        } catch (IOException e) {
            throw new BusinessException(ErrorCode.NOT_UPLOAD_PROBLEM);
        } finally {
            if (tempFile != null) {
                tempFile.delete();  // 방금 생성한 파일 삭제
            }
        }

        return ResponseEntity.ok("Success");

    }

    private File CreateTempFile(String fileName, AiSummaryResponseDto aiSummaryResponseDto)  throws IOException{ // String 기반으로 File 생성
        File tempFile = File.createTempFile(fileName, ".pdf");
        String content = ConvertToString(aiSummaryResponseDto); //파일 내용 변환

        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(content);
        }
        return tempFile;
    }


    public static String ConvertToString(AiSummaryResponseDto aiSummaryResponseDto) { // 파일의 내용 변환 함수
        if (aiSummaryResponseDto == null) {
            return ""; // 빈 문자열 반환 또는 예외 처리 등을 수행할 수 있습니다.
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(aiSummaryResponseDto.getSummaryTitle()).append("\n"); //요약정리 이름
        stringBuffer.append(aiSummaryResponseDto.getSummaryContent());

        return stringBuffer.toString();
    }


    public SummaryFiles SaveSummaryFile(String token, S3FileInformation fileInfo , AiSummaryDto aisummaryDto){
        SummaryFiles summaryFiles = SummaryFiles.builder()
                .memberId(token)   //추후에 member 토큰으로 변경해야함.(추후 변경 예정)
                .fileName(fileInfo.getFileKey()) //추후에 member가 지정한 이름으로 변경해야함.
                .fileKey(fileInfo.getFileKey())
                .dtype(DType.SUMMARY)
                .summaryAmount(aisummaryDto.getAmount())
                .build();

        summaryFilesRepository.save(summaryFiles);
        return summaryFiles;
    }


    public void SaveSummarys (SummaryFiles summaryFiles, AiSummaryResponseDto aiSummaryResponseDto){
        AiGeneratedSummarys aiGeneratedSummarys = AiGeneratedSummarys.builder()
                .summaryFiles(summaryFiles)
                .summaryTitle(aiSummaryResponseDto.getSummaryTitle())
                .summaryContent(aiSummaryResponseDto.getSummaryContent())
                .build();

        aiGeneratedSummarysRepository.save(aiGeneratedSummarys);
    }
}
