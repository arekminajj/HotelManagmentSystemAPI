package com.HotelManagmentSystem.HotelManagmentSystemAPI.controller;

import com.HotelManagmentSystem.HotelManagmentSystemAPI.exceptions.BadRequestException;
import com.HotelManagmentSystem.HotelManagmentSystemAPI.exceptions.NotFoundException;
import com.HotelManagmentSystem.HotelManagmentSystemAPI.repository.RoomRepository;
import com.HotelManagmentSystem.HotelManagmentSystemAPI.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path="/room")
public class RoomController {

    //private RoomService roomService;
    @Autowired
    private RoomRepository roomRepository;

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
}
