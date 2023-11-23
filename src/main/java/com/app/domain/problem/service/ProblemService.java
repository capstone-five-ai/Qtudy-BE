package com.app.domain.problem.service;

import com.app.domain.problem.dto.Problem.Request.CommentaryRequestDto;
import com.app.domain.problem.dto.Problem.Request.FileNameRequestDto;
import com.app.domain.problem.dto.Problem.Request.UpdateProblemChoicesRequestDto;
import com.app.domain.problem.entity.AiGeneratedProblems;
import com.app.domain.problem.entity.ProblemFiles;
import com.app.domain.problem.repository.AiGeneratedProblemsRepository;
import com.app.domain.problem.repository.ProblemFilesRepository;
import com.app.global.config.S3.S3Service;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class ProblemService { //Service 추후 분할 예정

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ProblemFilesRepository problemFilesRepository;
    @Autowired
    private AiGeneratedProblemsRepository aiGeneratedProblemsRepository;
    @Autowired
    private S3Service s3Service;



    public List<AiGeneratedProblems> GetFileProblems(String token, FileNameRequestDto fileNameRequestDto) {
        String fileName = fileNameRequestDto.getFileName();
        ProblemFiles problemFiles;
        List<AiGeneratedProblems> problems;

        problemFiles = problemFilesRepository.findByMemberIdAndFileName(token,fileName);
        problems = aiGeneratedProblemsRepository.findByProblemFiles_FileId(problemFiles.getFileId());

        return problems;
    }

    public void UpdateProblemChoices(String token, UpdateProblemChoicesRequestDto updateProblemChoicesRequestDto){
        int aiGeneratedProblemId = updateProblemChoicesRequestDto.getAiGeneratedProblemId();
        List<String> problemChoices = updateProblemChoicesRequestDto.getProblemChoices();

        Optional<AiGeneratedProblems> optionalAiGeneratedProblems = aiGeneratedProblemsRepository.findById(aiGeneratedProblemId);

        if(optionalAiGeneratedProblems.isPresent()){
            AiGeneratedProblems aiGeneratedProblems = optionalAiGeneratedProblems.get();
            aiGeneratedProblems.setProblemChoices(problemChoices);
            aiGeneratedProblemsRepository.save(aiGeneratedProblems);
        }
    }

    public void UpdateProblemCommentary(String token, CommentaryRequestDto commentaryRequestDto){
        int aiGeneratedProblemId = commentaryRequestDto.getAiGeneratedProblemId();
        String commentary = commentaryRequestDto.getCommentary();

        Optional<AiGeneratedProblems> optionalAiGeneratedProblems = aiGeneratedProblemsRepository.findById(aiGeneratedProblemId);

        // 엔티티가 존재하는 경우에만 업데이트 수행
        if (optionalAiGeneratedProblems.isPresent()) {
            AiGeneratedProblems aiGeneratedProblems = optionalAiGeneratedProblems.get();
            aiGeneratedProblems.setProblemCommentary(commentary);
            aiGeneratedProblemsRepository.save(aiGeneratedProblems);
        } else {
            // 해당 ID의 엔티티가 존재하지 않는 경우에 대한 처리 (생략 가능)
            // 예를 들어, 로깅 등을 수행할 수 있습니다.
            throw new BusinessException(ErrorCode.NOT_SENT_HTTP);
        }
    }
}