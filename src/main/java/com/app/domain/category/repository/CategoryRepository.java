package com.app.domain.category.repository;

import com.app.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Member, Long> {
}
