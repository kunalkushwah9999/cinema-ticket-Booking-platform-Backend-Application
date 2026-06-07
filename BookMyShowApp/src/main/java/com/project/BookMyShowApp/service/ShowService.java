package com.project.BookMyShowApp.service;

import com.project.BookMyShowApp.dto.*;
import com.project.BookMyShowApp.exception.ResourceNotFoundException;
import com.project.BookMyShowApp.model.Movie;
import com.project.BookMyShowApp.model.Screen;
import com.project.BookMyShowApp.model.Show;
import com.project.BookMyShowApp.model.ShowSeat;
import com.project.BookMyShowApp.repository.MovieRepository;
import com.project.BookMyShowApp.repository.ScreenRepository;
import com.project.BookMyShowApp.repository.ShowRepository;
import com.project.BookMyShowApp.repository.ShowSeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShowService {

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ScreenRepository screenRepository;

    @Autowired
    private ShowSeatRepository showSeatRepository;


    public ShowDto createShow(ShowDto showDto){
        Movie movie = movieRepository.findById(showDto.getMovie().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie Not Found"));

        Screen screen = screenRepository.findById(showDto.getScreen().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Screen Not Found"));

        Show show = new Show();
        show.setStartTime(showDto.getStartTime());
        show.setEndTime(showDto.getEndTime());
        show.setMovie(movie);
        show.setScreen(screen);

        Show savedShow = showRepository.save(show);
        List<ShowSeat> availableSeats = showSeatRepository.findByShowIdAndStatus(savedShow.getId(), "AVAILABLE");

        return mapToDto(savedShow, availableSeats);
    }

    public ShowDto getShowById(Long id){
        Show show = showRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Show Not Found"));
        List<ShowSeat> availableSeats = showSeatRepository.findByShowIdAndStatus(show.getId(), "AVAILABLE");
        return mapToDto(show, availableSeats);
    }

    public List<ShowDto> getAllShows(){
        List<Show> shows = showRepository.findAll();
        return shows.stream()
                .map(show -> {
                    List<ShowSeat> seats = showSeatRepository.findByShowIdAndStatus(show.getId(), "AVAILABLE");
                    return mapToDto(show, seats);
                })
                .collect(Collectors.toList());

    }

    public List<ShowDto> getShowsByMovie(Long movieId){
        List<Show> shows = showRepository.findByMovieId(movieId);
        return shows.stream()
                .map(show -> {
                    List<ShowSeat> seats = showSeatRepository.findByShowIdAndStatus(show.getId(), "AVAILABLE");
                    return mapToDto(show, seats);
                })
                .collect(Collectors.toList());
    }

    public List<ShowDto> getShowsByMovieAndCity(Long movieId, String city){
        List<Show> shows = showRepository.findByMovie_IdAndScreen_Theater_City(movieId, city);
        return shows.stream()
                .map(show -> {
                    List<ShowSeat> seats = showSeatRepository.findByShowIdAndStatus(show.getId(), "AVAILABLE");
                    return mapToDto(show, seats);
                })
                .collect(Collectors.toList());
    }

    public void deleteShow(Long id) {
        Show show = showRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Show Not Found"));
        showRepository.delete(show);

    }

    public List<ShowDto> getShowsByCityAndDate(String city, LocalDate date){
        LocalDateTime startDate = date.atStartOfDay();
        LocalDateTime endDate = date.plusDays(1).atStartOfDay();
        List<Show> shows = showRepository.findShowsByCityAndDate(city, startDate, endDate);
        return shows.stream()
                .map(show -> {
                    List<ShowSeat> seats = showSeatRepository.findByShowIdAndStatus(show.getId(), "AVAILABLE");
                    return mapToDto(show, seats);
                })
                .collect(Collectors.toList());
    }

    public ShowDto mapToDto(Show show, List<ShowSeat> seats){
        ShowDto showDto = new ShowDto();
        showDto.setId(show.getId());
        showDto.setStartTime(show.getStartTime());
        showDto.setEndTime(show.getEndTime());

        showDto.setMovie(new MovieDto(
                show.getMovie().getId(),
                show.getMovie().getTitle(),
                show.getMovie().getDescription(),
                show.getMovie().getLanguage(),
                show.getMovie().getGenre(),
                show.getMovie().getDurationMins(),
                show.getMovie().getReleaseDate(),
                show.getMovie().getPosterUrl()
        ));

        TheaterDto theaterDto = new TheaterDto();
        theaterDto.setId(show.getScreen().getTheater().getId());
        theaterDto.setName(show.getScreen().getTheater().getName());
        theaterDto.setCity(show.getScreen().getTheater().getCity());
        theaterDto.setAddress(show.getScreen().getTheater().getAddress());
        theaterDto.setTotalScreens(show.getScreen().getTheater().getTotalScreens());

        showDto.setScreen(new ScreenDto(
                show.getScreen().getId(),
                show.getScreen().getName(),
                show.getScreen().getTotalSeats(),
                theaterDto
        ));

        List<ShowSeatDto> seatDtos= seats.stream()
                .map(seat->{
                    ShowSeatDto seatDto=new ShowSeatDto();
                    seatDto.setId(seat.getId());
                    seatDto.setStatus(seat.getStatus());
                    seatDto.setPrice(seat.getPrice());

                    SeatDto baseSeatDto=new SeatDto();
                    baseSeatDto.setId(seat.getSeat().getId());
                    baseSeatDto.setSeatNumber(seat.getSeat().getSeatNumber());
                    baseSeatDto.setSeatType(seat.getSeat().getSeatType());
                    baseSeatDto.setBasePrice(seat.getSeat().getBasePrice());
                    seatDto.setSeat(baseSeatDto);
                    return seatDto;
                })
                .collect(Collectors.toList());

        showDto.setAvailableSeats(seatDtos);
        return showDto;
    }


    public ShowDto updateShow(Long id, ShowDto showDto) {
        Movie movie = movieRepository.findById(showDto.getMovie().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie Not Found"));

        Screen screen = screenRepository.findById(showDto.getScreen().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Screen Not Found"));

        Show show = showRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Show Not Found"));

        show.setStartTime(showDto.getStartTime());
        show.setEndTime(showDto.getEndTime());
        show.setMovie(movie);
        show.setScreen(screen);

        Show savedShow = showRepository.save(show);
        List<ShowSeat> availableSeats = showSeatRepository.findByShowIdAndStatus(savedShow.getId(), "AVAILABLE");

        return mapToDto(savedShow, availableSeats);
    }


}