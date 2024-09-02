package com.example.hotel_booking.service.Impl;

import com.example.hotel_booking.dto.UserDto;
import com.example.hotel_booking.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserServiceImpl userService;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Email: " + username);
        UserEntity userEntity = userService.selectByEmail(username);
        System.out.println(userEntity);
        if (userEntity == null) {
            throw new UsernameNotFoundException(username + "is not a valid username");
        }

        return  null;
    }
}
