package com.app.domain.categorizedProblem.controller;

import com.app.domain.categorizedProblem.dto.CategorizedProblemDto;
import com.app.domain.categorizedProblem.entity.CategorizedProblem;
import com.app.domain.categorizedProblem.mapper.CategorizedProblemMapper;
import com.app.domain.categorizedProblem.service.CategorizedProblemService;
import com.app.domain.memberSavedProblem.dto.MemberSavedProblemDto;
import com.app.domain.memberSavedSummary.dto.MemberSavedSummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
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

    @PostMapping("/download-problem-pdf/{categoryId}")
    public ResponseEntity<byte[]> downloadCategorizedProblemsPdf(@PathVariable @Positive Long categoryId) throws IOException{
        MemberSavedSummaryDto.pdfResponse response = categorizedProblemService.createCategorizedProblemsPdf(categoryId);

        byte[] pdfContent = response.getPdfContent();
        String title = response.getTitle();

        String encodedFilename = URLEncoder.encode(title, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20") + ".pdf";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        String contentDisposition = "attachment; filename=\"" + encodedFilename + "\"; filename*=UTF-8''" + encodedFilename;
        headers.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);

        return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
    }

    @PostMapping("/download-answer-pdf/{categoryId}")
    public ResponseEntity<byte[]> downloadCategorizedProblemsAnswerPdf(@PathVariable @Positive Long categoryId) throws IOException{
        MemberSavedSummaryDto.pdfResponse response = categorizedProblemService.createCategorizedProblemsAnswerPdf(categoryId);

        byte[] pdfContent = response.getPdfContent();
        String title = response.getTitle();

        String encodedFilename = URLEncoder.encode(title, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20") + ".pdf";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        String contentDisposition = "attachment; filename=\"" + encodedFilename + "\"; filename*=UTF-8''" + encodedFilename;
        headers.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);

        return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
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

    @GetMapping("/view-pdf/{categoryId}")
    public ModelAndView viewPdf(@PathVariable @Positive Long categoryId, Model model) throws IOException {
        MemberSavedSummaryDto.pdfResponse response =  categorizedProblemService.createCategorizedProblemsPdf(categoryId);
        String encodedPdf = Base64.getEncoder().encodeToString(response.getPdfContent());
        model.addAttribute("encodedPdf", encodedPdf);
        model.addAttribute("title", response.getTitle());
        return new ModelAndView("pdfView");
    }
}
