package com.ius.student.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface StudentRepository extends JpaRepository<Student, Integer> {

    Optional<Student> getStudentByEmail(String email);

    Optional<Student> getStudentByStudentID(Integer studentID);

    @Transactional
    @Modifying
    @Query("UPDATE Student s " +
            "SET s.enabled = TRUE " +
            "WHERE s.email = ?1 ")
    int enableStudent(String email);

    @Transactional
    @Modifying
    @Query("UPDATE Student s " +
            "SET s.locked = ?2 " +
            "WHERE s.email = ?1 ")
    int setLocked(String email, boolean isLocked);

}
