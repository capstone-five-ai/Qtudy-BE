package com.app.domain.problem.membersavedproblem.repository;

import com.app.domain.problem.membersavedproblem.entity.MemberSavedProblem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberSavedProblemRepository extends JpaRepository<MemberSavedProblem, Long> {
}
