package com.app.domain.memberProblemChoice.repository;

import com.app.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberProblemChoiceRepository extends JpaRepository<Member, Long> {
}
