package com.app.domain.file.problem.repository;

import com.app.domain.file.problem.entity.ProblemFiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProblemFilesRepository extends JpaRepository<ProblemFiles, Integer> {

    ProblemFiles findByMemberIdAndFileName(String MemberId, String FileName);

}
