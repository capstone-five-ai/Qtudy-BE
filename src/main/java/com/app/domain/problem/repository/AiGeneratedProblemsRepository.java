package com.app.domain.problem.repository;

import com.app.domain.problem.entity.AiGeneratedProblems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AiGeneratedProblemsRepository extends JpaRepository<AiGeneratedProblems, Integer> {

    List<AiGeneratedProblems> findByProblemFiles_FileId(int fileId);
}
