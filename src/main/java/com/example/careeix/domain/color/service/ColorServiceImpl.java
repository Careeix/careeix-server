package com.example.careeix.domain.color.service;


import com.example.careeix.domain.color.entity.Color;
import com.example.careeix.domain.color.repository.ColorRepository;
import com.example.careeix.domain.user.entity.User;
import com.example.careeix.domain.user.repository.UserRepository;
import com.example.careeix.utils.file.service.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ColorServiceImpl implements ColorService {

    private final ColorRepository colorRepository;
    private final UserRepository userRepository;

    private final AwsS3Service awsS3Service;


    @Override
    public User getColorName(User finalUser) {
        List<Color> c = colorRepository.findAll();
        Color color = c.get(0);
        finalUser.setUserProfileColor(color.getColorCode());
        return userRepository.save(finalUser);
    }
}






