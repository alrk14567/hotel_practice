package com.example.hotel_booking.repository;


import com.example.hotel_booking.dto.CityDto;
import com.example.hotel_booking.entity.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityRepository extends JpaRepository<CityEntity, Long> {

    Optional<CityEntity> save(CityDto cityDto);
}
