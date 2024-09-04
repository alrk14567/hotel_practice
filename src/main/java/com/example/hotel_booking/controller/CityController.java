package com.example.hotel_booking.controller;


import com.example.hotel_booking.dto.CityDto;
import com.example.hotel_booking.entity.CityEntity;
import com.example.hotel_booking.service.CityService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<?> write(@RequestBody CityEntity cityEntity) {
        return cityService.save(cityEntity).get();
    }
}
