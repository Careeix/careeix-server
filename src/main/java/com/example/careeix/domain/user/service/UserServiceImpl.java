package com.example.careeix.domain.user.service;


import com.example.careeix.domain.user.dto.AppleLoginRequest;
import com.example.careeix.domain.user.dto.KakaoLoginRequest;
import com.example.careeix.domain.user.dto.UserInfoRequest;
import com.example.careeix.domain.user.entity.User;
import com.example.careeix.domain.user.exception.NotFoundUserException;
import com.example.careeix.domain.user.exception.UserNicknameDuplicateException;
import com.example.careeix.domain.user.repository.UserRepository;
import com.example.careeix.utils.file.service.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.jdo.annotations.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final AwsS3Service awsS3Service;


    /**
     * 닉네임 중복 확인
     * @param userNickName
     * @return
     */
    @Override
    public void userNicknameDuplicateCheck(String userNickName) {
        Optional<User> user = userRepository.findByUserNickName(userNickName);

        if (!user.isEmpty()) throw new UserNicknameDuplicateException();
    }


    @Override
    public User insertUser(KakaoLoginRequest kakaoLoginRequest, User kakaoUser) {
        User user = kakaoLoginRequest.toEntity(kakaoUser.getUserId(), kakaoUser);

        return userRepository.save(user);
    }

    @Override
    public User insertUserApple(AppleLoginRequest appleLoginRequest, User appleUser) {
        User user = appleLoginRequest.toEntity(appleUser.getUserId(), appleUser);

        return userRepository.save(user);
    }


    @Override
    public User updateUserProfile(long userId, String nickName, MultipartFile file) {
        User user = this.getUserByUserId(userId);

        if (!user.getUserNickName().equals(nickName))
            this.userNicknameDuplicateCheck(nickName);

        user.setUserNickName(nickName);

        if (file != null) {
            String filename = awsS3Service.uploadImage(file);
            user.setUserProfileImg(awsS3Service.makeUrlOfFilename(filename));

        }

        return userRepository.save(user);
    }


    /**
     * 사용자 정보 조회
     * @param userId
     * @return User
     */

    @Override
    public User getUserByUserId(Long userId) {
        return userRepository.findByUserId(userId).orElseThrow(NotFoundUserException::new);
    }

    /**
     * 회원 탈퇴
     * @param userId
     * @return
     */
    @Override
    @Transactional
    public User withdrawUser(long userId) {
        User user = this.getUserByUserId(userId);
        user.setStatus(0);
        return userRepository.save(user);

    }

    @Override
    public User updateUserInfo(long userId, UserInfoRequest userInfoRequest) {
        User user = this.getUserByUserId(userId);
        user.setUserJob(userInfoRequest.getUserJob());
        user.setUserWork(userInfoRequest.getUserWork());
        user.setIntoContent(userInfoRequest.getUserIntro());
        return userRepository.save(user);

    }

}




