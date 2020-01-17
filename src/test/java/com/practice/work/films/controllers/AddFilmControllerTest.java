package com.practice.work.films.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.jayway.jsonpath.JsonPath;
import com.practice.work.films.dtos.FilmDTO;
import com.practice.work.films.entities.Film;
import com.practice.work.films.service.FilmsService;
import com.practice.work.films.validation.Violation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.practice.work.films.constants.TestConstants.*;
import static com.practice.work.films.controllers.AddMultipleFilmsTest.FILM_DTO_AS_STRING;
import static com.practice.work.films.controllers.AddMultipleFilmsTest.INVALID_TEST_FILM_DTO_AS_STRING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AddFilmControllerTest {

    @MockBean
    private FilmsService filmsService;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private AddFilmController addFilmController;

    @MockBean
    private ModelMapper mockModelMapper;

    @BeforeAll
    static void stringSetup() throws IOException {
        INVALID_TEST_FILM_DTO_AS_STRING = OBJECT_MAPPER.writeValueAsString(INVALID_TEST_FILM_DTO);
    }

    @Test
    void testInsertFilmDocument_CorrectValues() throws Exception {
        doReturn(Optional.of(TEST_FILM)).when(filmsService).insertSingleFilmDocument(TEST_FILM);
        doReturn(TEST_FILM).when(mockModelMapper).map(TEST_FILM_DTO, Film.class);
        doReturn(TEST_FILM_DTO).when(mockModelMapper).map(TEST_FILM, FilmDTO.class);

        String response = this.mockMvc.perform(post("/v1/addFilm/")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(FILM_DTO_AS_STRING))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath(".id").value(RANDOM_UUID))
                .andExpect(jsonPath(".title").value(TEST_TITLE))
                .andExpect(jsonPath(".genre").value(TEST_GENRE))
                .andExpect(jsonPath(".composer").value(TEST_COMPOSER))
                .andExpect(jsonPath(".writer").value(TEST_WRITER))
                .andExpect(jsonPath(".cinematographer").value(TEST_CINEMATOGRAPHER))
                .andExpect(jsonPath(".releaseDate").value(TEST_RELEASE_DATE.toString()))
                .andExpect(jsonPath(".dateAdded").value(TEST_DATE_ADDED.toString()))
                .andExpect(jsonPath(".actors[0]").value(TEST_ACTORS.get(0)))
                .andExpect(jsonPath(".actors[1]").value(TEST_ACTORS.get(1)))
                .andExpect(jsonPath(".director").value(TEST_DIRECTOR))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Map<String, String> object = JsonPath.read(response, "$");
        assertThat(object.keySet().size()).isEqualTo(10);
    }

    /**
     * If, for whatever reason, the DB encounters an error and returns no film object
     * then the add film endpoint will return an unprocessable entity response
     *
     * @throws Exception MockMvc's perform throws generic Exception
     */
    @Test
    void testInsertFilmDocument_EmptyFilmReturnsUnprocessableEntity() throws Exception {
        doReturn(Optional.empty()).when(filmsService).insertSingleFilmDocument(TEST_FILM);

        this.mockMvc.perform(post("/v1/addFilm/")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(FILM_DTO_AS_STRING))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void testFilmDTO_BlankField_ReturnsViolationWithDetails() throws Exception {
        String response = this.mockMvc.perform(post("/v1/addFilm/")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(INVALID_TEST_FILM_DTO_AS_STRING))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        try {
            Set<Violation> violations = OBJECT_MAPPER.readValue(response, new TypeReference<HashSet<Violation>>() {
            });

            for (Violation violation : violations) {
                assertThat(violation.getMessage()).isEqualTo("must not be blank");
            }
        } catch (JsonProcessingException ex) {
            fail("Exception when processing JSON.");
        }
    }
}
