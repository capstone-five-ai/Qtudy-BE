package com.app.domain.problem.repository;

import com.app.domain.problem.entity.ProblemFiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProblemFilesRepository extends JpaRepository<ProblemFiles, Integer> {

    ProblemFiles findByMemberIdAndFileName(String MemberId, String FileName);

    Optional<ProblemFiles> findByFileName(String FileName);

}
