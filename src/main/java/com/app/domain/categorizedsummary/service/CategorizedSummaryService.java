package com.app.domain.categorizedsummary.service;

import com.app.domain.categorizedsummary.entity.CategorizedSummary;
import com.app.domain.categorizedsummary.repository.CategorizedSummaryRepository;
import com.app.domain.category.entity.Category;
import com.app.domain.category.service.CategoryService;
import com.app.domain.member.entity.Member;
import com.app.domain.member.service.MemberService;
import com.app.domain.summary.aigeneratedsummary.service.AiGeneratedSummaryService;
import com.app.domain.summary.entity.Summary;
import com.app.domain.summary.membersavedsummary.dto.MemberSavedSummaryDto;
import com.app.domain.summary.membersavedsummary.mapper.MemberSavedSummaryMapper;
import com.app.domain.summary.membersavedsummary.service.MemberSavedSummaryService;
import com.app.domain.summary.service.SummaryService;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.BusinessException;
import com.app.global.error.exception.EntityNotFoundException;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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

    private final MemberSavedSummaryMapper memberSavedSummaryMapper;

    private final MemberService memberService;

    private final AiGeneratedSummaryService aiGeneratedSummaryService;

    private final CategorizedSummaryRepository categorizedSummaryRepository;

    private final SummaryService summaryService;

    public CategorizedSummary createCategorizedSummary(Long categoryId, Integer summaryId) {
        checkForDuplicateCategorizedProblem(categoryId, summaryId);

        Category category = categoryService.findVerifiedCategoryByCategoryId(categoryId);
        CategorizedSummary categorizedSummary = createCategorizedSummaryEntity(category, summaryId);

        return categorizedSummaryRepository.save(categorizedSummary);
    }

    private void checkForDuplicateCategorizedProblem(Long categoryId, Integer summaryId) {
        boolean exists = categorizedSummaryRepository.existsByCategoryCategoryIdAndSummarySummaryId(
                categoryId,
                summaryId
        );
        if (exists) {
            throw new BusinessException(ErrorCode.DUPLICATE_CATEGORIZED_SUMMARY);
        }
    }

    private CategorizedSummary createCategorizedSummaryEntity(Category category, Integer summaryId) {
        Summary summary = summaryService.findVerifiedSummaryBySummaryId(summaryId);
        CategorizedSummary categorizedSummary = CategorizedSummary.builder()
                .summary(summary)
                .build();

        categorizedSummary.updateCategory(category);
        return categorizedSummary;
    }

    public MemberSavedSummaryDto.pdfResponse createSummaryPdf(Long categorizedSummaryId) throws IOException{
        CategorizedSummary categorizedSummary = findVerifiedCategorizedSummaryByCategorizedSummaryId(categorizedSummaryId);
        if (categorizedSummary.getMemberSavedSummary() != null) {
            // MemberSavedSummary와 연관된 경우
            Long memberSavedSummaryId = categorizedSummary.getMemberSavedSummary().getSummaryId().longValue();
            return memberSavedSummaryService.createSummaryPdf(memberSavedSummaryId);
        }else{
            // AiGeneratedSummary와 연관된 경우
            Integer aiGeneratedSummaryId = categorizedSummary.getAiGeneratedSummary().getSummaryId();
            return aiGeneratedSummaryService.createSummaryPdf(aiGeneratedSummaryId);
        }
    }

    public CategorizedSummary updateCategorizedSummary(Long categorizedSummaryId, MemberSavedSummaryDto.Patch summaryPatchDto) {
        CategorizedSummary categorizedSummary = findVerifiedCategorizedSummaryByCategorizedSummaryId(categorizedSummaryId);
        if (categorizedSummary.getMemberSavedSummary() != null) {
            memberSavedSummaryService.updateSummary(memberSavedSummaryMapper.
                    summaryPatchDtoToSummary(summaryPatchDto), categorizedSummary.getMemberSavedSummary().getSummaryId().longValue());
        }
        else{
            aiGeneratedSummaryService.updateSummary(memberSavedSummaryMapper.
                    summaryPatchDtoToSummary(summaryPatchDto), categorizedSummary.getAiGeneratedSummary().getSummaryId());
        }

        return categorizedSummaryRepository.save(categorizedSummary);
    }
    public void deleteCategorizedSummary(Long categorizedSummaryId) {
        CategorizedSummary categorizedSummary = findVerifiedCategorizedSummaryByCategorizedSummaryId(categorizedSummaryId);

        Long memberSavedSummaryId = categorizedSummary.getMemberSavedSummary() != null
                ? categorizedSummary.getMemberSavedSummary().getSummaryId().longValue()
                : null;

        categorizedSummaryRepository.deleteById(categorizedSummaryId);

        if (memberSavedSummaryId != null && !isMemberSavedSummaryUsedInOtherCategorizedSummarys(memberSavedSummaryId)) {
            memberSavedSummaryService.deleteSummary(memberSavedSummaryId);
        }
    }

    private boolean isMemberSavedSummaryUsedInOtherCategorizedSummarys(Long memberSavedSummaryId){
        return categorizedSummaryRepository.existsByMemberSavedSummaryMemberSavedSummaryId(memberSavedSummaryId);
    }

    public CategorizedSummary findVerifiedCategorizedSummaryByCategorizedSummaryId(Long categorizedSummaryId) {
        return categorizedSummaryRepository.findById(categorizedSummaryId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.CATEGORIZED_SUMMARY_NOT_EXISTS));
    }

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
