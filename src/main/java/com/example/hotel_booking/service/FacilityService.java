package com.example.hotel_booking.service;

import com.example.hotel_booking.dto.FacilityDto;

import java.util.List;

public interface FacilityService {

    List<FacilityDto> selectAll(Long hotelId);

}
