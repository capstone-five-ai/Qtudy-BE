package com.app.domain.categorizedsummary.service;

import com.app.domain.categorizedsummary.entity.CategorizedSummary;
import com.app.domain.categorizedsummary.repository.CategorizedSummaryRepository;
import com.app.domain.category.entity.Category;
import com.app.domain.category.service.CategoryService;
import com.app.domain.member.entity.Member;
import com.app.domain.member.service.MemberService;
import com.app.domain.summary.dto.SummaryDto;
import com.app.domain.summary.entity.Summary;
import com.app.domain.summary.membersavedsummary.service.MemberSavedSummaryService;
import com.app.domain.summary.repository.SummaryRepository;
import com.app.domain.summary.service.SummaryService;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.BusinessException;
import com.app.global.error.exception.EntityNotFoundException;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CategorizedSummaryService {
    private final CategoryService categoryService;

    private final MemberSavedSummaryService memberSavedSummaryService;

    private final MemberService memberService;

    private final CategorizedSummaryRepository categorizedSummaryRepository;

    private final SummaryService summaryService;

    private final SummaryRepository summaryRepository;

    public CategorizedSummary createCategorizedSummary(Long categoryId, Long summaryId) {
        checkForDuplicateCategorizedProblem(categoryId, summaryId);

        Category category = categoryService.findVerifiedCategoryByCategoryId(categoryId);
        CategorizedSummary categorizedSummary = createCategorizedSummaryEntity(category, summaryId);

        return categorizedSummaryRepository.save(categorizedSummary);
    }

    private void checkForDuplicateCategorizedProblem(Long categoryId, Long summaryId) {
        boolean exists = categorizedSummaryRepository.existsByCategoryCategoryIdAndSummarySummaryId(
                categoryId,
                summaryId
        );
        if (exists) {
            throw new BusinessException(ErrorCode.DUPLICATE_CATEGORIZED_SUMMARY);
        }
    }

    private CategorizedSummary createCategorizedSummaryEntity(Category category, Long summaryId) {
        Summary summary = summaryService.findVerifiedSummaryBySummaryId(summaryId);
        CategorizedSummary categorizedSummary = CategorizedSummary.builder()
                .summary(summary)
                .build();

        categorizedSummary.updateCategory(category);
        return categorizedSummary;
    }

    public SummaryDto.pdfResponse createSummaryPdf(Long categorizedSummaryId) throws IOException{
        CategorizedSummary categorizedSummary = findVerifiedCategorizedSummaryByCategorizedSummaryId(categorizedSummaryId);
        Long summaryId = categorizedSummary.getSummary().getSummaryId();
        return summaryService.createSummaryPdf(summaryId);
    }

    public CategorizedSummary updateCategorizedSummary(Long categorizedSummaryId, SummaryDto.Patch summaryPatchDto) {
        CategorizedSummary categorizedSummary = findVerifiedCategorizedSummaryByCategorizedSummaryId(categorizedSummaryId);
        summaryService.updateSummary(
                summaryPatchDto.getSummaryTitle(),
                summaryPatchDto.getSummaryContent(),
                categorizedSummary.getSummary().getSummaryId()
        );
        return categorizedSummaryRepository.save(categorizedSummary);
    }

    public void deleteCategorizedSummary(Long categorizedSummaryId) {
        CategorizedSummary categorizedSummary = findVerifiedCategorizedSummaryByCategorizedSummaryId(categorizedSummaryId);

        categorizedSummaryRepository.deleteById(categorizedSummaryId);

        Summary summary = categorizedSummary.getSummary();
        Long summaryId = summary.getSummaryId();
        if (summary.isMemberSavedSummary() && !isSummaryUsedInOtherCategorizedSummarys(summaryId)) {
            summaryRepository.deleteById(summaryId);
        }
    }

    private boolean isSummaryUsedInOtherCategorizedSummarys(Long summaryId){
        return categorizedSummaryRepository.existsBySummarySummaryId(summaryId);
    }

    public CategorizedSummary findVerifiedCategorizedSummaryByCategorizedSummaryId(Long categorizedSummaryId) {
        return categorizedSummaryRepository.findById(categorizedSummaryId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.CATEGORIZED_SUMMARY_NOT_EXISTS));
    }

    @Transactional(readOnly = true)
//    @Cacheable(value = "categorizedSummary", key = "#categoryId")
    public Page<CategorizedSummary> findCategorziedSummarysByCategoryId(Long categoryId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return categorizedSummaryRepository.findByCategoryCategoryId(categoryId, pageRequest);
    }

    public Boolean checkIsWriter(HttpServletRequest httpServletRequest, CategorizedSummary categorizedSummary) {
        Member member = memberService.getLoginMember(httpServletRequest);
        if (categorizedSummary.getCategory().getMember().getMemberId() == member.getMemberId()) {
            return true;
        }
        return false;
    }
}
