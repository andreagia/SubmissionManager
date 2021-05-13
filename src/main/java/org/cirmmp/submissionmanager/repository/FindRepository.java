package org.cirmmp.submissionmanager.repository;

import org.cirmmp.submissionmanager.model.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FindRepository extends JpaRepository<JobStatus, Long> {
    Optional<JobStatus> findByTag(String tag);
    List<JobStatus> findByStatus(String status);
}
