package com.ius.curse;

import com.ius.clients.curse.CurseGetByNameResponse;
import com.ius.curse.request.CurseAddRequest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CurseController.class)
class CurseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurseService curseService;

    @MockBean
    private CurseRepository curseRepository;

    @Test
    void shouldGetAllCurses() throws Exception {
        //given
        String curseName = "Curse_Name";
        Curse curse = Curse.builder()
                .name(curseName)
                .build();
        when(curseService.getCurses())
                .thenReturn(Collections.singletonList(curse));

        //when
        //then
        mockMvc.perform(get("/api/v1/curse"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(curseName))
                );

        verify(curseService).getCurses();
    }

    @Test
    void shouldAddCurse() throws Exception {
        //given
        String curseName = "Curse_Name";
        CurseAddRequest curseAddRequest = new CurseAddRequest(curseName);

        //when
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/v1/curse")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("{\"curseName\": \"%s\"}", curseName))
                )
                .andExpect(status().isOk())
                .andDo(print());

        //then
        verify(curseService).addCurse(curseAddRequest);
    }

    @Test
    void shouldGetCurseByName() throws Exception {
        //given
        String curseName = "Curse_Name";
        int curseID = 1;
        boolean isExists = true;

        Curse curse = Curse.builder()
                .curseID(curseID)
                .name(curseName)
                .build();
        Optional<Curse> optionalCurse = Optional.of(curse);

        when(curseService.getCurseByName(curseName)).thenReturn(optionalCurse);

        //when
        mockMvc.perform(MockMvcRequestBuilders
                                .get("/api/v1/curse/" + curseName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.isExists", is(isExists)))
                .andExpect(jsonPath("$.curseID", is(curseID)))
                .andDo(print());

        //then
        verify(curseService).getCurseByName(curseName);
    }

    @Test
    void shouldReturnEmptyGetCurseByName() throws Exception {
        //given
        String curseName = "Curse_Name";
        int curseID = 0;
        boolean isExists = false;

        Optional<Curse> optionalCurse = Optional.empty();

        when(curseService.getCurseByName(curseName)).thenReturn(optionalCurse);

        //when
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/curse/" + curseName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.isExists", is(isExists)))
                .andExpect(jsonPath("$.curseID", is(curseID)))
                .andDo(print());

        //then
        verify(curseService).getCurseByName(curseName);
    }
}