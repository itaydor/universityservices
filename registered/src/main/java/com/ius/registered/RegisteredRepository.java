package com.ius.registered;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegisteredRepository extends JpaRepository<Registered, Integer> {

    Optional<Registered> getRegisteredByStudentIDAndCurseID(Integer studentID, Integer curseID);
}
