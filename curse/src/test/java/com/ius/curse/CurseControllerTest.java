package com.ius.curse;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ExtendWith(MockitoExtension.class)
class CurseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CurseService curseService;

    @Mock
    private CurseRepository curseRepository;

    @Test
    @Disabled
    void shouldGetAllCurses() throws Exception {
        //given
        String curseName = "Curse_Name";
        Curse curse = Curse.builder()
                .name(curseName)
                .build();
        when(curseService.getCurses())
                .thenReturn(Collections.singletonList(curse));

        mockMvc.perform(get(""))
                .andExpect(status().isOk());
    }

    @Test
    @Disabled
    void shouldAddCurse() {
    }

    @Test
    @Disabled
    void shouldGetCurseByName() {
    }
}