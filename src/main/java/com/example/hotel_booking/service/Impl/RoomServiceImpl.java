package com.example.hotel_booking.service.Impl;

import com.example.hotel_booking.dto.RoomDto;
import com.example.hotel_booking.entity.HotelEntity;
import com.example.hotel_booking.entity.RoomEntity;
import com.example.hotel_booking.entity.RoomTypeEntity;
import com.example.hotel_booking.entity.UserEntity;
import com.example.hotel_booking.repository.*;
import com.example.hotel_booking.service.RoomService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {


    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final RoomFileRepository roomFileRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final UserRepository userRepository;


    public boolean save(Long hotelId,RoomDto roomDto) throws IOException {

        Optional<HotelEntity> optionalHotelEntity = hotelRepository.findById(roomDto.getHotelId());
        if (optionalHotelEntity.isPresent()) {
            HotelEntity hotelEntity = optionalHotelEntity.get();
            UserEntity userEntity = userRepository.findById(roomDto.getUserId()).get();
            RoomTypeEntity roomTypeEntity = roomTypeRepository.findById(roomDto.getRoomTypeId()).get();
            RoomEntity roomEntity = RoomEntity.toInsertEntity(roomDto, hotelEntity, roomTypeEntity, userEntity);
            roomRepository.save(roomEntity);
            return true;
        }
        return false;
    }

    @Transactional
    public List<RoomEntity> selectAll(Long hotelId) {
        HotelEntity hotelEntity = hotelRepository.findById(hotelId).get();
        List<RoomEntity> roomEntityList = roomRepository.findAllByHotelEntityOrderByIdDesc(hotelEntity);
        List<RoomDto> roomDtoList = new ArrayList<>();
        for (RoomEntity roomEntity : roomEntityList) {
            RoomDto roomDto = RoomDto.toRoomDto(roomEntity, hotelId);
            roomDtoList.add(roomDto);
        }
        return roomEntityList;
    }

    @Transactional
    public RoomEntity selectOne(Long roomId) {
        Optional<RoomEntity> optionalRoomEntity = roomRepository.findById(roomId);
//        System.out.println(optionalRoomEntity);
        if (optionalRoomEntity.isPresent()) {
            RoomEntity roomEntity = optionalRoomEntity.get();

            return roomEntity;
        } else {
            return null;
        }
    }

    @Transactional
    public RoomEntity update(RoomDto roomDto) {
        HotelEntity hotelEntity = hotelRepository.findById(roomDto.getHotelId()).get();
        RoomTypeEntity roomTypeEntity = roomTypeRepository.findById(roomDto.getRoomTypeId()).get();
        UserEntity userEntity = userRepository.findById(roomDto.getUserId()).get();
        RoomEntity roomEntity = RoomEntity.toUpdateEntity(roomDto, hotelEntity, roomTypeEntity, userEntity);
        roomRepository.save(roomEntity);
        return roomEntity;
    }

    @Transactional
    public boolean deleteById(Long id) {

        roomRepository.deleteById(id);
        return true;
    }


}
