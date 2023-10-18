package com.app.domain.aicreate.summary.controller;

import com.app.domain.aicreate.summary.service.SummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/ai/summary")
public class SummaryController {

    @Autowired
    SummaryService summaryService;

    @PostMapping("/generateSummary")
    public String generateContent(@RequestParam String text) throws IOException {
        return summaryService.generateSummary(text);
    }



}
