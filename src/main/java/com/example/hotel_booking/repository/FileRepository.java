package com.example.hotel_booking.repository;

import com.example.hotel_booking.entity.FileEntity;
import com.example.hotel_booking.entity.RoomFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FileRepository extends JpaRepository<FileEntity, Long> {

    List<FileEntity> findByHotelEntity_id(Long hotelId);


    List<FileEntity> findByRoomEntity_id(Long roomId);
}
