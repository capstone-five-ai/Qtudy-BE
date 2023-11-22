package com.app.domain.file.summary.repository;

import com.app.domain.file.summary.entity.SummaryFiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SummaryFilesRepository extends JpaRepository<SummaryFiles,Integer> {
}
