package com.app.domain.problem.service;

import com.app.domain.problem.entity.Problem;
import com.app.domain.problem.repository.ProblemRepository;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProblemService {

    private final ProblemRepository problemRepository;

    @Transactional(readOnly = true)
    public Problem findVerifiedProblemByProblemId(Long problemId) {
        return problemRepository.findById(problemId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.PROBLEM_NOT_EXISTS));
    }

}
