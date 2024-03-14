package com.HotelManagmentSystem.HotelManagmentSystemAPI.controller;

import com.HotelManagmentSystem.HotelManagmentSystemAPI.Repository.RoomRepository;
import com.HotelManagmentSystem.HotelManagmentSystemAPI.model.Room;
import com.HotelManagmentSystem.HotelManagmentSystemAPI.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
public class RoomController {

    //private RoomService roomService;
    @Autowired
    private RoomRepository roomRepository;

    //public RoomController(RoomRepository roomRepository) {
    //    this.roomRepository = roomRepository;
    //}


    @PostMapping(path="/add") // Map ONLY POST Requests
    public @ResponseBody String addNewRoom () {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request

        Room room = new Room();

        room.setNumber(12);
        room.setDescription("jakis opis pokoju");
        room.setCapacity(3);
        room.setFloor(4);
        room.setPrice(200);


        roomRepository.save(room);
        return "Saved";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<Room> getAllUsers() {
        // This returns a JSON or XML with the users
        return roomRepository.findAll();
    }

}
