package com.app.domain.categorizedSummary.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categorized-summary")
@Validated
@RequiredArgsConstructor
public class CategorizedSummaryController {
}
