package com.app.domain.file.problem.service;

import com.app.domain.file.common.service.S3Service;
import com.app.domain.file.problem.dto.ProblemChoice.Request.AddChoiceRequestDto;
import com.app.domain.file.problem.dto.ProblemChoice.Request.DeleteChoiceRequestDto;
import com.app.domain.file.problem.dto.ProblemChoice.Request.ProblemIdRequestDto;
import com.app.domain.file.problem.dto.ProblemChoice.Request.UpdateChoiceRequestDto;
import com.app.domain.file.problem.entity.AiGeneratedProblems;
import com.app.domain.file.problem.entity.AiProblemChoice;
import com.app.domain.file.problem.repository.AiGeneratedProblemsRepository;
import com.app.domain.file.problem.repository.AiProblemChoiceRepository;
import com.app.domain.file.problem.repository.ProblemFilesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class ProblemChoiceService { //Service 추후 분할 예정

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

    public List<AiProblemChoice> GetProblemChoices(String token, ProblemIdRequestDto problemIdRequestDto){
        int aiGeneratedProblemId = problemIdRequestDto.getAiGeneratedProblemId();
        List<AiProblemChoice> choices;

        choices = aiProblemChoiceRepository.findByAiGeneratedProblems_AiGeneratedProblemId(aiGeneratedProblemId);
        return choices;
    }

    public void AddProblemChoice(String token, AddChoiceRequestDto addChoiceRequestDto){
        int aiGeneratedProblemId = addChoiceRequestDto.getAiGeneratedProblemId();
        String content = addChoiceRequestDto.getContent();

        Optional<AiGeneratedProblems> aiGeneratedProblemsOptional = aiGeneratedProblemsRepository.findById(aiGeneratedProblemId);

        // AI Generated Problem이 존재하는지 확인 (추후 변경예정)
        AiGeneratedProblems aiGeneratedProblems = aiGeneratedProblemsOptional.orElseThrow(() ->
                new RuntimeException("AI Generated Problem not found for the given ID: " + aiGeneratedProblemId));

        AiProblemChoice aiProblemChoice = AiProblemChoice.builder()
                .aiGeneratedProblems(aiGeneratedProblems) // 변경: aiGeneratedProblems로 설정
                .aiProblemChoiceContent(content)
                .build();

        aiProblemChoiceRepository.save(aiProblemChoice);

    }

    public void DeleteProblemChoice(String token, DeleteChoiceRequestDto deleteChoiceRequestDto){
        int aiProblemChoiceId = deleteChoiceRequestDto.getAiProblemChoiceId();

        Optional<AiProblemChoice> aiProblemChoiceOptional = aiProblemChoiceRepository.findById(aiProblemChoiceId);

        // Ai Problem Choice가 존재하는지 확인 (추후 변경예정)
        AiProblemChoice aiProblemChoice = aiProblemChoiceOptional.orElseThrow(() ->
                new RuntimeException("AI Generated Problem not found for the given ID: " + aiProblemChoiceId));

        aiProblemChoiceRepository.delete(aiProblemChoice);

    }

    public String UpdateProblemChoice(String token, UpdateChoiceRequestDto updateChoiceRequestDto){
        int aiProblemChoiceId = updateChoiceRequestDto.getAiProblemChoiceId();
        String content = updateChoiceRequestDto.getNewContent();

        Optional<AiProblemChoice> aiProblemChoiceOptional = aiProblemChoiceRepository.findById(aiProblemChoiceId);

        // Ai Problem Choice가 존재하는지 확인 (추후 변경예정)
        AiProblemChoice aiProblemChoice = aiProblemChoiceOptional.orElseThrow(() ->
                new RuntimeException("AI Generated Problem not found for the given ID: " + aiProblemChoiceId));

        aiProblemChoice.setAiProblemChoiceContent(content); // Choice 내용 수정
        aiProblemChoiceRepository.save(aiProblemChoice); //

        return "Sucess";
    }



}