package com.ius.curse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class CurseRepositoryTest {

    @Autowired
    private CurseRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void checkIfCurseNameExists() {
        //given
        String name = "Curse_Name";
        Curse curse = Curse.builder()
                .name(name)
                .build();
        underTest.save(curse);

        //when
        Optional<Curse> curseByName = underTest.findCurseByName(name);

        //then
        assertThat(curseByName.isPresent()).isTrue();
    }

    @Test
    void checkIfCurseNameDoesNotExists() {
        //given
        String name = "Curse_Name";

        //when
        Optional<Curse> curseByName = underTest.findCurseByName(name);

        //then
        assertThat(curseByName.isEmpty()).isTrue();
    }
}