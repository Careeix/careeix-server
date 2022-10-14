package com.example.careeix.domain.user.repository;

import com.example.careeix.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    Optional<User> findByUserId(Long userId);


    Optional<User> findByUserEmail(String userEmail);

    Optional<User> findBySocialId(String socialId);

    Optional<User> findByUserNickName(String userNickName);

}


