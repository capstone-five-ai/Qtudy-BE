package com.app.domain.categorizedProblem.repository;

import com.app.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategorizedProblemRepository extends JpaRepository<Member, Long> {
}
