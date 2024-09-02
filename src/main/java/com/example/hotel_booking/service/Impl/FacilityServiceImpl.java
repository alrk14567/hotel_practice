package com.example.hotel_booking.service.Impl;

import com.example.hotel_booking.dto.FacilityDto;
import com.example.hotel_booking.entity.HotelEntity;
import com.example.hotel_booking.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FacilityServiceImpl {

  /*  private final HotelFacilityRepository hotelFacilityRepository;
    private final HotelRepository hotelRepository;


    public List<FacilityEntity> selectAll(Long hotelId) {

        List<HotelFacilityEntity> facilityEntityList = hotelFacilityRepository.findAll();
        List<FacilityEntity> facilityEntityList = new ArrayList<>();

        for (HotelFacilityEntity facilityEntity : facilityEntityList) {
            facilityDtoList.add(FacilityDto.toFacilityDto(facilityEntity, hotelId));
        }

        return facilityDtoList;
    }

    public void save(List<FacilityDto> facilityDtoList, Long hotelId) {
        Optional<HotelEntity> optionalHotelEntity = hotelRepository.findById(hotelId);
        HotelEntity hotelEntity = optionalHotelEntity.get();
        for (int i = 0; i < facilityDtoList.size(); i++) {
            HotelFacilityEntity facilityEntity = HotelFacilityEntity.toFacilityEntity(facilityDtoList.get(i), hotelEntity);
            hotelFacilityRepository.save(facilityEntity);
        }

    }

    public void update(List<FacilityDto> facilityDtoList, Long hotelId) {
        Optional<HotelEntity> optionalHotelEntity = hotelRepository.findById(hotelId);
        HotelEntity hotelEntity = optionalHotelEntity.get();
        for (int i = 0; i < facilityDtoList.size(); i++) {
            HotelFacilityEntity facilityEntity = HotelFacilityEntity.toUpdateFacilityEntity(facilityDtoList.get(i), hotelEntity);
            hotelFacilityRepository.save(facilityEntity);
        }

    }
*/
}