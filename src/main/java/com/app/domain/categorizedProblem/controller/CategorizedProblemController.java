package com.app.domain.categorizedProblem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categorized-problem")
@Validated
@RequiredArgsConstructor
public class CategorizedProblemController { }
