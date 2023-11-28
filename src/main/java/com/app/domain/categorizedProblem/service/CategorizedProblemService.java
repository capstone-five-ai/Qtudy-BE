package com.app.domain.categorizedProblem.service;

import com.app.domain.categorizedProblem.entity.CategorizedProblem;
import com.app.domain.categorizedProblem.repository.CategorizedProblemRepository;
import com.app.domain.category.entity.Category;
import com.app.domain.category.service.CategoryService;
import com.app.domain.memberSavedProblem.dto.MemberSavedProblemDto;
import com.app.domain.memberSavedProblem.entity.MemberSavedProblem;
import com.app.domain.memberSavedProblem.mapper.MemberSavedProblemMapper;
import com.app.domain.memberSavedProblem.service.MemberSavedProblemService;
import com.app.domain.problem.entity.AiGeneratedProblem;
import com.app.domain.problem.service.ProblemService;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.BusinessException;
import com.app.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CategorizedProblemService {
    private final CategoryService categoryService;

    private final MemberSavedProblemService memberSavedProblemService;

    private final MemberSavedProblemMapper memberSavedProblemMapper;

    private final ProblemService aiGeneratedProblemService;

    private final CategorizedProblemRepository categorizedProblemRepository;

    public CategorizedProblem createCategorizedProblem(Long categoryId, Long memberSavedProblemId,
                                                       Integer aiGeneratedProblemId) {
        validateCategorizedProblemInputs(memberSavedProblemId, aiGeneratedProblemId);
        checkForDuplicateCategorizedProblem(categoryId, memberSavedProblemId, aiGeneratedProblemId);

        Category category = categoryService.findVerifiedCategoryByCategoryId(categoryId);
        CategorizedProblem categorizedProblem = createCategorizedProblemEntity(category,memberSavedProblemId, aiGeneratedProblemId);

        return categorizedProblemRepository.save(categorizedProblem);
    }

    public CategorizedProblem updateCategorizedProblem(Long categorizedProblemId, MemberSavedProblemDto.Patch problemPatchDto) {
        CategorizedProblem categorizedProblem = findVerifiedCategorizedProblemByCategorizedProblemId(categorizedProblemId);
        if(categorizedProblem.getMemberSavedProblem().getMemberSavedProblemId() != null){
            memberSavedProblemService.updateProblem(memberSavedProblemMapper.
                    problemPatchDtoToProblem(problemPatchDto), categorizedProblem.getMemberSavedProblem().getMemberSavedProblemId());
        }
        else{
            aiGeneratedProblemService.updateProblem(memberSavedProblemMapper.
                    problemPatchDtoToProblem(problemPatchDto), categorizedProblem.getAiGeneratedProblem().getAiGeneratedProblemId());
        }
        return categorizedProblemRepository.save(categorizedProblem);
    }
    private CategorizedProblem createCategorizedProblemEntity(Category category, Long memberSavedProblemId, Integer aiGeneratedProblemId) {
        if (memberSavedProblemId != null) {
            MemberSavedProblem memberSavedProblem = memberSavedProblemService.findVerifiedProblemByProblemId(memberSavedProblemId);
            return CategorizedProblem.builder()
                    .category(category)
                    .memberSavedProblem(memberSavedProblem)
                    .build();
        } else {
            AiGeneratedProblem aiGeneratedProblems = aiGeneratedProblemService.findVerifiedProblemByProblemId(aiGeneratedProblemId);
            return CategorizedProblem.builder()
                    .category(category)
                    .aiGeneratedProblem(aiGeneratedProblems)
                    .build();
        }
    }

    /**
     * 카테고리화 문제에 fk값이 둘 중 하나만 있는지 확인
     * @param memberSavedProblemId
     * @param aiGeneratedProblemId
     */
    private void validateCategorizedProblemInputs(Long memberSavedProblemId, Integer aiGeneratedProblemId) {
        boolean bothNull = memberSavedProblemId == null && aiGeneratedProblemId == null;
        boolean bothNotNull = memberSavedProblemId != null && aiGeneratedProblemId != null;

        if (bothNull || bothNotNull) {
            throw new BusinessException(bothNull ? ErrorCode.FK_NOT_EXISTS : ErrorCode.FK_BOTH_EXISTS);
        }
    }

    /**
     * 카테고리화 문제의 카테고리에 이미 같은 문제가 저장되어있는지 확인
     * @param categoryId
     * @param memberSavedProblemId
     * @param aiGeneratedProblemId
     */
    private void checkForDuplicateCategorizedProblem(Long categoryId, Long memberSavedProblemId, Integer aiGeneratedProblemId) {
        boolean exists = memberSavedProblemId != null
                ? categorizedProblemRepository.existsByCategoryCategoryIdAndMemberSavedProblemMemberSavedProblemId(categoryId, memberSavedProblemId)
                : categorizedProblemRepository.existsByCategoryCategoryIdAndAiGeneratedProblemsAiGeneratedProblemId(categoryId, aiGeneratedProblemId);

        if (exists) {
            throw new BusinessException(ErrorCode.DUPLICATE_CATEGORIZED_PROBLEM);
        }
    }

    public void deleteCategorizedProblem(Long categorizedProblemID){
        CategorizedProblem categorizedProblem = findVerifiedCategorizedProblemByCategorizedProblemId(categorizedProblemID);

        categorizedProblemRepository.deleteById(categorizedProblemID);
    }

    private CategorizedProblem findVerifiedCategorizedProblemByCategorizedProblemId(Long categorizedProblemId){
        return categorizedProblemRepository.findById(categorizedProblemId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.CATEGORIZED_PROBLEM_NOT_EXISTS));
    }
}
