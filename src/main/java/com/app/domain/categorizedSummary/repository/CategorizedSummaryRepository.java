package com.app.domain.categorizedSummary.repository;

import com.app.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategorizedSummaryRepository extends JpaRepository<Member, Long> {
}
