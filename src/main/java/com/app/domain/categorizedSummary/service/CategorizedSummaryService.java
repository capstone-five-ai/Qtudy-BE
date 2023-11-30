package com.app.domain.categorizedSummary.service;

import com.app.domain.categorizedProblem.entity.CategorizedProblem;
import com.app.domain.categorizedSummary.entity.CategorizedSummary;
import com.app.domain.categorizedSummary.repository.CategorizedSummaryRepository;
import com.app.domain.category.entity.Category;
import com.app.domain.category.service.CategoryService;
import com.app.domain.memberSavedSummary.dto.MemberSavedSummaryDto;
import com.app.domain.memberSavedSummary.entity.MemberSavedSummary;
import com.app.domain.memberSavedSummary.mapper.MemberSavedSummaryMapper;
import com.app.domain.memberSavedSummary.service.MemberSavedSummaryService;
import com.app.domain.summary.entity.AiGeneratedSummary;
import com.app.domain.summary.service.SummaryService;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.BusinessException;
import com.app.global.error.exception.EntityNotFoundException;
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

    private final SummaryService aiGeneratedSummaryService;

    private final CategorizedSummaryRepository categorizedSummaryRepository;

    public CategorizedSummary createCategorizedSummary(Long categoryId, Long memberSavedSummaryId,
                                                       Integer aiGeneratedSummaryId) {
        validateCategorizedSummaryInputs(memberSavedSummaryId, aiGeneratedSummaryId);
        checkForDuplicateCategorizedProblem(categoryId, memberSavedSummaryId, aiGeneratedSummaryId);

        Category category = categoryService.findVerifiedCategoryByCategoryId(categoryId);
        CategorizedSummary categorizedSummary = createCategorizedSummaryEntity(category, memberSavedSummaryId, aiGeneratedSummaryId);

        return categorizedSummaryRepository.save(categorizedSummary);
    }

    public CategorizedSummary updateCategorizedSummary(Long categorizedSummaryId, MemberSavedSummaryDto.Patch summaryPatchDto) {
        CategorizedSummary categorizedSummary = findVerifiedCategorizedSummaryByCategorizedSummaryId(categorizedSummaryId);
        if (categorizedSummary.getMemberSavedSummary().getMemberSavedSummaryId() != null) {
            memberSavedSummaryService.updateSummary(memberSavedSummaryMapper.
                    summaryPatchDtoToSummary(summaryPatchDto), categorizedSummary.getMemberSavedSummary().getMemberSavedSummaryId());
        }
        else{
            aiGeneratedSummaryService.updateSummary(memberSavedSummaryMapper.
                    summaryPatchDtoToSummary(summaryPatchDto), categorizedSummary.getAiGeneratedSummary().getAiGeneratedSummaryId());
        }

        return categorizedSummaryRepository.save(categorizedSummary);
    }

    public void deleteCategorizedSummary(Long categorizedSummaryId) {
        CategorizedSummary categorizedSummary = findVerifiedCategorizedSummaryByCategorizedSummaryId(categorizedSummaryId);

        Long memberSavedSummaryId = categorizedSummary.getMemberSavedSummary() != null
                ? categorizedSummary.getMemberSavedSummary().getMemberSavedSummaryId()
                : null;

        categorizedSummaryRepository.deleteById(categorizedSummaryId);

        if (memberSavedSummaryId != null && !isMemberSavedSummaryUsedInOtherCategorizedSummarys(memberSavedSummaryId)) {
            memberSavedSummaryService.deleteSummary(memberSavedSummaryId);
        }
    }

    private boolean isMemberSavedSummaryUsedInOtherCategorizedSummarys(Long memberSavedSummaryId){
        return categorizedSummaryRepository.existsByMemberSavedSummaryMemberSavedSummaryId(memberSavedSummaryId);
    }
    private CategorizedSummary createCategorizedSummaryEntity(Category category, Long memberSavedSummaryId, Integer aiGeneratedSummaryId) {
        CategorizedSummary categorizedSummary;

        if(memberSavedSummaryId != null){
            MemberSavedSummary memberSavedSummary = memberSavedSummaryService.findVerifiedSummaryBySummaryId(memberSavedSummaryId);
            categorizedSummary = CategorizedSummary.builder()
                    .memberSavedSummary(memberSavedSummary)
                    .build();
        }else{
            AiGeneratedSummary aiGenerateSummary = aiGeneratedSummaryService.findVerifiedSummaryBySummaryId(aiGeneratedSummaryId);
            categorizedSummary = CategorizedSummary.builder()
                    .aiGeneratedSummary(aiGenerateSummary)
                    .build();
        }

        categorizedSummary.updateCategory(category);
        return categorizedSummary;
    }

    private void validateCategorizedSummaryInputs(Long memberSavedSummaryId, Integer aiGeneratedSummaryId) {
        boolean bothNull = memberSavedSummaryId == null && aiGeneratedSummaryId == null;
        boolean bothNotNull = memberSavedSummaryId != null && aiGeneratedSummaryId != null;

        if (bothNull || bothNotNull) {
            throw new BusinessException(bothNull ? ErrorCode.FK_NOT_EXISTS : ErrorCode.FK_BOTH_EXISTS);
        }
    }

    private void checkForDuplicateCategorizedProblem(Long categoryId, Long memberSavedSummaryId, Integer aiGeneratedSummaryId) {
        boolean exists = memberSavedSummaryId != null
                ? categorizedSummaryRepository.existsByCategoryCategoryIdAndMemberSavedSummaryMemberSavedSummaryId(categoryId, memberSavedSummaryId)
                : categorizedSummaryRepository.existsByCategoryCategoryIdAndAiGeneratedSummaryAiGeneratedSummaryId(categoryId, aiGeneratedSummaryId);

    if(exists){
        throw new BusinessException(ErrorCode.DUPLICATE_CATEGORIZED_SUMMARY);
    }
    }

    public CategorizedSummary findVerifiedCategorizedSummaryByCategorizedSummaryId(Long categorizedSummaryId) {
        return categorizedSummaryRepository.findById(categorizedSummaryId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.CATEGORIZED_SUMMARY_NOT_EXISTS));
    }

    public Page<CategorizedSummary> findCategorziedSummarysByCategoryId(Long categoryId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return categorizedSummaryRepository.findByCategoryCategoryId(categoryId, pageRequest);
    }
}
