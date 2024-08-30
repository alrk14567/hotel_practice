package com.example.hotel_booking.service;

import com.example.hotel_booking.dto.UserDto;
import com.example.hotel_booking.entity.UserEntity;

import java.util.List;

public interface UserService {
    List<UserEntity> getAllUsers();

    UserEntity findByEmail(String email);

    UserEntity save(UserEntity userEntity);

    void deleteUser(Long id);

    Long incrementVisitorCount(Long userId);

    int countUsers(List<String> roles);

    UserEntity selectByEmail(String Email);

    void register(UserDto userDto);

    UserEntity findById(Long id);

    UserEntity update(UserDto guestDto);

    UserEntity findByEmailAndNameAndPhone(String email, String name, String phone);

    String getPassword(UserEntity user);

    UserEntity findByNameAndPhone(String name, String phone);

    UserEntity auth(String email, String password);

    boolean checkPassword(Long userId, String password);


}
