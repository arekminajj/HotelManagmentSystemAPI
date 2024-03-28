package com.HotelManagmentSystem.HotelManagmentSystemAPI.controller;

import com.HotelManagmentSystem.HotelManagmentSystemAPI.exceptions.BadRequestException;
import com.HotelManagmentSystem.HotelManagmentSystemAPI.exceptions.NotFoundException;
import com.HotelManagmentSystem.HotelManagmentSystemAPI.model.Term;
import com.HotelManagmentSystem.HotelManagmentSystemAPI.model.Booking;
import com.HotelManagmentSystem.HotelManagmentSystemAPI.repository.BookingRepository;
import com.HotelManagmentSystem.HotelManagmentSystemAPI.repository.RoomRepository;
import com.HotelManagmentSystem.HotelManagmentSystemAPI.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path="/room")
public class RoomController {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @GetMapping(path="/all")
    public ResponseEntity<Iterable<Room>> getAlRooms() {
        Iterable<Room> rooms = roomRepository.findAll();
        return new ResponseEntity<Iterable<Room>>(rooms, HttpStatus.OK);
    }

    @GetMapping(path="/{id}")
    public ResponseEntity<Room> GetRoomById(@PathVariable int id) {
        Optional<Room> roomOptional = roomRepository.findById(id);

        if (roomOptional.isPresent()) {
            return ResponseEntity.ok(roomOptional.get());
        } else {
            throw new NotFoundException("Room with the id of: " + id + "does not exist.");
        }
    }

    @GetMapping(path="/booked/{id}")
    public ResponseEntity<List<Term>>GetRoomBookedTerms(@PathVariable int id) {
        List<Booking> bookings = bookingRepository.findByRoomId(id);
        List<Term> bookedTerms = new ArrayList<Term>();

        for (Booking booking : bookings) {
            Term term = new Term(booking.getRoomId(), booking.getCheckInDate(), booking.getCheckOutDate());
            bookedTerms.add(term);
        }

        //Moglbym dodac excepcje dla nieistniejacego pokoju ale spowololni to dosc program

        return ResponseEntity.status(HttpStatus.OK).body(bookedTerms);
    }

    @GetMapping(path="free")
    public ResponseEntity<List<Room>>getFreeRoomsForTerm(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        Iterable<Room> rooms = roomRepository.findAll();
        List<Room> freeRooms = new ArrayList<Room>();

        for(Room room : rooms) {
                List<Booking> roomBookings = bookingRepository.findByRoomId(room.getId());
                boolean canBeAdded = true;
                for(Booking booking : roomBookings) {
                    if(checkDateOverlap(startDate, endDate, booking.getCheckInDate(), booking.getCheckOutDate())) {
                        canBeAdded = false;
                    }
                    
                }
                if(canBeAdded) {
                    freeRooms.add(room);
                }
        }

        return ResponseEntity.status(HttpStatus.OK).body(freeRooms);
    }

    @PostMapping(path="/add") // Map ONLY POST Requests
    public ResponseEntity<Room> addNewRoom(@RequestBody Room room) {
        Room savedRoom = roomRepository.save(room);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRoom);
    }

    @PutMapping(path="/{id}")
    public ResponseEntity<Room> updateRoom(@RequestBody Room updatedRoom,@PathVariable int id)
    {
        Optional<Room> existingRoomOptional = roomRepository.findById(id);

        if (existingRoomOptional.isPresent()) {
            Room existingRoom = existingRoomOptional.get();;

            existingRoom.setDescription(updatedRoom.getDescription());
            existingRoom.setCapacity(updatedRoom.getCapacity());
            existingRoom.setPrice(updatedRoom.getPrice());
            existingRoom.setFloor(updatedRoom.getFloor());
            existingRoom.setPhotos(updatedRoom.getPhotos());

            Room savedRoom = roomRepository.save(existingRoom);
            return ResponseEntity.ok(savedRoom);
        } else {
            throw new BadRequestException("Requested room does not exist");
        }
    }

    @DeleteMapping(path="/{id}")
    public ResponseEntity<String> deleteRoom(@PathVariable int id) {
        Optional<Room> roomOptional = roomRepository.findById(id);
        if(roomOptional.isPresent()) {
            roomRepository.deleteById(id);
            return ResponseEntity.ok("Room of id: " + id + " has been deleted");
        }
        else {
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
}
