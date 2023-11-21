package com.app.domain.memberSavedProblem.repository;

import com.app.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberSavedProblemRepository extends JpaRepository<Member, Long> {
}
