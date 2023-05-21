package io.shyftlabs.srms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.shyftlabs.srms.domain.Result;

public interface ResultRepository extends JpaRepository<Result, Long> {

	boolean existsByCourseIdAndStudentId(Long courseId, Long studentId);

}
