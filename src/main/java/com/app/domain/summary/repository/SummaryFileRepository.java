package com.app.domain.summary.repository;

import com.app.domain.summary.entity.SummaryFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SummaryFileRepository extends JpaRepository<SummaryFile,Integer> {


    Optional<SummaryFile> findByFileName(String FileName);
}