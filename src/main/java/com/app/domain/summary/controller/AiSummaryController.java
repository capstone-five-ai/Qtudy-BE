package com.app.domain.summary.controller;

import com.app.domain.summary.dto.AiSummaryDto;
import com.app.domain.summary.service.AiSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/summary")
public class AiSummaryController {

    @Autowired
    AiSummaryService aiSummaryService;

    @PostMapping("/generateSummary")
    public ResponseEntity<String> generateSummary(@RequestHeader("Authorization") String token, @Valid @RequestBody AiSummaryDto aiSummaryDto){
        return aiSummaryService.generateSummary(token, aiSummaryDto);
    }

}
