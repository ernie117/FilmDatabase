package com.practice.work.films.controllers;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.practice.work.films.configuration.ConfigProperties;
import com.practice.work.films.service.FilmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@Api(tags = {"Fetch Film IDs by Title"})
public class FilmIdController {

    private final FilmsService filmsService;
    private final ConfigProperties configProperties;

    @Autowired
    FilmIdController(FilmsService filmsService, ConfigProperties configProperties) {
        this.filmsService = filmsService;
        this.configProperties = configProperties;
    }

    @GetMapping(value = "/v1/getFilmId")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ArrayNode> getFilmIdsByTitle(@ApiParam("Film title to search, as string")
                                                      @RequestParam String title) {
        return this.filmsService.getFilmIdsByTitle(title)
                .map(jsonNode -> ResponseEntity
                        .ok()
                        .location(URI.create(configProperties.getGetFilmIdURI()))
                        .body(jsonNode))
                .orElse(ResponseEntity.notFound().build());
    }
}
