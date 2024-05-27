package com.app.domain.summary.membersavedsummary.repository;

import com.app.domain.summary.membersavedsummary.entity.MemberSavedSummary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberSavedSummaryRepository extends JpaRepository<MemberSavedSummary, Long> {
}
