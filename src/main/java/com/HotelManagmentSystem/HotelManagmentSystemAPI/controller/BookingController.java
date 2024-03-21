package com.HotelManagmentSystem.HotelManagmentSystemAPI.controller;

import com.HotelManagmentSystem.HotelManagmentSystemAPI.exceptions.BadRequestException;
import com.HotelManagmentSystem.HotelManagmentSystemAPI.exceptions.NotFoundException;
import com.HotelManagmentSystem.HotelManagmentSystemAPI.model.Booking;
import com.HotelManagmentSystem.HotelManagmentSystemAPI.model.Room;
import com.HotelManagmentSystem.HotelManagmentSystemAPI.repository.BookingRepository;
import com.HotelManagmentSystem.HotelManagmentSystemAPI.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path="/booking")
public class BookingController {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private RoomRepository roomRepository;

    @GetMapping(path="/all")
    public ResponseEntity<Iterable<Booking>> getAlBookings() {
        Iterable<Booking> bookings = bookingRepository.findAll();
        return new ResponseEntity<Iterable<Booking>>(bookings, HttpStatus.OK);
    }


    //TODO: make it return message when throwing exception.
    //TODO: need two exception messages a) room doesnt exit b) room already booked for the date.
    @PostMapping(path="/add")
    public ResponseEntity<Booking> addNewBooking(@RequestBody Booking booking) {
        int requestedRoomId = booking.getRoomId();
        Optional<Room> requestedRoomOptional = roomRepository.findById(requestedRoomId);
        List<Booking> bookings = bookingRepository.findByRoomId(requestedRoomId);
        //sprawdza czy w ogole jest pokoj o takim id w hotelu
        if (requestedRoomOptional.isPresent()) {
            for (Booking iterableBooking : bookings) {
                // sprawdza czy pokoj nie jest zajety
                if(checkDateOverlap(
                        booking.getCheckInDate(),
                        booking.getCheckOutDate(),
                        iterableBooking.getCheckInDate(),
                        iterableBooking.getCheckOutDate())) {
                    throw new BadRequestException();
                }
            }

            Booking savedBooking = bookingRepository.save(booking);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedBooking);
        }
        else {
        throw new BadRequestException();
        }
        //todo: make sure room isnt already booked for the time specified in the req;
        //todo: also throw exception if room doesnt exist

    }

    @GetMapping(path="/{id}")
    public ResponseEntity<Booking> GetBookingById(@PathVariable int id) {
        Optional<Booking> bookingOptional = bookingRepository.findById(id);

        if (bookingOptional.isPresent()) {
            return ResponseEntity.ok(bookingOptional.get());
        } else {
            throw new NotFoundException();
        }
    }

    @PutMapping(path="/{id}")
    public ResponseEntity<Booking> updateBooking(@RequestBody Booking updatedBooking,@PathVariable int id)
    {
        Optional<Booking> existingBookingOptional = bookingRepository.findById(id);

        if (existingBookingOptional.isPresent()) {
            Booking existingBooking = existingBookingOptional.get();;

            existingBooking.setCheckInDate(updatedBooking.getCheckInDate());
            existingBooking.setCheckOutDate(updatedBooking.getCheckOutDate());
            existingBooking.setGuest(updatedBooking.getGuest());
            existingBooking.setRoomId(updatedBooking.getRoomId());
            existingBooking.setNumberOfGuests(updatedBooking.getNumberOfGuests());

            //todo: make sure room isnt already booked for the time specified in the req;
            //todo: also throw exception if room doesnt exist

            Booking savedBooking = bookingRepository.save(existingBooking);
            return ResponseEntity.ok(savedBooking);
        } else {
            throw new BadRequestException();
        }
    }

    @DeleteMapping(path="/{id}")
    public ResponseEntity<String> deleteBooking(@PathVariable int id) {
        Optional<Booking> bookingOptional = bookingRepository.findById(id);
        if(bookingOptional.isPresent()) {
            bookingRepository.deleteById(id);
            return ResponseEntity.ok("Booking of id: " + id + " has been deleted");
        }
        else {
            throw new BadRequestException();
        }
    }

    public static boolean checkDateOverlap(LocalDate startDate1, LocalDate endDate1, LocalDate startDate2, LocalDate endDate2) {
        if (endDate1.isBefore(startDate2) || startDate1.isAfter(endDate2)) {
            return false;
        } else {
            return true;
        }
    }
}
