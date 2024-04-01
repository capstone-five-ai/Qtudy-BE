package com.app.domain.member.controller;

import com.app.api.login.validator.OauthValidator;
import com.app.domain.member.mapper.MemberMapper;
import com.app.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@Validated
@RequiredArgsConstructor
public class MemberController {}