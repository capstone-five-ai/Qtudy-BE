package com.app.domain.file.problem.service;

import com.app.domain.file.common.ENUM.DType;
import com.app.domain.file.common.service.S3Service;
import com.app.domain.file.problem.dto.ProblemFile.Request.AiGenerateProblemDto;
import com.app.domain.file.problem.dto.ProblemFile.Response.AiGenerateProblemResponseDto;
import com.app.domain.file.problem.dto.ProblemFile.Request.DeleteProblemFileDto;
import com.app.domain.file.problem.entity.AiGeneratedProblems;
import com.app.domain.file.problem.entity.AiProblemChoice;
import com.app.domain.file.problem.entity.ProblemFiles;
import com.app.domain.file.problem.repository.AiGeneratedProblemsRepository;
import com.app.domain.file.problem.repository.AiProblemChoiceRepository;
import com.app.domain.file.problem.repository.ProblemFilesRepository;
import com.app.domain.file.problem.value.S3FileInformation;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.BusinessException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProblemFileService { //Service 추후 분할 예정

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ProblemFilesRepository problemFilesRepository;
    @Autowired
    private AiGeneratedProblemsRepository aiGeneratedProblemsRepository;
    @Autowired
    private AiProblemChoiceRepository aiProblemChoiceRepository;
    @Autowired
    private S3Service s3Service;


    public void AiGenerateProblemFile(String token, AiGenerateProblemDto aiproblemDtoGenerate) {
        String url ;
        AiGenerateProblemResponseDto[] aiGenerateProblemResponseDto;

        switch (aiproblemDtoGenerate.getType()){  //Problem 타입 체크
            case MCQ:
                url = "http://localhost:5000/create/problem/mcq";
                break;
            case SAQ:
                url = "http://localhost:5000/create/problem/saq";
                break;
            default:
                throw new BusinessException(ErrorCode.NOT_GENERATE_PROBLEM);
        }



        String jsonBody;   // AI 문제 DTO를 JSON 문자열로 변환
        try {
            HttpHeaders headers = new HttpHeaders();  // HTTP 요청 헤더 설정
            headers.setContentType(MediaType.APPLICATION_JSON);

            ObjectMapper objectMapper = new ObjectMapper();  // JSON 데이터로 변환하기 위한 ObjectMapper 생성
            jsonBody = objectMapper.writeValueAsString(aiproblemDtoGenerate); //HTTP BODY 생성

            HttpEntity<String> request = new HttpEntity<>(jsonBody, headers); // // HTTP 요청 전송
            aiGenerateProblemResponseDto = restTemplate.postForObject(url, request, AiGenerateProblemResponseDto[].class); // http 응답 받아옴
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.NOT_SENT_HTTP);
        }
        //String jsonBody = String.format("{\"text\": \"%s\"}", aiproblemDto.getText());



        File tempFile = null;

        AiGenerateProblemResponseDto[] aiGenerateProblemResponseDtoArray = aiGenerateProblemResponseDto;//.//get();
        try {

            String fileName = aiGenerateProblemResponseDtoArray[0].getProblemName().substring(0,5);// 예시로 앞글자를 사용한 .txt 파일 이름 생성 (추후 변경 예정)
            tempFile = CreateTempFile(fileName, aiGenerateProblemResponseDtoArray); // 파일 생성

            // 파일을 MultipartFile로 변환
            FileSystemResource resource = new FileSystemResource(tempFile);
            MultipartFile multipartFile = new MockMultipartFile(fileName, resource.getFilename(), "application/pdf", resource.getInputStream());

            //생성된 파일을 S3에 업로드
            S3FileInformation fileInfo = s3Service.uploadFile(multipartFile);

            ProblemFiles problemFile = SaveProblemFile(token, fileInfo, aiproblemDtoGenerate); //PROBLEM_FILE 테이블 저장

            SaveProblems(problemFile, aiGenerateProblemResponseDtoArray); // AI_GENERATED_PROBLEMS 테이블 및 객관식 보기 저장

        } catch (IOException e) {
            throw new BusinessException(ErrorCode.NOT_UPLOAD_PROBLEM);
        } finally {
            if (tempFile != null) {
                tempFile.delete();  // 방금 생성한 파일 삭제
            }
        }
    }

    private File CreateTempFile(String fileName, AiGenerateProblemResponseDto[] aiGenerateProblemResponseDtoArray)  throws IOException{ // String 기반으로 File 생성
        File tempFile = File.createTempFile(fileName, ".pdf");
        String content = ConvertToString(aiGenerateProblemResponseDtoArray); //파일 내용 변환

        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(content);
        }
        return tempFile;
    }


    public static String ConvertToString(AiGenerateProblemResponseDto[] aiGenerateProblemResponseDtoArray) { // 파일의 내용 변환 함수
        if (aiGenerateProblemResponseDtoArray == null || aiGenerateProblemResponseDtoArray.length == 0) {
            return ""; // 빈 문자열 반환 또는 예외 처리 등을 수행할 수 있습니다.
        }

        StringBuffer stringBuffer = new StringBuffer();

        int problemNumber =1;

        for (AiGenerateProblemResponseDto aiDto : aiGenerateProblemResponseDtoArray) {
            stringBuffer.append(problemNumber++).append(aiDto.getProblemName()).append("\n");   //문제 이름

            List<String> choices = aiDto.getProblemChoices(); // 문제 보기
            if (choices != null && !choices.isEmpty()) {
                for (int i = 0; i < choices.size(); i++) {
                    stringBuffer.append(i + 1).append(": ").append(choices.get(i)).append("\n");
                }
            }

            stringBuffer.append(aiDto.getProblemCommentary()).append("\n"); // 문제 정답
            stringBuffer.append("\n\n");
        }

        return stringBuffer.toString();
    }


    public ProblemFiles SaveProblemFile(String token, S3FileInformation fileInfo , AiGenerateProblemDto aiproblemDtoGenerate){
        ProblemFiles problemFiles = ProblemFiles.builder()
                .memberId(token)   //추후에 member 토큰으로 변경해야함.(추후 변경 예정)
                .fileName(fileInfo.getFileKey()) //추후에 member가 지정한 이름으로 변경해야함.
                .fileKey(fileInfo.getFileKey())
                .dtype(DType.PROBLEM)
                .problemDifficulty(aiproblemDtoGenerate.getDifficulty())
                .problemAmount(aiproblemDtoGenerate.getAmount())
                .problemType(aiproblemDtoGenerate.getType())
                .build();

        problemFilesRepository.save(problemFiles);
        return problemFiles;
    }

    public void SaveProblems (ProblemFiles problemFiles, AiGenerateProblemResponseDto[] aiGenerateProblemResponseDtoArray){
        for(AiGenerateProblemResponseDto aiDto : aiGenerateProblemResponseDtoArray){
            AiGeneratedProblems aiGeneratedProblems = AiGeneratedProblems.builder() // 문제생성
                    .problemFiles(problemFiles)
                    .problemName(aiDto.getProblemName())
                    .problemCommentary(aiDto.getProblemCommentary())
                    .build();

            aiGeneratedProblemsRepository.save(aiGeneratedProblems);

            if(aiDto.getProblemChoices() != null) { // 객관식 보기가 존재 할때만 실행.
                for (int i = 0; i < aiDto.getProblemChoices().size(); i++) {  //객관식 보기 생성
                    AiProblemChoice aiProblemChoice = AiProblemChoice.builder()
                            .aiGeneratedProblems (aiGeneratedProblems)
                            .aiProblemChoiceContent(aiDto.getProblemChoices().get(i))
                            .build();

                    aiProblemChoiceRepository.save(aiProblemChoice);
                }
            }
        }
    }







    public void DeleteProblemFile(String token, DeleteProblemFileDto deleteProblemFileDto) {
        int fileId = deleteProblemFileDto.getFileId();

        Optional<ProblemFiles> optionalProblemFile = problemFilesRepository.findById(fileId);

        if (optionalProblemFile.isPresent()) {
            ProblemFiles problemFile = optionalProblemFile.get();
            problemFilesRepository.delete(problemFile);
        } else {
            // 추후 에러 처리할 예정...
        }
    }

}