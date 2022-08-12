package com.ius.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface StudentRepository extends JpaRepository<Student, Integer> {

    Optional<Student> getStudentByEmail(String email);

    Optional<Student> getStudentByStudentID(Integer studentID);
}
