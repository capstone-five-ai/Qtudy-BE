package com.app.domain.membersavedsummary.controller;

import com.app.domain.membersavedsummary.dto.MemberSavedSummaryDto;
import com.app.domain.membersavedsummary.entity.MemberSavedSummary;
import com.app.domain.membersavedsummary.mapper.MemberSavedSummaryMapper;
import com.app.domain.membersavedsummary.service.MemberSavedSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

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

    @PostMapping("/download-pdf/{memberSavedSummaryId}")
    public ResponseEntity<byte[]> downloadSummaryPdf(@PathVariable @Positive Long memberSavedSummaryId) throws IOException {
        MemberSavedSummaryDto.pdfResponse response = memberSavedSummaryService.createSummaryPdf(memberSavedSummaryId);

        byte[] pdfContent = response.getPdfContent();
        String title = response.getTitle();

        String encodedFilename = URLEncoder.encode(title, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20") + ".pdf";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        String contentDisposition = "attachment; filename=\"" + encodedFilename + "\"; filename*=UTF-8''" + encodedFilename;
        headers.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);

        return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
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

    @GetMapping("/view-pdf/{memberSavedSummaryId}")
    public ModelAndView viewPdf(@PathVariable @Positive Long memberSavedSummaryId, Model model) throws IOException {
        MemberSavedSummaryDto.pdfResponse response = memberSavedSummaryService.createSummaryPdf(memberSavedSummaryId);
        String encodedPdf = Base64.getEncoder().encodeToString(response.getPdfContent());
        model.addAttribute("encodedPdf", encodedPdf);
        model.addAttribute("title", response.getTitle());
        return new ModelAndView("pdfView");
    }

    @GetMapping("/{memberSavedSummaryId}")
    public ResponseEntity getMemberSavedSummary(@PathVariable @Positive Long memberSavedSummaryId,
                                                HttpServletRequest httpServletRequest) {
        MemberSavedSummary memberSavedSummary = memberSavedSummaryService.findVerifiedSummaryBySummaryId(memberSavedSummaryId);
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        Boolean isWriter = false;
        // "Authorization" 헤더가 존재하면 checkIsWriter 함수 호출
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            isWriter = memberSavedSummaryService.checkIsWriter(httpServletRequest, memberSavedSummary);
        }

        MemberSavedSummaryDto.LinkedSharedResponse response = memberSavedSummaryMapper.summaryToLinkedSharedResponse(memberSavedSummary, isWriter);

        return ResponseEntity.ok(response);
    }


}
