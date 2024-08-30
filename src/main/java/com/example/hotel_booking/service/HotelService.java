package com.example.hotel_booking.service;

import com.example.hotel_booking.dto.HotelDto;
import com.example.hotel_booking.entity.HotelEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface HotelService {

    List<HotelEntity> searchHotel(List<Long> gradeList, List<Long> cityIdList
            , List<Long> facilityIdList, String hotelName);

    Long save(HotelDto hotelDto);

    ResponseEntity<Void> delete(Long id);

    List<HotelEntity> findAll();

    HotelEntity findById(long id);

    HotelEntity update(HotelDto hotelDto);

    List<Long> facilityAll(long id);
}
