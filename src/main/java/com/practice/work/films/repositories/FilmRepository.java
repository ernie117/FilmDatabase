package com.practice.work.films.repositories;

import com.practice.work.films.entities.Film;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FilmRepository extends MongoRepository<Film, String> {

    // save/saveAll methods exist as default methods

    Film findFilmById(String id);

    @Query("{'title': {$regex: ?0, $options: i}}")
    List<Film> findFilmByTitleRegexIgnoreCase(String title);

    @Query("{'director': {$regex: ?0, $options: i}}")
    List<Film> findAllByDirectorRegexIgnoreCase(String director);

    @Query("{'genre': {$regex: ?0, $options: i}}")
    List<Film> findFilmsByGenreRegexIgnoreCase(String genre);

    List<Film> findAllByReleaseDate(LocalDate date);

    @Query("{'releaseDate': {$gt: ?0, $lt: ?1}}")
    List<Film> findAllFilmsByYear(LocalDate from, LocalDate to);

    List<Film> findAll();

    @Query("{'actors': {$regex: ?0, $options: i}}")
    List<Film> findFilmsByActorsRegexIgnoreCase(String actor);

    @Query("{'composer': {$regex: ?0, $options: i}}")
    List<Film> findFilmsByComposerRegexIgnoreCase(String composer);

    @Query("{'cinematographer': {$regex: ?0, $options: i}}")
    List<Film> findFilmsByCinematographerRegexIgnoreCase(String cinematographer);

    @Query("{'writer': {$regex: ?0, $options: i}}")
    List<Film> findFilmsByWriterRegexIgnoreCase(String writer);

    void deleteFilmById(String id);
}
