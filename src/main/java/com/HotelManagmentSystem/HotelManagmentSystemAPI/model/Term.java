package com.HotelManagmentSystem.HotelManagmentSystemAPI.model;

import java.time.LocalDate;

public class Term {
    private int roomId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    public Term(int roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        this.roomId = roomId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }
}
