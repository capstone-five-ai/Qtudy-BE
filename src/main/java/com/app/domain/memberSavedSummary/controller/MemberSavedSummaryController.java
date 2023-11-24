package com.app.domain.memberSavedSummary.controller;

import com.app.domain.memberSavedProblem.dto.MemberSavedProblemDto;
import com.app.domain.memberSavedProblem.entity.MemberSavedProblem;
import com.app.domain.memberSavedSummary.dto.MemberSavedSummaryDto;
import com.app.domain.memberSavedSummary.entity.MemberSavedSummary;
import com.app.domain.memberSavedSummary.mapper.MemberSavedSummaryMapper;
import com.app.domain.memberSavedSummary.service.MemberSavedSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/api/member-saved-summary")
@Validated
@RequiredArgsConstructor
public class MemberSavedSummaryController {

    private final MemberSavedSummaryService memberSavedSummaryService;

    private final MemberSavedSummaryMapper memberSavedSummaryMapper;

    @PostMapping("/new")
    public ResponseEntity createSummary(@Valid @RequestBody MemberSavedSummaryDto.Post summaryPostDto,
                                        HttpServletRequest httpServletRequest) {
        MemberSavedSummary memberSavedSummary =
                memberSavedSummaryService.
                        createSummary(memberSavedSummaryMapper.summaryPostDtoToSummary(summaryPostDto), httpServletRequest);

        MemberSavedSummaryDto.Response response = memberSavedSummaryMapper.summaryToResponse(memberSavedSummary);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/edit/{memberSavedSummaryId}")
    public ResponseEntity updateSummary(@PathVariable @Positive Long memberSavedSummaryId,
                                        @Valid @RequestBody MemberSavedSummaryDto.Patch summaryPatchDto){
        MemberSavedSummary memberSavedSummary =
                memberSavedSummaryService.
                        updateSummary(memberSavedSummaryMapper.summaryPatchDtoToSummary(summaryPatchDto), memberSavedSummaryId);

        MemberSavedSummaryDto.Response response = memberSavedSummaryMapper.summaryToResponse(memberSavedSummary);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{memberSavedSummaryId}")
    public ResponseEntity<Void> deleteSummary(@PathVariable @Positive Long memberSavedSummaryId){
        memberSavedSummaryService.deleteSummary(memberSavedSummaryId);
        return ResponseEntity.ok().build();
    }
}
