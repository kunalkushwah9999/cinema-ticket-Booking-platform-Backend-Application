package com.project.BookMyShowApp.repository;

import com.project.BookMyShowApp.model.Booking;
import com.project.BookMyShowApp.model.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {

    List<Show> findByMovieId(Long movieId);


    List<Show> findByMovie_IdAndScreen_Theater_City(Long movieId, String city);

    //List<Show> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT s FROM Show s JOIN s.screen sc JOIN sc.theater t WHERE t.city = :city AND s.startTime >= :startDate AND s.startTime < :endDate")
    List<Show> findShowsByCityAndDate(
            @Param("city") String city,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    List<Show> findByScreenId(Long screenId);


}