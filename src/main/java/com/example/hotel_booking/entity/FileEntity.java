package com.example.hotel_booking.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "files")
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String originalFileName;

    @Column
    private String storedFileName;

    @Column
    private String extension;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    private HotelEntity hotelEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = true) // null 허용
    private RoomEntity roomEntity;


    public static FileEntity toHotelFileEntity(HotelEntity hotelEntity, String originalFileName, String storedFileName){
        FileEntity fileEntity = new FileEntity();
        fileEntity.setOriginalFileName(originalFileName);
        fileEntity.setStoredFileName(storedFileName);
        fileEntity.setHotelEntity(hotelEntity);

        return fileEntity;
    }

    public static FileEntity toRoomFileEntity(RoomEntity roomEntity, String originalFileName, String storedFileName) {
        FileEntity roomFileEntity = new FileEntity() {}; // 익명 클래스를 사용하여 생성
        roomFileEntity.setOriginalFileName(originalFileName);
        roomFileEntity.setStoredFileName(storedFileName);
        roomFileEntity.setRoomEntity(roomEntity);
        return roomFileEntity;
    }
}
