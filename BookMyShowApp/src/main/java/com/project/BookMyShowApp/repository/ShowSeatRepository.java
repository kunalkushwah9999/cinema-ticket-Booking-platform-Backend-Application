package com.project.BookMyShowApp.repository;

import com.project.BookMyShowApp.model.Booking;
import com.project.BookMyShowApp.model.ShowSeat;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowSeatRepository extends JpaRepository<ShowSeat, Long> {

    List<ShowSeat> findByShowId(Long id);

    List<ShowSeat> findByShowIdAndStatus(Long id, String status);

    List<ShowSeat> findByBookingId(Long id);



    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ss FROM ShowSeat ss WHERE ss.id IN :ids")
    List<ShowSeat> findAllByIdWithLock(@Param("ids") List<Long> ids);
}
