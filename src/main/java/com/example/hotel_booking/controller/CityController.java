package com.example.hotel_booking.controller;


import com.example.hotel_booking.dto.CityDto;
import com.example.hotel_booking.entity.CityEntity;
import com.example.hotel_booking.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cities")
@CrossOrigin
public class CityController {
    private final CityService cityService;

    @GetMapping("/cityAll")
    public ResponseEntity<List<?>> hotelAll() {

        return ResponseEntity.ok(cityService.findAll());
    }

    @PostMapping("/insert")
    public ResponseEntity<?> write(@RequestBody CityDto cityDto) {
        try {
            // Ensure cityName is not null and not empty
            if (cityDto.getCityName() == null || cityDto.getCityName().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("City name cannot be null or empty");
            }

            CityEntity cityEntity = new CityEntity();
            cityEntity.setCityName(cityDto.getCityName());

            cityService.save(cityEntity);

            return ResponseEntity.ok("City inserted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to insert city: " + e.getMessage());
        }
    }
}
