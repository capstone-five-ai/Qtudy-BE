package com.app.domain.file.problem.service;

import com.app.domain.file.common.ENUM.DType;
import com.app.domain.file.common.service.S3Service;
import com.app.domain.file.problem.dto.AiGenerateDto;
import com.app.domain.file.problem.entity.ProblemFiles;
import com.app.domain.file.problem.repository.ProblemFilesRepository;
import com.app.domain.file.problem.value.S3FileInformation;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Service
public class AiGenerateService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ProblemFilesRepository problemFilesRepository;
    @Autowired
    private S3Service s3Service;


    public ResponseEntity<String> AiGenerateProblem(AiGenerateDto aiGenerateDto) {
        String url ;
        String content;



        switch (aiGenerateDto.getType()){  //Problem 타입 체크
            case MCQ:
                url = "http://localhost:7000/mcq";
                break;
            case SAQ:
                url = "http://localhost:7000/saq";
                break;
            default:
                throw new BusinessException(ErrorCode.NOT_GENERATE_PROBLEM);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String jsonBody = String.format("{\"text\": \"%s\"}", aiGenerateDto.getText()); // 추후 인증 토큰 추가해야함.
        HttpEntity<String> request = new HttpEntity<>(jsonBody, headers); // http 전송함


        try {
            content = restTemplate.postForObject(url, request, String.class); // http 응답 받아옴
        } catch (RestClientException e) { // 응답 제대로 안왔을 시, 에러처리
            throw new BusinessException(ErrorCode.NOT_GENERATE_PROBLEM);
        }



        File tempFile = null;

        try {
            tempFile = createTempFile(content);
            // 파일을 MultipartFile로 변환
            FileSystemResource resource = new FileSystemResource(tempFile);
            MultipartFile multipartFile = new MockMultipartFile(resource.getFilename(), resource.getInputStream());

            //생성된 파일을 S3에 업로드
            S3FileInformation fileInfo = s3Service.uploadFile(multipartFile); // ###############여기 부터 다시 시작!###################



            ProblemFiles problemFiles = ProblemFiles.builder()
                    .memberId(1)   //추후에 member 토큰으로 변경해야함.
                    .fileName(fileInfo.getFileKey()) //추후에 member가 지정한 이름으로 변경해야함.
                    .fileUrl(fileInfo.getFileUrl())
                    .dtype(DType.PROBLEM)
                    .problemDifficulty(aiGenerateDto.getDifficulty())
                    .problemAmount(aiGenerateDto.getAmount())
                    .problemType(aiGenerateDto.getType())
                    .build();

            problemFilesRepository.save(problemFiles);

        } catch(IOException e){
            throw new BusinessException(ErrorCode.NOT_UPLOAD_PROBLEM);
        }finally {
            if (tempFile != null) {
                tempFile.delete();  // 방금 생성한 파일 삭제
            }
        }

        return ResponseEntity.ok("Success");
    }

    private File createTempFile(String content)  throws IOException{ // String 기반으로 File 생성
        File tempFile = File.createTempFile("prefix", ".txt");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(content);
        }
        return tempFile;
    }
}