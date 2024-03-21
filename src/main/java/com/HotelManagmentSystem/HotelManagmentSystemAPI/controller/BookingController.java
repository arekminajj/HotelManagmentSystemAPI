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

    @GetMapping(path = "/all")
    public ResponseEntity<Iterable<Booking>> getAlBookings() {
        Iterable<Booking> bookings = bookingRepository.findAll();
        return new ResponseEntity<Iterable<Booking>>(bookings, HttpStatus.OK);
    }

    @PostMapping(path = "/add")
    public ResponseEntity<Booking> addNewBooking(@RequestBody Booking booking) {
        int requestedRoomId = booking.getRoomId();
        Optional<Room> requestedRoomOptional = roomRepository.findById(requestedRoomId);
        List<Booking> bookings = bookingRepository.findByRoomId(requestedRoomId);

        if (doesRoomExist(requestedRoomOptional)) {
            if (canBeBooked(bookings, booking)) {
                Booking savedBooking = bookingRepository.save(booking);
                return ResponseEntity.status(HttpStatus.CREATED).body(savedBooking);
            }
            else{
                throw new BadRequestException("Room is alreadu booked for the date");
            }
        }
        else {
            throw new BadRequestException("Room does not exits.");
        }
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Booking> GetBookingById(@PathVariable int id) {
        Optional<Booking> bookingOptional = bookingRepository.findById(id);

        if (bookingOptional.isPresent()) {
            return ResponseEntity.ok(bookingOptional.get());
        } else {
            throw new NotFoundException("Booking with the id of: " + id + "does not exist.");
        }
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Booking> updateBooking(@RequestBody Booking updatedBooking, @PathVariable int id) {
        Optional<Booking> existingBookingOptional = bookingRepository.findById(id);
        int requestedRoomId = updatedBooking.getRoomId();
        Optional<Room> requestedRoomOptional = roomRepository.findById(requestedRoomId);
        List<Booking> bookings = bookingRepository.findByRoomId(requestedRoomId);

        if(doesRoomExist(requestedRoomOptional)) {
            if(canBeBooked(bookings, updatedBooking)) {
                Booking existingBooking = existingBookingOptional.get();

                existingBooking.setCheckInDate(updatedBooking.getCheckInDate());
                existingBooking.setCheckOutDate(updatedBooking.getCheckOutDate());
                existingBooking.setGuest(updatedBooking.getGuest());
                existingBooking.setRoomId(updatedBooking.getRoomId());
                existingBooking.setNumberOfGuests(updatedBooking.getNumberOfGuests());

                Booking savedBooking = bookingRepository.save(existingBooking);
                return ResponseEntity.ok(savedBooking);
            }
            else {
                throw new BadRequestException("Room is alreadu booked for the date");
            }
        }
        else {
            throw new BadRequestException("Room does not exist");
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteBooking(@PathVariable int id) {
        Optional<Booking> bookingOptional = bookingRepository.findById(id);
        if (bookingOptional.isPresent()) {
            bookingRepository.deleteById(id);
            return ResponseEntity.ok("Booking of id: " + id + " has been deleted");
        } else {
            throw new BadRequestException("Requested room does not exist.");
        }
    }

    private static boolean checkDateOverlap(LocalDate startDate1, LocalDate endDate1, LocalDate startDate2, LocalDate endDate2) {
        if (endDate1.isBefore(startDate2) || startDate1.isAfter(endDate2)) {
            return false;
        } else {
            return true;
        }
    }

    private boolean doesRoomExist(Optional<Room> requestedRoomOptional) {
        return requestedRoomOptional.isPresent();
    }

    private boolean canBeBooked(List<Booking> bookings, Booking booking) {
        for (Booking iterableBooking : bookings) {
            // sprawdza czy pokoj nie jest zajety
            if (checkDateOverlap(
                    booking.getCheckInDate(),
                    booking.getCheckOutDate(),
                    iterableBooking.getCheckInDate(),
                    iterableBooking.getCheckOutDate())) {
                return false;
            }
        }
        return true;
    }
}
