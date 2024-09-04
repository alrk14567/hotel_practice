package com.example.hotel_booking.service.Impl;

import com.example.hotel_booking.dto.CityDto;
import com.example.hotel_booking.entity.CityEntity;
import com.example.hotel_booking.repository.CityRepository;
import com.example.hotel_booking.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    @Override
    public Optional<CityEntity> save(CityDto cityDto) {
        return cityRepository.save(cityDto);
    }

    @Override
    public List<CityEntity> findAll() {
        return cityRepository.findAll();
    }

    @Override
    public Optional<CityEntity> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public boolean existsById(Long id) {
        return false;
    }

    @Override
    public long count() {
        return 0;
    }
}
