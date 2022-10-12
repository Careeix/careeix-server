package com.example.careeix.domain.user.service;


import com.example.careeix.domain.user.entity.User;
import com.example.careeix.domain.user.exception.UserEmailDuplicateException;
import com.example.careeix.domain.user.exception.UserNicknameDuplicateException;
import com.example.careeix.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

 
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


    /**
     * 이메일 중복 확인
     * @param userEmail
     * @return
     */
    @Override
    public void userEmailDuplicateCheck(String userEmail) {
        Optional<User> user = userRepository.findByUserEmail(userEmail);

        if (!user.isEmpty()) throw new UserEmailDuplicateException();
    }


    /**
     * 사용자 정보 조회
     * @param userId
     * @return User
     */

    @Override
    public User getUserByUserId(Long userId) { return userRepository.findByUserId(userId).get();}

//    /**
//     * 사용자 정보 수정
//     * @param loginId, userProfileRequest, file
//     * @return User
//     */
//    @Override
//    @Transactional
//    public User updateUserProfile(String loginId, UserProfileRequest profileRequest, MultipartFile file) {
//        User user = this.getUserProfile(loginId);
//
//        if (!user.getUserNickName().equals(profileRequest.getUserNickName()))
//            this.userNicknameDuplicateCheck(profileRequest.getUserNickName());
//
//        user.setUserNickName(profileRequest.getUserNickName());
//
//        if (file != null) {
//            MyFile profileImg = myFileService.saveImage(file);
//            user.setUserProfileImg(profileImg.getFileKey());
//        }
//
//        return user;
//
//    }

//    /**
//     * 회원 탈퇴
//     * @param withdrawUserRequest, loginId
//     * @return
//     */
//    @Override
//    @Transactional
//    public void withdrawUser(WithdrawUserRequest withdrawUserRequest, String loginId) {
//        User user = this.getUserProfile(loginId);
//
//        if (!user.getUserPassword().equals(sha256Util.encrypt(withdrawUserRequest.getPassword()))) throw new NotEqualPasswordException();
//        user.setUserState(UserConstants.eUser.eDELETE.getValue());
//    }
//
//    public User getUserByUserId(Long userId) {
//        return this.userRepository.findByUserId(userId).orElseThrow(NotFoundUserException::new);
//    }
//

}
