package com.practice.work.films.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.practice.work.films.entities.Film;
import com.practice.work.films.service.FilmsService;
import com.practice.work.films.validation.Violation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import static com.practice.work.films.constants.TestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {"spring.data.mongodb.database=embeddedDB", "logging.level.root=OFF"})
@AutoConfigureMockMvc
class FindFilmsByActorControllerTest {

    @MockBean
    private FilmsService filmsService;

    @Autowired
    private MockMvc mockMvc;

    private static List<Film> TEST_FILM_1 = new ArrayList<>();

    @BeforeAll
    static void setup() throws IOException {
        List<Film> testFilms = OBJECT_MAPPER.readValue(TEST_JSON, new TypeReference<>() {
        });
        TEST_FILM_1.add(testFilms.get(0));
    }

    @Test
    void testFetchFilmsByActor_CorrectValues() throws Exception {
        doReturn(Optional.of(TEST_FILM_1)).when(filmsService).fetchFilmsByActor(TEST_ACTORS.get(0));

        this.mockMvc.perform(get("/v1/findFilmsByActor")
                .param("actor", TEST_ACTORS.get(0)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(header().string(HttpHeaders.LOCATION, "/v1/findFilmsByActor"))
                .andExpect(jsonPath("[0]['title']").value("test title one"))
                .andExpect(jsonPath("[0]['director']").value("test director one"))
                .andExpect(jsonPath("[0]['cinematographer']").value("test cinematographer one"))
                .andExpect(jsonPath("[0]['composer']").value("test composer one"))
                .andExpect(jsonPath("[0]['writer']").value("test writer one"))
                .andExpect(jsonPath("[0]['genre']").value("test genre one"))
                .andExpect(jsonPath("[0]['releaseDate']").value(LocalDate.of(2000, 1, 31).toString()))
                .andExpect(jsonPath("[0]['releaseDate']", isA(String.class)))
                .andExpect(jsonPath("[0]['actors']", isA(List.class)))
                .andExpect(jsonPath("[0]['actors'][0]").value("test actor one"))
                .andExpect(jsonPath("[0]['actors'][1]").value("test actor two"))
                .andExpect(jsonPath("[0]['actors'].length()", is(2)));
    }

    @Test
    void testActorNotStoredReturns_NotFoundResponse() throws Exception {
        doReturn(Optional.empty()).when(filmsService).fetchFilmsByActor("non-existent actor");

        this.mockMvc.perform(get("/v1/findFilmsByActor")
                .param("actor", "non-existent actor"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testNameNotMatchingRegex_ReturnsViolation() throws Exception {
        String response = this.mockMvc.perform(get("/v1/findFilmsByActor")
                .param("actor", "!!!"))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        try {
            Set<Violation> violations = OBJECT_MAPPER.readValue(response, new TypeReference<HashSet<Violation>>() {
            });

            violations.forEach(v -> {
                assertThat(v.getMessage()).isEqualTo("must match \"[a-zA-Z,.'\\-\\s]+\"");
                assertThat(v.getField()).isEqualTo("actor");
            });

        } catch (JsonProcessingException ex) {
            fail("Exception when processing JSON.", ex.getCause());
        }
    }

    @Test
    void testNameBlank_ReturnsViolation() throws Exception {
        String response = this.mockMvc.perform(get("/v1/findFilmsByActor")
                .param("actor", ""))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        try {
            Set<Violation> violations = OBJECT_MAPPER.readValue(response, new TypeReference<HashSet<Violation>>() {
            });

            violations.forEach(v -> {
                assertThat(v.getMessage()).isEqualTo("must match \"[a-zA-Z,.'\\-\\s]+\"");
                assertThat(v.getField()).isEqualTo("actor");
            });

        } catch (JsonProcessingException ex) {
            fail("Exception when processing JSON.", ex.getCause());
        }
    }
}