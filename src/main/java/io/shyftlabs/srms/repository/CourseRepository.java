package io.shyftlabs.srms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.shyftlabs.srms.domain.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {

	boolean existsByName(String name);

}
