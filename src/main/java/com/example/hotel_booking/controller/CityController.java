package com.example.hotel_booking.controller;


import com.example.hotel_booking.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cities")
@CrossOrigin
public class CityController {
    private final CityService cityService;

    @GetMapping("cityAll")
    public ResponseEntity<List<?>> hotelAll() {

        return ResponseEntity.ok(cityService.findAll());
    }
}
