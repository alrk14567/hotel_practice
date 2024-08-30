package com.example.hotel_booking.controller;

import com.example.hotel_booking.dto.ReservationDto;
import com.example.hotel_booking.dto.RoomDto;
import com.example.hotel_booking.dto.RoomFileDto;
import com.example.hotel_booking.entity.FileEntity;
import com.example.hotel_booking.entity.RoomEntity;
import com.example.hotel_booking.service.FileService;
import com.example.hotel_booking.service.Impl.*;
import com.example.hotel_booking.service.ReservationService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/reservation/")
public class ReservationController {

    private final ReservationServiceImpl reservationService;
    private final UserServiceImpl userService;
    private final RoomServiceImpl roomService;
    private final FileService fileService;
    private final RoomTypeServiceImpl roomTypeService;


    @GetMapping("showOne/{id}")
    public ResponseEntity<Map<?, ?>> selectOne(@PathVariable Long id) {
        HashMap<String, Object> resultMap = new HashMap<>();
        // 예약정보
        ReservationDto reservationDto = reservationService.selectOne(id);
        // 예약한 방 정보
        RoomEntity roomEntity = roomService.selectOne(reservationDto.getRoomId());

        // 예약한 방 사진
        List<FileEntity> roomFileDtoList = fileService.findByRoomId(reservationDto.getRoomId());

        resultMap.put("reservationDto", reservationDto);
        resultMap.put("roomDto", roomEntity);
        resultMap.put("roomTypeList", roomTypeService.selectAll());
        resultMap.put("roomFileDtoList", roomFileDtoList);

        return ResponseEntity.ok(resultMap);
    }

    @PostMapping("roomReservation/{roomId}")
    public ResponseEntity<Map<?, ?>> write(@PathVariable Long roomId, @RequestBody ReservationDto reservationDto) {
        reservationDto.setRoomId(roomId);
        // 유저 정보는 가져와야하니까 로그인된 사람이 예약눌렀을때 로그인된 아이디의 id값을 출력해야함
        reservationDto.setUserId(1L);
        String reservationNum = String.valueOf(System.currentTimeMillis());
        reservationDto.setReservationNumber(reservationNum);
        // 가격은 계산 나중에 다시 설정
        // 방 가격
        // endDate-startDate= 2   얘네를 스트링으로 받아와서 인티저로 바꿔 그다음에
        reservationDto.setPayPrice(roomService.selectOne(roomId).getRoomPrice() * 2);
        HashMap<String, Object> resultMap = new HashMap<>();
        try {
            Long reservationId = reservationService.insert(reservationDto);

            resultMap.put("result", "success");
            resultMap.put("reservationId", reservationId);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("result", "fail");
        }
        return ResponseEntity.ok(resultMap);
    }

    @PostMapping("canceled/{reservationId}")
    public ResponseEntity<Map<?, ?>> cancel(@PathVariable Long reservationId) {
        HashMap<String, Object> resultMap = new HashMap<>();
        ReservationDto reservationDto = reservationService.selectOne(reservationId);
        resultMap.put("destReservationId", reservationDto.getId());
        System.out.println(reservationDto);
        try {
            reservationService.cancled(reservationDto);
            resultMap.put("result", "success");
            resultMap.put("reservationId", reservationDto.getId());
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("result","fail");
        }
        return ResponseEntity.ok(resultMap);
    }
}

