package com.example.hotel_booking.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link com.example.hotel_booking.entity.FacilityEntity}
 */
@Data
@RequiredArgsConstructor
public class FacilityDto implements Serializable {
    private Long id;
    private Long facilityId;
    private Long hotelId;

    public static FacilityDto toFacilityDto(HotelFacilityEntity hotelFacilityEntity, Long hotelId){
        FacilityDto facilityDto = new FacilityDto();
        facilityDto.setId(hotelFacilityEntity.getId());
        facilityDto.setHotelId(hotelId);
        facilityDto.setFacilityId(hotelFacilityEntity.getFacilityId());
        return facilityDto;
    }
}