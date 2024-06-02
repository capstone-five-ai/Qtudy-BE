package com.app.domain.problem.membersavedproblem.controller;

import com.app.domain.problem.membersavedproblem.doc.MemberSavedProblemApi;
import com.app.domain.problem.membersavedproblem.dto.MemberSavedProblemDto;
import com.app.domain.problem.membersavedproblem.entity.MemberSavedProblem;
import com.app.domain.problem.membersavedproblem.mapper.MemberSavedProblemMapper;
import com.app.domain.problem.membersavedproblem.service.MemberSavedProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/api/member-saved-problem")
@Validated
@RequiredArgsConstructor
public class MemberSavedProblemController implements MemberSavedProblemApi{

    private final MemberSavedProblemService memberSavedProblemService;

    private final MemberSavedProblemMapper memberSavedProblemMapper;

    @PostMapping("/new")
    public ResponseEntity createProblem(@Valid @RequestBody MemberSavedProblemDto.Post problemPostDto,
                                        HttpServletRequest httpServletRequest){
        MemberSavedProblem memberSavedProblem =
                memberSavedProblemService
                        .createProblem(memberSavedProblemMapper.problemPostDtoToProblem(problemPostDto), httpServletRequest);

        MemberSavedProblemDto.Response response = memberSavedProblemMapper.problemToResponse(memberSavedProblem);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/edit/{memberSavedProblemId}")
    public ResponseEntity updateProblem(@PathVariable @Positive Long memberSavedProblemId,
                                        @Valid @RequestBody MemberSavedProblemDto.Patch problemPatchDto){
        MemberSavedProblem memberSavedProblem =
                memberSavedProblemService.
                        updateProblem(memberSavedProblemMapper.problemPatchDtoToProblem(problemPatchDto), memberSavedProblemId);

        MemberSavedProblemDto.Response response = memberSavedProblemMapper.problemToResponse(memberSavedProblem);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{memberSavedProblemId}")
    public ResponseEntity<Void> deleteProblem(@PathVariable @Positive Long memberSavedProblemId){
        memberSavedProblemService.deleteProblem(memberSavedProblemId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{memberSavedProblemId}")
    public ResponseEntity getMemberSavedProblem(@PathVariable @Positive Long memberSavedProblemId,
                                                HttpServletRequest httpServletRequest) {
        MemberSavedProblem memberSavedProblem = memberSavedProblemService.findVerifiedProblemByProblemId(memberSavedProblemId);

        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        Boolean isWriter = false;
        // "Authorization" 헤더가 존재하면 checkIsWriter 함수 호출
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            isWriter = memberSavedProblemService.checkIsWriter(httpServletRequest, memberSavedProblem);
        }

        MemberSavedProblemDto.LinkSharedResponse response = memberSavedProblemMapper.problemToLinkSharedResponse(memberSavedProblem, isWriter);

        return ResponseEntity.ok(response);
    }
}
