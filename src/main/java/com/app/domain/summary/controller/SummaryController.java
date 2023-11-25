package com.app.domain.summary.controller;

import com.app.domain.summary.dto.Summary.Request.GetSummaryRequestDto;
import com.app.domain.summary.dto.Summary.Request.UpdateSummaryRequestDto;
import com.app.domain.summary.dto.Summary.Response.GetSummaryResponseDto;
import com.app.domain.summary.dto.Summary.Response.UpdateSummaryResponseDto;
import com.app.domain.summary.entity.AiGeneratedSummary;
import com.app.domain.summary.service.SummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/summary")
public class SummaryController {

    @Autowired
    SummaryService summaryService;


    @PostMapping("/getSummary") // 요약정리 제목,해설 업데이트
    public ResponseEntity<GetSummaryResponseDto> GetSummary(@RequestHeader("Authorization") String token, @Valid @RequestBody GetSummaryRequestDto getSummaryRequestDto) {
        AiGeneratedSummary aiGeneratedSummary = summaryService.GetSummary(token, getSummaryRequestDto);

        GetSummaryResponseDto getSummaryResponseDto = GetSummaryResponseDto.builder()
                .aiGeneratedSummaryId(aiGeneratedSummary.getAiGeneratedSummaryId())
                .summaryTitle(aiGeneratedSummary.getSummaryTitle())
                .summaryContent(aiGeneratedSummary.getSummaryContent())
                .createTime(aiGeneratedSummary.getSummaryFile().getCreateTime())
                .updateTime(aiGeneratedSummary.getSummaryFile().getUpdateTime())
                .build();

        return ResponseEntity.ok(getSummaryResponseDto);
    }


    @PostMapping("/updateSummary") // 요약정리 제목,해설 업데이트
    public ResponseEntity<UpdateSummaryResponseDto> UpdateSummary(@RequestHeader("Authorization") String token, @Valid @RequestBody UpdateSummaryRequestDto updateSummaryRequestDto) {
        AiGeneratedSummary aiGeneratedSummary = summaryService.UpdateSummary(token, updateSummaryRequestDto);

        UpdateSummaryResponseDto updateSummaryResponseDto = UpdateSummaryResponseDto.builder()
                .aiGeneratedSummaryId(aiGeneratedSummary.getAiGeneratedSummaryId())
                .summaryTitle(aiGeneratedSummary.getSummaryTitle())
                .summaryContent(aiGeneratedSummary.getSummaryContent())
                .createTime(aiGeneratedSummary.getSummaryFile().getCreateTime())
                .updateTime(aiGeneratedSummary.getSummaryFile().getUpdateTime())
                .build();

        return ResponseEntity.ok(updateSummaryResponseDto);
    }




}
