package io.shyftlabs.srms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.shyftlabs.srms.domain.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {

	boolean existsByEmail(String email);

}
