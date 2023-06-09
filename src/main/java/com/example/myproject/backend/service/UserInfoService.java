package com.example.myproject.backend.service;

import com.example.myproject.backend.domain.entity.UserInfoEntity;
import com.example.myproject.backend.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserInfoService {

    @Autowired
    UserInfoRepository userInfoRepository;

    public void UserInfoService(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    public UserInfoEntity addUser(UserInfoEntity userInfoEntity1) {

        UserInfoEntity userInfoEntity = new UserInfoEntity();

        Long maxId = userInfoRepository.findMaxUserId();
        if (maxId == null) {
            userInfoEntity.setUserId(100000001L);
        } else userInfoEntity.setUserId(maxId + 1);

        userInfoEntity.setFullName(userInfoEntity1.getFullName());
        userInfoEntity.setEmail(userInfoEntity1.getEmail());
        userInfoEntity.setUsername(userInfoEntity1.getUsername());

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10, new SecureRandom());
        String encryptedPassword = bCryptPasswordEncoder.encode(userInfoEntity1.getPassword());

        userInfoEntity.setPassword(encryptedPassword);
        userInfoEntity.setDob(userInfoEntity1.getDob());
        userInfoEntity.setRole("U");
        userInfoEntity.setActive("1");
        userInfoEntity.setiUsr(userInfoEntity1.getUsername());
        userInfoEntity.setiDt(LocalDate.now());

        return userInfoRepository.save(userInfoEntity);

    }

    public UserInfoEntity updateUser(UserInfoEntity userInfoEntity) {

        userInfoEntity.setuUsr(userInfoEntity.getUsername());
        userInfoEntity.setuDt(LocalDate.now());

        return userInfoRepository.save(userInfoEntity);

    }
}
