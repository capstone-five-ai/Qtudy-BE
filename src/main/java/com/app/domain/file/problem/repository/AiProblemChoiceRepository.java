package com.app.domain.file.problem.repository;

import com.app.domain.file.problem.entity.AiProblemChoice;
import com.app.domain.file.problem.entity.ProblemFiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AiProblemChoiceRepository extends JpaRepository<AiProblemChoice, Integer> {

}
