package com.example.hotel_booking.service.Impl;

import com.example.hotel_booking.dto.HotelFileDto;
import com.example.hotel_booking.dto.RoomFileDto;
import com.example.hotel_booking.entity.FileEntity;
import com.example.hotel_booking.entity.HotelEntity;
import com.example.hotel_booking.entity.RoomEntity;
import com.example.hotel_booking.entity.RoomFileEntity;
import com.example.hotel_booking.repository.FileRepository;
import com.example.hotel_booking.repository.HotelRepository;
import com.example.hotel_booking.repository.RoomRepository;
import com.example.hotel_booking.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final FileRepository fileRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;

    public boolean saveHotel(HotelFileDto hotelFileDto, Long id) {
        Optional<HotelEntity> optionalHotelEntity = hotelRepository.findById(id);
        HotelEntity hotelEntity = optionalHotelEntity.get();
        FileEntity fileEntity = FileEntity.toHotelFileEntity(hotelEntity, hotelFileDto.getOriginalFileName(), hotelFileDto.getStoredFileName());
        fileRepository.save(fileEntity);
        return true;
    }

    public void saveRoom(RoomFileDto roomFileDto, Long roomId) {
        Optional<RoomEntity> optionalRoomEntity = roomRepository.findById(roomId);
        RoomEntity roomEntity=optionalRoomEntity.get();
        FileEntity fileEntity=FileEntity.toRoomFileEntity(roomEntity,roomFileDto.getOriginalFileName(), roomFileDto.getStoredFileName());
        fileRepository.save(fileEntity);
    }

    public List<FileEntity> findByHotelId(long id) {
        List<FileEntity> fileEntityList = fileRepository.findByHotelEntity_id(id);

        return fileEntityList;
    }

    public List<String> findByHotelIdToName(Long id) {
        List<FileEntity> fileEntityList = fileRepository.findByHotelEntity_id(id);
        List <String> hotelFileStoredNameList=new ArrayList<>();
        for (FileEntity fileEntity : fileEntityList) {
            hotelFileStoredNameList.add(HotelFileDto.toHotelFileDto(fileEntity,id).getStoredFileName());
        }
        return hotelFileStoredNameList;
    }

    public List<FileEntity> findByRoomId(Long id) {
        List<FileEntity> fileEntityList = fileRepository.findByRoomEntity_id(id);

        return fileEntityList;
    }

    public List<String> findByRoomIdToName(Long id) {
        List<FileEntity> fileEntityList = fileRepository.findByRoomEntity_id(id);
        List <String> roomFileStoredNameList=new ArrayList<>();
        for (FileEntity fileEntity : fileEntityList) {
            roomFileStoredNameList.add(RoomFileDto.toRoomFileDto(fileEntity,id).getStoredFileName());
        }
        return roomFileStoredNameList;
    }

}
