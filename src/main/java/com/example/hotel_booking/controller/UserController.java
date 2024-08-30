package com.example.hotel_booking.controller;

import com.example.hotel_booking.dto.*;
import com.example.hotel_booking.entity.UserEntity;
import com.example.hotel_booking.service.Impl.AdminUserServiceImpl;
import com.example.hotel_booking.service.Impl.UserServiceImpl;
import com.example.hotel_booking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/")
public class UserController {
    private final UserServiceImpl USER_SERVICE;
    @Autowired
    private BCryptPasswordEncoder encoder;

    private final UserService userService;



    @RequestMapping("authOk")
    public ResponseEntity<Map<String, Object>> authOk(Authentication authentication) {
        Map<String, Object> resultMap = new HashMap<>();
        UserDto userDto = (UserDto) authentication.getPrincipal();
        // getPrincipal을 바로 resultMap에 넣는건 좋지 않다 왜냐면 패스워드가 넘어가니깐
        resultMap.put("result", "success");
        resultMap.put("id", userDto.getId());
        resultMap.put("nickname", userDto.getNickname());
        resultMap.put("role", userDto.getRole());
        resultMap.put("address", userDto.getAddress());
        resultMap.put("phone", userDto.getPhone());
        resultMap.put("email", userDto.getEmail());


        return ResponseEntity.ok(resultMap);
    }

    @RequestMapping("authFail")
    public ResponseEntity<Map<String, Object>> authFail() {
        System.out.println("Auth has failed");
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("result", "fail");

        return ResponseEntity.ok(resultMap);
    }

    @RequestMapping("logOutSuccess")
    public ResponseEntity<Void> logOutSuccess() {
        System.out.println("log out success");

        //위에선 resultMap을 통해서 ok 했지만 로그아웃은 아무것도 없으므로 어쩌라고 듣지 않기 위해 .build()를 해준다.
        return ResponseEntity.ok().build();
    }

    @PostMapping("register")
    public HashMap<String, Object> register(@RequestBody UserDto userDto) {
        System.out.println(userDto);
        userDto.setPassword(encoder.encode(userDto.getPassword()));
        HashMap<String, Object> resultMap = new HashMap<>();

        try {
            USER_SERVICE.register(userDto);
            resultMap.put("result", "success");
            resultMap.put("resultId", userDto.getId());

        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("result", "fail");
        }
        return resultMap;
    }

    @GetMapping("userCount")
    public ResponseEntity<Map<String, Integer>> getUserCount(@RequestParam List<String> roles) {
        int userCount = userService.countUsers(roles);
        Map<String, Integer> response = new HashMap<>();
        response.put("userCount", userCount);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/userList")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        List<UserEntity> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable Long id) {
        UserEntity user = userService.findById(id);
        System.out.println("AdminUserController.getUserById");
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestBody UserEntity id) {

        return null;
    }

    @PostMapping("/update")
    public ResponseEntity<UserEntity> updateUser(@RequestBody UserEntity userEntity) {
        UserEntity updateUser = userService.findById(userEntity.getId());

        updateUser.setAddress(userEntity.getAddress());
        updateUser.setPhone(userEntity.getPhone());
        updateUser.setEmail(userEntity.getEmail());
        updateUser.setRole(userEntity.getRole());

        UserEntity savedUser = userService.save(updateUser);
        return ResponseEntity.ok(savedUser);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/incrementVisitor")
    public ResponseEntity<?> incrementVisitorCount(@RequestParam("user_id") Long userId) {
        Long updatedVisitorCount = userService.incrementVisitorCount(userId);
        return ResponseEntity.ok(userService.incrementVisitorCount(userId));
    }

    //id로 유저 정보 가져오기(내 정보)
    @GetMapping("update/{id}")
    public ResponseEntity<UserEntity> update(@PathVariable Long id, Model model) {
        System.out.println("id = " + id + ", model = " + model);
        UserDto userDto = guestService.findById(id);
        model.addAttribute("guest", userDto);
        return ResponseEntity.ok(guestService.findById(id));
    }

    //유저 업데이트(수정 페이지로)
    @PostMapping("update")
    public UserDto update(@RequestBody UserDto guestDto, Model model) {
        UserDto updatedGuestDto = guestService.update(guestDto);
        model.addAttribute("guest", updatedGuestDto);
        return updatedGuestDto;
    }

    //예약 정보 출력(내 예약)
    @GetMapping("reservations/{id}")
    public ResponseEntity<List<ReservationDto>> reservations(@PathVariable("id") Long id) {
        try {
            System.out.println("Received ID: " + id);
            List<ReservationDto> reservations = reservationService.findAllByGuestId(id);
            System.out.println("Reservations found: " + reservations.size());

            if (reservations.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // 404 반환
            }
            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            e.printStackTrace();  // 예외 전체 스택 트레이스를 콘솔에 출력
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);  // 500 반환
        }
    }

    // 좋아하는 호텔 유저 id를 통해 가져오기(내 찜 목록)
    @GetMapping("wishlist/{id}")
    public List<WishlistDto> wishlist(@PathVariable Long id) {
        List<WishlistDto> WishlistList = wishlistService.findAllByGuestId(id);

        return WishlistList;
    }

    // 좋아하는 호텔 저장
    @PostMapping("wishlist")
    public String wishAddDelete(@RequestBody WishlistDto wishlistDto) {
        wishlistService.wishAddDelete(wishlistDto);

        return null;
    }

    //리뷰 등록
    @GetMapping("review/{id}")
    public List<ReviewDto> review(@PathVariable Long id) {
        List<ReviewDto> reviewDtoList = reviewService.findAllByGuestId(id);

        return reviewDtoList;
    }

    //리뷰 수정 페이지로 갈 때 리뷰 정보 가져오기
    @GetMapping("review")
    public ReviewDto reviewPage() {
        ReviewDto reviewDto = new ReviewDto();
        return reviewDto;
    }

    // 리뷰 추가 API
    @PostMapping("review")
    public void reviewAdd(@RequestBody ReviewDto reviewDto) {
        reviewDto.setTotalRating(reviewDto.setTotalRating());
        reviewService.reviewAdd(reviewDto);
    }

    //리뷰 수정 페이지로
    @GetMapping("review/update/{id}")
    public ReviewDto updatePage(@PathVariable Long id) {
        ReviewDto reviewDto = reviewService.findById(id);
        return reviewDto;
    }

    //리뷰 업데이트 API
    @PostMapping("review/update")
    public void reviewUpdate(@RequestBody ReviewDto reviewDto) {
        reviewDto.setTotalRating(reviewDto.setTotalRating());
        reviewService.reviewUpdate(reviewDto);
    }

    //리뷰 삭제 API
    @GetMapping("review/delete/{id}")
    public void reviewDelete(@PathVariable Long id) {
        reviewService.deleteReview(id);
    }

    //이메일 찾기
    @PostMapping("forgot-username")
    public ResponseEntity<Map<String, Object>> forgotUsername(@RequestBody Map<String, String> request) {
        String name = request.get("name");
        String phone = request.get("phone");
        Map<String, Object> resultMap = new HashMap<>();

        try {
            Optional<UserEntity> userOptional = guestService.findByNameAndPhone(name, phone);
            if (userOptional.isPresent()) {
                UserEntity user = userOptional.get();
                resultMap.put("result", "success");
                resultMap.put("email", user.getEmail());
                return ResponseEntity.ok(resultMap);
            } else {
                resultMap.put("result", "fail");
                resultMap.put("message", "User not found");
                return ResponseEntity.status(404).body(resultMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("result", "fail");
            resultMap.put("message", e.getMessage());
            return ResponseEntity.status(500).body(resultMap);
        }
    }

    //비밀번호 찾기
    @PostMapping("forgot-password")
    public ResponseEntity<Map<String, Object>> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String name = request.get("name");
        String phone = request.get("phone");
        Map<String, Object> resultMap = new HashMap<>();

        try {
            Optional<UserEntity> userOptional = guestService.findByEmailAndNameAndPhone(email, name, phone);
            if (userOptional.isPresent()) {
                UserEntity user = userOptional.get();
                if (user.getName().equals(name) && user.getPhone().equals(phone)) {
                    String password = guestService.getPassword(user); // 비밀번호 반환
                    resultMap.put("result", "success");
                    resultMap.put("password", password); // 주의: 해시된 비밀번호가 반환됩니다.
                    return ResponseEntity.ok(resultMap);
                } else {
                    resultMap.put("result", "fail");
                    resultMap.put("message", "일치하는 정보가 없습니다.");
                    return ResponseEntity.status(404).body(resultMap);
                }
            } else {
                resultMap.put("result", "fail");
                resultMap.put("message", "찾기 실패");
                return ResponseEntity.status(404).body(resultMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("result", "fail");
            resultMap.put("message", e.getMessage());
            return ResponseEntity.status(500).body(resultMap);
        }
    }
    @PostMapping("auth")
    public ResponseEntity<Map<String, Object>> auth(@RequestBody Map<String, String> request) {
        System.out.println("GuestController.auth");
        String email = request.get("email");
        String password = request.get("password");
        Map<String, Object> resultMap = new HashMap<>();

        Optional<UserEntity> userOptional = guestService.auth(email, password);
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            resultMap.put("result", "success");
            resultMap.put("id", user.getId());
            resultMap.put("role", user.getRole());
            resultMap.put("nickname", user.getNickname());
            resultMap.put("name", user.getName());
            resultMap.put("email", user.getEmail());
            resultMap.put("password",user.getPassword());
            resultMap.put("phone", user.getPhone());
            resultMap.put("address", user.getAddress());
            resultMap.put("userTotalAmount",user.getUserTotalAmount());


            return ResponseEntity.ok(resultMap);
        } else {
            resultMap.put("result", "fail");
            resultMap.put("message", "아이디 비번 확인 바람");
            return ResponseEntity.status(401).body(resultMap);
        }
    }

    @PostMapping("check-password")
    public ResponseEntity<Map<String, Boolean>> checkPassword(@RequestBody PasswordCheckRequest request) {
        boolean valid = guestService.checkPassword(request.getUserId(), request.getPassword());
        Map<String, Boolean> response = new HashMap<>();
        response.put("valid", valid);
        return ResponseEntity.ok(response);
    }


}
