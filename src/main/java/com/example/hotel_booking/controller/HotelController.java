package com.example.hotel_booking.controller;

import com.example.hotel_booking.dto.FacilityDto;
import com.example.hotel_booking.dto.HotelDto;
import com.example.hotel_booking.dto.HotelFileDto;
import com.example.hotel_booking.service.Impl.FacilityServiceImpl;
import com.example.hotel_booking.service.Impl.HotelFileServiceImpl;
import com.example.hotel_booking.service.Impl.HotelServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/hotels/")
@RequiredArgsConstructor
@CrossOrigin
public class HotelController {
    private final HotelServiceImpl hotelService;
    private final FacilityServiceImpl facilityServiceImpl;
    private final HotelFileServiceImpl hotelFileService;

    @GetMapping("hotelAll")
    public ResponseEntity<List<?>> hotelAll() {

        return ResponseEntity.ok(hotelService.findAll());
    }

    @GetMapping("hotelOne/{id}")
    public ResponseEntity<Map<?,?>> findById(@PathVariable Long id) {

        return ResponseEntity.ok(hashMap);

    }

    @PostMapping("insert")
    public ResponseEntity<Map<?,?>> write(@RequestBody HashMap<String, Object> valueMap) {
        System.out.println(valueMap);
        HotelDto hotelDto = new HotelDto();
        hotelDto.setHotelName((String) valueMap.get("hotelName"));
        hotelDto.setHotelEmail((String) valueMap.get("hotelEmail"));
        hotelDto.setHotelPhone((String) valueMap.get("hotelPhone"));
        hotelDto.setHotelAddress((String) valueMap.get("hotelAddress"));
        System.out.println(valueMap.get("hotelGrade").toString());
        int hotelGrade = Integer.parseInt(valueMap.get("hotelGrade").toString());
        int cityId = Integer.parseInt(valueMap.get("cityId").toString());
        int userId = Integer.parseInt(valueMap.get("userId").toString());


        hotelDto.setHotelGrade((long) hotelGrade);
        hotelDto.setCityId((long) cityId);
        hotelDto.setUserId((long) userId);


        Long id = hotelService.save(hotelDto);


        List<FacilityDto> facilityDtoList = new ArrayList<>();
        System.out.println(valueMap.get("facilities").getClass());

        List<Integer> facilityList = (ArrayList<Integer>) valueMap.get("facilities");
        for (int i = 0; i < facilityList.size(); i++) {
            FacilityDto temp = new FacilityDto();
            temp.setHotelId(id);
            temp.setFacilityId(facilityList.get(i).longValue());
            facilityDtoList.add(temp);
        }

        facilityServiceImpl.save(facilityDtoList, id);
        System.out.println(facilityList);

        System.out.println("HotelController.write");

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("result", hotelDto);
        resultMap.put("resultId", id);

        return ResponseEntity.ok(resultMap);

    }

    @PostMapping("update/{id}")
    public ResponseEntity<Map<?,?>> update(@RequestBody HashMap<String, Object> valueMap, @PathVariable Long id) {
        System.out.println(valueMap);
        HotelDto hotelDto = new HotelDto();

        hotelDto.setHotelName((String) valueMap.get("hotelName"));
        hotelDto.setHotelEmail((String) valueMap.get("hotelEmail"));
        hotelDto.setHotelPhone((String) valueMap.get("hotelPhone"));
        hotelDto.setHotelAddress((String) valueMap.get("hotelAddress"));
        System.out.println(valueMap.get("hotelGrade").toString());
        int hotelGrade = Integer.parseInt(valueMap.get("hotelGrade").toString());
        int cityId = Integer.parseInt(valueMap.get("cityId").toString());

        hotelDto.setId(id);
        hotelDto.setHotelGrade((long) hotelGrade);
        hotelDto.setCityId((long) cityId);

        hotelService.update(hotelDto);

        List<FacilityDto> facilityDtoList = new ArrayList<>();
        System.out.println(valueMap.get("facilities").getClass());

        List<Integer> facilityList = (ArrayList<Integer>) valueMap.get("facilities");
        for (int i = 0; i < facilityList.size(); i++) {
            FacilityDto temp = new FacilityDto();
            temp.setHotelId(id);
            temp.setFacilityId(facilityList.get(i).longValue());
            facilityDtoList.add(temp);
        }

        facilityServiceImpl.update(facilityDtoList, id);
        System.out.println(facilityList);

        System.out.println("HotelController.update");

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("result", hotelDto);
        resultMap.put("resultId", id);

        return ResponseEntity.ok(resultMap);

    }


    @DeleteMapping("delete/{id}")
    public void deleteHotel(@PathVariable Long id) {
        System.out.println("id = " + id);
        hotelService.delete(id);
    }

    @PostMapping("imgInsert/{id}")
    public ResponseEntity<String> insertImg(@RequestParam(value = "file", required = false) MultipartFile[] files, @RequestParam Long id) throws IOException {
        System.out.println("HotelController.insertImg");
        System.out.println("files = " + Arrays.toString(files) + ", id = " + id);

        StringBuilder fileNames = new StringBuilder();

        Path uploadPath = Paths.get("src/main/resources/static/hotel");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }


        for (MultipartFile file : files) {
            String originalFileName = file.getOriginalFilename();
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

            HotelFileDto temp = new HotelFileDto();
            temp.setHotelId(id);
            temp.setOriginalFileName(originalFileName);
            temp.setStoredFileName(storedFileName);
            temp.setExtension(extension);

            System.out.println(temp);


        }
        return ResponseEntity.ok("Files uploaded successfully");


    }

    @GetMapping("image")
    public ResponseEntity<Resource> getImage(@RequestParam String fileName) throws IOException {
        Path filePath = Paths.get("src/main/resources/static/hotel").resolve(fileName);
        if (Files.exists(filePath)) {
            Resource fileResource = new UrlResource(filePath.toUri());
            return ResponseEntity.ok()
                    .body(fileResource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}