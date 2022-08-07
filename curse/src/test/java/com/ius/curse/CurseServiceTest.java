package com.ius.curse;

import com.ius.curse.request.CurseAddRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CurseServiceTest {

    @Mock
    private CurseRepository curseRepository;
    private CurseService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CurseService(curseRepository);
    }

    @Test
    void canGetAllCurses() {
        //when
        underTest.getCurses();

        //then
        verify(curseRepository).findAll();
    }

    @Test
    void canAddCurse() {
        //given
        String curseName = "Curse_Name";
        CurseAddRequest curseAddRequest = new CurseAddRequest(curseName);

        //when
        underTest.addCurse(curseAddRequest);

        //then
        ArgumentCaptor<Curse> curseArgumentCaptor = ArgumentCaptor.forClass(Curse.class);

        verify(curseRepository).save(curseArgumentCaptor.capture());

        Curse capturedCurse = curseArgumentCaptor.getValue();

        assertThat(capturedCurse.getName()).isEqualTo(curseName);
    }

    @Test
    void willThrowWhenCurseNameIsTaken() {
        //given
        String curseName = "Curse_Name";
        Curse curse = Curse.builder()
                .name(curseName)
                .build();
        Optional<Curse> optionalCurse = Optional.of(curse);
        CurseAddRequest curseAddRequest = new CurseAddRequest(curseName);

        given(curseRepository.findCurseByName(curseName)).willReturn(optionalCurse);

        //when
        //then
        assertThatThrownBy(() -> underTest.addCurse(curseAddRequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Curse called " + curseName + " was already added");

        verify(curseRepository, never()).save(any());
    }

    @Test
    void canGetCurseByName() {
        //given
        String curseName = "Curse_Name";

        //when
        underTest.getCurseByName(curseName);

        //then
        verify(curseRepository).findCurseByName(curseName);
    }
}