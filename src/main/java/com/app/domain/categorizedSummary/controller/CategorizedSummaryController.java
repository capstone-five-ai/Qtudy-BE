package com.app.domain.categorizedSummary.controller;

import com.app.domain.categorizedSummary.dto.CategorizedSummaryDto;
import com.app.domain.categorizedSummary.entity.CategorizedSummary;
import com.app.domain.categorizedSummary.mapper.CategorizedSummaryMapper;
import com.app.domain.categorizedSummary.service.CategorizedSummaryService;
import com.app.domain.memberSavedSummary.dto.MemberSavedSummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/categorized-summary")
@Validated
@RequiredArgsConstructor
public class CategorizedSummaryController {
    private final CategorizedSummaryService categorizedSummaryService;

    private final CategorizedSummaryMapper categorizedSummaryMapper;

    @PostMapping("/new")
    public ResponseEntity createCategorizedSummary(@Valid @RequestBody CategorizedSummaryDto.Post categorizedSummaryPostDto) {
        List<Long> categorizedSummaryIdList = new ArrayList<>();

        for (Long categoryId : categorizedSummaryPostDto.getCategoryIdList()) {
            CategorizedSummary categorizedSummary =
                    categorizedSummaryService.createCategorizedSummary(categoryId,
                            categorizedSummaryPostDto.getMemberSavedSummaryId(), categorizedSummaryPostDto.getAiGeneratedSummaryId());
            categorizedSummaryIdList.add(categorizedSummary.getCategorizedSummaryId());
        }

        CategorizedSummaryDto.PostResponse postResponse = categorizedSummaryMapper.
                categorizedSummaryToPostResponse(categorizedSummaryIdList, categorizedSummaryPostDto);

        return ResponseEntity.ok(postResponse);
    }

    @PatchMapping("/edit/{categorizedSummaryId}")
    public ResponseEntity updateCategorizedSummary(@PathVariable @Positive Long categorizedSummaryId,
                                                   @Valid @RequestBody MemberSavedSummaryDto.Patch problemPatchDto) {
        CategorizedSummary categorizedSummary = categorizedSummaryService.
                updateCategorizedSummary(categorizedSummaryId, problemPatchDto);
        CategorizedSummaryDto.Response response = categorizedSummaryMapper.categorizedSummaryToResponse(categorizedSummary);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{categorizedSummaryId}")
    public ResponseEntity<Void> deleteCategorizedProblem(@PathVariable @Positive Long categorizedSummaryId) {
        categorizedSummaryService.deleteCategorizedSummary(categorizedSummaryId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{categorizedSummaryId}")
    public ResponseEntity getCategorizedSummary(@PathVariable @Positive Long categorizedSummaryId) {
        CategorizedSummary categorizedSummary = categorizedSummaryService.findVerifiedCategorizedSummaryByCategorizedSummaryId(categorizedSummaryId);
        CategorizedSummaryDto.Response response = categorizedSummaryMapper.categorizedSummaryToResponse(categorizedSummary);

        return ResponseEntity.ok(response);
    }
}
