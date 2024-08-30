package com.example.hotel_booking.service.Impl;

import com.example.hotel_booking.dto.UserDto;
import com.example.hotel_booking.entity.UserEntity;
import com.example.hotel_booking.repository.UserRepository;
import com.example.hotel_booking.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserEntity> getAllUsers() {

        return userRepository.findAll();
    }

    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + email));
    }


    public UserEntity save(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    public void deleteUser(Long id) {

        userRepository.deleteById(id);
    }


    public Long incrementVisitorCount(Long userId) {
        // 현재 사용자의 방문 횟수를 조회
        Long currentVisitorCount = userRepository.findVisitorCountByUserId(userId);
        if (currentVisitorCount == null) {
            currentVisitorCount = 0L;
        }
        // 방문 횟수를 1 증가시킴
        Long updatedVisitorCount = currentVisitorCount + 1;
        userRepository.updateVisitorCount(updatedVisitorCount, userId);
        return updatedVisitorCount;
    }

    public int countUsers(List<String> roles) {
        return userRepository.countUsersByRoles(roles);
    }



    public UserEntity selectByEmail(String Email) {
        Optional<UserEntity> byUserEmail = userRepository.findByEmail(Email);

        if (!byUserEmail.isPresent()) {
            return null;
        } else {
            return byUserEmail.get();
        }
    }

    public void register(UserDto userDto) {
        UserEntity userEntity = UserEntity.toUserEntity(userDto);
        userRepository.save(userEntity);
    }

    @Transactional
    public UserEntity findById(Long id) {
        Optional<UserEntity> guestEntityOptional = userRepository.findById(id);
        if (guestEntityOptional.isPresent()) {
            UserEntity guestEntity = guestEntityOptional.get();
            UserDto guestDto = UserDto.toGuestDto(guestEntity);
            return guestEntityOptional.get();
        } else {
            return null;
        }
    }

    public UserEntity update(UserDto guestDto) {

                return userRepository.save(UserEntity.toUserEntity(guestDto));
    }

    public UserEntity findByEmailAndNameAndPhone(String email, String name, String phone) {
        return userRepository.findByEmailAndNameAndPhone(email, name, phone).get();
    }

    public String getPassword(UserEntity user) {
        // 나중에 수정해야해 이건 테스트용
        return user.getPassword();
    }

    public UserEntity findByNameAndPhone(String name, String phone) {
        return userRepository.findByNameAndPhone(name, phone).get();
    }

    public UserEntity auth(String email, String password) {
        System.out.println("Received email: " + email);  // 이메일 출력
        System.out.println("Received password: " + password);  // 비밀번호 출력
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
           /* if (passwordEncoder.matches(password, user.getPassword())) {
                return Optional.of(user);
            }*/
            if (password.equals(user.getPassword())) {  // 평문 비교
                return userOptional.get();
            }
        }
        return null;
    }

    public boolean checkPassword(Long userId, String password) {
        Optional<UserEntity> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            // 비밀번호를 암호화 체크 해야함
            return user.getPassword().equals(password);
        } else {
            return false; // 사용자가 존재하지 않는 경우
        }
    }


}