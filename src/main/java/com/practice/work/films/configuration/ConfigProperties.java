package com.practice.work.films.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
@ConfigurationProperties("films")
@Getter
@Setter
public class ConfigProperties {

    private String findFilmsByActorURI;
    private String findAllFilmsURI;
    private String findFilmsByYearURI;
    private String findFilmsByGenreURI;
    private String findFilmsByDirectorURI;
    private String findFilmsByComposerURI;
    private String findFilmsByCinematographerURI;
    private String findFilmByTitleURI;
    private String getFilmIdURI;

}
