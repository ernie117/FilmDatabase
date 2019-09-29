package com.practice.work.films.controllers;

import com.practice.work.films.configuration.ConfigProperties;
import com.practice.work.films.service.FilmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;


@RestController
@ResponseStatus(HttpStatus.OK)
@Api(tags = {"Fetch Films by Exact Date"})
public class FindFilmsByReleaseDateController {

    private FilmsService filmsService;
    private ConfigProperties configProperties;

    @Autowired
    FindFilmsByReleaseDateController(FilmsService filmsService, ConfigProperties configProperties) {
        this.filmsService = filmsService;
        this.configProperties = configProperties;
    }

    @GetMapping(path = "/v1/findAllByYear")
    public ResponseEntity<?> fetchAllFilmsByReleaseDate(@ApiParam("Year to search, as string")
                                                 @RequestParam
                                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate localDate) {
    return this.filmsService.findFilmsByReleaseDate(localDate)
            .map(films -> {
                try {
                    return ResponseEntity
                            .ok()
                            .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_UTF8_VALUE))
                            .location(new URI(configProperties.getFindFilmsByYearURI()))
                            .body(films);
                } catch (URISyntaxException use) {
                    return ResponseEntity.badRequest().build();
                }
            }).orElse(ResponseEntity.notFound().build());
    }

}