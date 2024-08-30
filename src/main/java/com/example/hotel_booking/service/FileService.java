package com.example.hotel_booking.service;

import com.example.hotel_booking.dto.HotelFileDto;
import com.example.hotel_booking.dto.RoomFileDto;
import com.example.hotel_booking.entity.FileEntity;

import java.util.List;

public interface FileService {
    boolean saveHotel(HotelFileDto hotelFileDto, Long id);

    void saveRoom(RoomFileDto roomFileDto, Long roomId);

    List<FileEntity> findByHotelId(long id);

    List<String> findByHotelIdToName(Long id);

    List<FileEntity> findByRoomId(Long id);

    List<String> findByRoomIdToName(Long id);
}
