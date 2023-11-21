package com.app.domain.memberSavedSummary.repository;

import com.app.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberSavedSummaryRepository extends JpaRepository<Member, Long> {
}
