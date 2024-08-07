package com.app.domain.categorizedsummary.controller;

import com.app.domain.categorizedsummary.doc.CategorizedSummaryApi;
import com.app.domain.categorizedsummary.dto.CategorizedSummaryDto;
import com.app.domain.categorizedsummary.entity.CategorizedSummary;
import com.app.domain.categorizedsummary.mapper.CategorizedSummaryMapper;
import com.app.domain.categorizedsummary.service.CategorizedSummaryService;
import com.app.domain.summary.dto.SummaryDto;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categorized-summary")
@Validated
@RequiredArgsConstructor
public class CategorizedSummaryController implements CategorizedSummaryApi {
    private final CategorizedSummaryService categorizedSummaryService;

    private final CategorizedSummaryMapper categorizedSummaryMapper;

    @PostMapping("/new")
    public ResponseEntity createCategorizedSummary(@Valid @RequestBody CategorizedSummaryDto.Post categorizedSummaryPostDto) {
        List<Long> categorizedSummaryIdList = new ArrayList<>();

        for (Long categoryId : categorizedSummaryPostDto.getCategoryIdList()) {
            CategorizedSummary categorizedSummary = categorizedSummaryService.createCategorizedSummary(
                    categoryId,
                    categorizedSummaryPostDto.getSummaryId()
            );
            categorizedSummaryIdList.add(categorizedSummary.getCategorizedSummaryId());
        }

        CategorizedSummaryDto.PostResponse postResponse = categorizedSummaryMapper.
                categorizedSummaryToPostResponse(categorizedSummaryIdList, categorizedSummaryPostDto);

        return ResponseEntity.ok(postResponse);
    }

    @PostMapping("/download-pdf/{categorizedSummaryId}")
    public ResponseEntity<byte[]> downloadSummaryPdf(@PathVariable @Positive Long categorizedSummaryId) throws IOException {
        SummaryDto.pdfResponse response = categorizedSummaryService.createSummaryPdf(categorizedSummaryId);

        byte[] pdfContent = response.getPdfContent();
        String title = response.getTitle();

        String encodedFilename = URLEncoder.encode(title, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20") + ".pdf";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        String contentDisposition = "attachment; filename=\"" + encodedFilename + "\"; filename*=UTF-8''" + encodedFilename;
        headers.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);

        return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
    }
    @PatchMapping("/edit/{categorizedSummaryId}")
    public ResponseEntity updateCategorizedSummary(@PathVariable @Positive Long categorizedSummaryId,
                                                   @Valid @RequestBody SummaryDto.Patch problemPatchDto) {
        CategorizedSummary categorizedSummary = categorizedSummaryService.
                updateCategorizedSummary(categorizedSummaryId, problemPatchDto);
        CategorizedSummaryDto.Response response = categorizedSummaryMapper.categorizedSummaryToResponse(categorizedSummary);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{categorizedSummaryId}")
    public ResponseEntity<Void> deleteCategorizedSummary(@PathVariable @Positive Long categorizedSummaryId) {
        categorizedSummaryService.deleteCategorizedSummary(categorizedSummaryId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{categorizedSummaryId}")
    public ResponseEntity getCategorizedSummary(@PathVariable @Positive Long categorizedSummaryId,
                                                HttpServletRequest httpServletRequest) {
        CategorizedSummary categorizedSummary = categorizedSummaryService.findVerifiedCategorizedSummaryByCategorizedSummaryId(categorizedSummaryId);

        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        Boolean isWriter = false;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            isWriter = categorizedSummaryService.checkIsWriter(httpServletRequest, categorizedSummary);
        }
        CategorizedSummaryDto.LinkedSharedResponse response = categorizedSummaryMapper.categorizedSummaryToLinkedSharedResponse(categorizedSummary, isWriter);

        return ResponseEntity.ok(response);
    }
}
