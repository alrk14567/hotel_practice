package com.example.hotel_booking.service;

import com.example.hotel_booking.dto.CityDto;
import com.example.hotel_booking.entity.CityEntity;

import java.util.List;
import java.util.Optional;

public interface CityService {
    CityEntity save(CityDto cityDto);

    List<CityEntity> findAll();

    Optional<CityEntity> findById(Long id);

    void deleteById(Long id);

    boolean existsById(Long id);

    long count();
}
