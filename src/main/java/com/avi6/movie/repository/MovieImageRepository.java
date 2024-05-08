package com.avi6.movie.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avi6.movie.entity.MovieImage;

public interface MovieImageRepository extends JpaRepository<MovieImage, Long> {

}
