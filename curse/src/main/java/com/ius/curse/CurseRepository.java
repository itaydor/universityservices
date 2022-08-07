package com.ius.curse;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurseRepository extends JpaRepository<Curse, Integer> {

    Optional<Curse> findCurseByName(String curseName);

}
