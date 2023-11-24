package com.app.domain.summary.repository;

import com.app.domain.problem.entity.ProblemFiles;
import com.app.domain.summary.entity.SummaryFiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SummaryFilesRepository extends JpaRepository<SummaryFiles,Integer> {


    Optional<SummaryFiles> findByFileName(String FileName);
}
