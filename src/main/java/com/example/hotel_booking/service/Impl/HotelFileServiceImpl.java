package com.example.hotel_booking.service.Impl;


import com.example.hotel_booking.dto.HotelFileDto;
import com.example.hotel_booking.entity.HotelEntity;
import com.example.hotel_booking.entity.FileEntity;
import com.example.hotel_booking.repository.HotelFileRepository;
import com.example.hotel_booking.repository.HotelRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HotelFileServiceImpl {

    private final HotelRepository hotelRepository;
    private final HotelFileRepository hotelFileRepository;




    public void save(HotelFileDto hotelFileDto, Long id) {
        Optional<HotelEntity> optionalHotelEntity = hotelRepository.findById(id);
        HotelEntity hotelEntity = optionalHotelEntity.get();
        FileEntity fileEntity = FileEntity.toHotelFileEntity(hotelEntity, hotelFileDto.getOriginalFileName(), hotelFileDto.getStoredFileName());
        hotelFileRepository.save(fileEntity);
    }



    public List<HotelFileDto> findByHotelId(long id) {
        List<FileEntity> fileEntityList = hotelFileRepository.findByHotelEntity_id(id);
        List<HotelFileDto> hotelFileDtoList = new ArrayList<>();
        for (FileEntity entity : fileEntityList) {
            hotelFileDtoList.add(HotelFileDto.toHotelFileDto(entity, id));

        }
        return hotelFileDtoList;
    }

    public List<String> findByHotelIdToName(Long id) {
        List<FileEntity> fileEntityList = hotelFileRepository.findByHotelEntity_id(id);
        List <String> hotelFileStoredNameList=new ArrayList<>();
        for (FileEntity fileEntity : fileEntityList) {
            hotelFileStoredNameList.add(HotelFileDto.toHotelFileDto(fileEntity,id).getStoredFileName());
        }
        return hotelFileStoredNameList;
    }
}