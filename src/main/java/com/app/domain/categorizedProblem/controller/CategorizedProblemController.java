package com.app.domain.categorizedProblem.controller;

import com.app.domain.categorizedProblem.dto.CategorizedProblemDto;
import com.app.domain.categorizedProblem.entity.CategorizedProblem;
import com.app.domain.categorizedProblem.mapper.CategorizedProblemMapper;
import com.app.domain.categorizedProblem.service.CategorizedProblemService;
import com.app.domain.memberSavedProblem.dto.MemberSavedProblemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/categorized-problem")
@Validated
@RequiredArgsConstructor
public class CategorizedProblemController {

    private final CategorizedProblemService categorizedProblemService;

    private final CategorizedProblemMapper categorizedProblemMapper;


    @PostMapping("/new")
    public ResponseEntity createCategorizedProblem(@Valid @RequestBody CategorizedProblemDto.Post categorizedProblemPostDto) {
        List<Long> categorizedProblemIdList = new ArrayList<>();

        for (Long categoryId: categorizedProblemPostDto.getCategoryIdList()) {
            CategorizedProblem categorizedProblem =
                    categorizedProblemService.createCategorizedProblem(categoryId
                            ,categorizedProblemPostDto.getMemberSavedProblemId(), categorizedProblemPostDto.getAiGeneratedProblemId());
            categorizedProblemIdList.add(categorizedProblem.getCategorizedProblemId());
        }

        CategorizedProblemDto.PostResponse postResponse = categorizedProblemMapper.categorizedProblemToPostResponse(
                categorizedProblemIdList, categorizedProblemPostDto.getCategoryIdList(),
                categorizedProblemPostDto.getMemberSavedProblemId(), categorizedProblemPostDto.getAiGeneratedProblemId());

        return ResponseEntity.ok(postResponse);
    }

    @PatchMapping("/edit/{categorizedProblemId}")
    public ResponseEntity updateCategorizedProblem(@PathVariable @Positive Long categorizedProblemId,
                                                   @Valid @RequestBody MemberSavedProblemDto.Patch problemPatchDto) {
        CategorizedProblem categorizedProblem = categorizedProblemService.
                updateCategorizedProblem(categorizedProblemId, problemPatchDto);
        CategorizedProblemDto.Response patchResponse = CategorizedProblemDto.Response.of(categorizedProblem);

        return ResponseEntity.ok(patchResponse);

//        CategorizedProblemDto.PostResponse
    }
    @DeleteMapping("/delete/{categorizedProblemId}")
    public ResponseEntity<Void> deleteCategorizedProblem(@PathVariable @Positive Long categorizedProblemId){
        categorizedProblemService.deleteCategorizedProblem(categorizedProblemId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{categorizedProblemId}")
    public ResponseEntity getCategorizedProblem(@PathVariable @Positive Long categorizedProblemId){
        CategorizedProblem categorizedProblem = categorizedProblemService.findVerifiedCategorizedProblemByCategorizedProblemId(categorizedProblemId);
        CategorizedProblemDto.Response response = CategorizedProblemDto.Response.of(categorizedProblem);

        return ResponseEntity.ok(response);
    }
}
