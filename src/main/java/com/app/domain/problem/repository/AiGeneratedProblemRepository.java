package com.app.domain.problem.repository;

import com.app.domain.problem.entity.AiGeneratedProblem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AiGeneratedProblemRepository extends JpaRepository<AiGeneratedProblem, Integer> {

    List<AiGeneratedProblem> findByProblemFiles_FileId(int fileId);
}
