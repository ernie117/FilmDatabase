package com.practice.work.films.controllers;

import com.practice.work.films.entities.Film;
import com.practice.work.films.repositories.FilmRepository;
import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@ResponseStatus(HttpStatus.CREATED)
@Api(value = "Add Film Endpoint", description = "REST Endpoint for adding films")
public class AddFilmController {

    private FilmRepository filmRepository;

    @Autowired
    AddFilmController(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    @RequestMapping(value = "/addFilm", method = RequestMethod.POST)
    public Film create(@Valid @RequestBody Film film) {
        return filmRepository.save(film);
    }
}
