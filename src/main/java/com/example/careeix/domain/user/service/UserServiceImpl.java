package com.example.careeix.domain.user.service;


import com.example.careeix.domain.myfile.entity.MyFile;
import com.example.careeix.domain.myfile.service.MyFileService;
import com.example.careeix.domain.user.constant.UserConstants;
import com.example.careeix.domain.user.dto.KakaoLoginRequest;
import com.example.careeix.domain.user.dto.UserInfoRequest;
import com.example.careeix.domain.user.entity.User;
import com.example.careeix.domain.user.exception.NotFoundUserException;
import com.example.careeix.domain.user.exception.UserNicknameDuplicateException;
import com.example.careeix.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.jdo.annotations.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final MyFileService myFileService;


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
        userRepository.save(kakaoUser);
        User user = kakaoLoginRequest.toEntity(kakaoUser.getUserId());

        return userRepository.save(user);
    }


    @Override
    public User updateUserProfile(long userId, String nickName, MultipartFile file) {
        User user = this.getUserByUserId(userId);

        if (!user.getUserNickName().equals(nickName))
            this.userNicknameDuplicateCheck(nickName);

        user.setUserNickName(nickName);

        if (file != null) {
            MyFile profileImg = myFileService.saveImage(file);
            user.setUserProfileImg(profileImg.getFileKey());
        }

        return user;
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
    public void withdrawUser(long userId) {
        User user = this.getUserByUserId(userId);
        user.setStatus(UserConstants.eUser.eDELETE.ordinal());
    }

    @Override
    public User updateUserInfo(long userId, UserInfoRequest userInfoRequest) {
        User user = this.getUserByUserId(userId);
        user.setUserJob(userInfoRequest.getUserJob());
        user.setUserWork(userInfoRequest.getUserWork());
        user.setIntoContent(user.getIntoContent());
        return user;

    }

}




