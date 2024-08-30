package com.example.hotel_booking.service;

import com.example.hotel_booking.entity.UserEntity;

import java.util.List;

public interface AdminUserService {

    List<UserEntity> getAllUsers();

    UserEntity findById(Long id);

    UserEntity save(UserEntity userEntity);

    void deleteById(Long id);

    Long incrementVisitorCount(Long id);

    int countUsers(List<String> roles);
}
