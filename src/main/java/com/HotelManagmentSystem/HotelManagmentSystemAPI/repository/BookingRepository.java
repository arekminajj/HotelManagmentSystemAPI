package com.HotelManagmentSystem.HotelManagmentSystemAPI.repository;

import com.HotelManagmentSystem.HotelManagmentSystemAPI.model.Booking;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookingRepository extends CrudRepository<Booking, Integer> {
    List<Booking> findByRoomId(int roomId);
}
