package com.practice.work.films.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.work.films.service.FilmsService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static com.practice.work.films.constants.TestConstants.TEST_TITLE;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class FilmIdControllerTest {

    @MockBean
    private FilmsService filmsService;

    @Autowired
    private MockMvc mockMvc;

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static JsonNode mockResult;

    @BeforeAll
    static void setup() {
        mockResult = objectMapper
                .createArrayNode()
                .add(objectMapper.createObjectNode()
                        .put("test title", "1234567890"));
    }

    @Test
    @DisplayName("GET /v1/getFilmId")
    void testGetFilmIdByTitle() throws Exception {
        doReturn(Optional.of(mockResult)).when(filmsService).getFilmIdsByTitle(TEST_TITLE);

        this.mockMvc.perform(get("/v1/getFilmId")
                .param("title", TEST_TITLE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(header().string(HttpHeaders.LOCATION, "/v1/getFilmId"))
                .andExpect(jsonPath("$[0]['test title']").value("1234567890"));
    }

    @Test
    @DisplayName("GET /v1/getFilmId -- Not Found")
    void testGetFilmByIdNotFound() throws Exception {
        doReturn(Optional.empty()).when(filmsService).getFilmIdsByTitle("empty");

        this.mockMvc.perform(get("/v1/getFilmId")
                .param("title", "empty"))
                .andExpect(status().isNotFound());
    }
}

