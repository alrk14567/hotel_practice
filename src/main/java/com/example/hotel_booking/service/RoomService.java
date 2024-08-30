package com.example.hotel_booking.service;

import com.example.hotel_booking.dto.RoomDto;
import com.example.hotel_booking.entity.RoomEntity;

import java.io.IOException;
import java.util.List;

public interface RoomService {
    boolean save(Long hotelId, RoomDto roomDto);

    List<RoomEntity> selectAll(Long hotelId);

    RoomEntity selectOne(Long roomId);

    RoomEntity update(RoomDto roomDto);

    boolean deleteById(Long id);
}
