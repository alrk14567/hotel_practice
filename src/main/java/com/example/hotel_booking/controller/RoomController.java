package com.example.hotel_booking.controller;

import com.example.hotel_booking.dto.RoomDto;
import com.example.hotel_booking.dto.RoomFileDto;
import com.example.hotel_booking.entity.RoomEntity;
import com.example.hotel_booking.service.Impl.RoomTypeServiceImpl;
import com.example.hotel_booking.service.RoomService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
public class RoomController {


    private final RoomService roomService;
    private final RoomTypeServiceImpl roomTypeService;
    private final RoomFileServiceImpl roomFileService;


    @GetMapping("/{id}")        //다 리스폰스엔티티 => 엔티티값 넣어라
    public ResponseEntity<?> getById(@PathVariable Long id) {

        return ResponseEntity.ok(roomService.selectOne(id));
    }

    @GetMapping("/list")
    public ResponseEntity<List<?>> getlist(@RequestParam Long id) {

        return ResponseEntity.ok(roomService.selectAll(id));
    }


    @PostMapping("imgInsert/{id}")
    public void insertImg(@RequestParam(value = "file", required = false) MultipartFile[] files, @PathVariable Long id, HttpServletRequest request) throws IOException {

        System.out.println("files = " + Arrays.toString(files) + ", id = " + id);

        StringBuilder fileNames = new StringBuilder();

        Path uploadPath = Paths.get("src/main/resources/static/room/");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }


        for (MultipartFile file : files) {
            String originalFileName = file.getOriginalFilename();
            long fileSize = file.getSize();
            String extension = "";

            if (originalFileName != null && originalFileName.contains(".")) {
                extension = originalFileName.substring(originalFileName.lastIndexOf('.') + 1);
            }

            String storedFileName = System.currentTimeMillis() + "." + extension;
            fileNames.append(",").append(storedFileName);

            Path filePath = uploadPath.resolve(storedFileName);
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

            }

            RoomFileDto temp = new RoomFileDto();
            temp.setId(id);
            temp.setOriginalFileName(originalFileName);
            temp.setStoredFileName(storedFileName);
            temp.setExtension(extension);

            roomFileService.save(temp, id);

        }

    }

    @PostMapping("/{hotelId}")  //상수에서 일부를 변수처리 하기위해 {}를 넣어줌
    public ResponseEntity<Boolean> write(@PathVariable Long hotelId, @RequestBody RoomDto roomDto) {

        return ResponseEntity.ok(roomService.save(hotelId,roomDto));
    }

    @PutMapping("/update")
    public ResponseEntity<RoomEntity> update(@RequestBody RoomDto roomDto) {

        return ResponseEntity.ok(roomService.update(roomDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {

        return ResponseEntity.ok(roomService.deleteById(id));
    }

    @GetMapping("/roomImage")
    public ResponseEntity<Resource> getImage(@RequestParam String fileName) throws IOException {
        Path filePath = Paths.get("src/main/resources/static/room").resolve(fileName);
        if (Files.exists(filePath)) {
            Resource fileResource = new UrlResource(filePath.toUri());
            return ResponseEntity.ok()
                    .body(fileResource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
