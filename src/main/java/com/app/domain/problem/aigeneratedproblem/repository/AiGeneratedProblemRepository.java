package com.app.domain.problem.aigeneratedproblem.repository;

import com.app.domain.problem.aigeneratedproblem.entity.AiGeneratedProblem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AiGeneratedProblemRepository extends JpaRepository<AiGeneratedProblem, Integer> {

    List<AiGeneratedProblem> findByProblemFile_FileId(int fileId);
}
