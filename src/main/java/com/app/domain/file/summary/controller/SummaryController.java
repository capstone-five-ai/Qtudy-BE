package com.app.domain.file.summary.controller;

import com.app.domain.file.summary.dto.SummaryDto;
import com.app.domain.file.summary.service.SummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/ai")
public class SummaryController {

    @Autowired
    SummaryService summaryService;

    @PostMapping("/generateSummary")
    public ResponseEntity<String> generateContent(@Valid @RequestBody SummaryDto summaryDto){
        return summaryService.generateSummary(summaryDto);
    }



}
