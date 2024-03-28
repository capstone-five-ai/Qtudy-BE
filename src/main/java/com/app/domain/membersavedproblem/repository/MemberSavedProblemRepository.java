package com.app.domain.membersavedproblem.repository;

import com.app.domain.membersavedproblem.entity.MemberSavedProblem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberSavedProblemRepository extends JpaRepository<MemberSavedProblem, Long> {
}
