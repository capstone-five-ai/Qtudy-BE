package com.app.domain.membersavedsummary.repository;

import com.app.domain.membersavedsummary.entity.MemberSavedSummary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberSavedSummaryRepository extends JpaRepository<MemberSavedSummary, Long> {
}
