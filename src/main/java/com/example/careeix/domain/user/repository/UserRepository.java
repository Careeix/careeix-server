package com.example.careeix.domain.user.repository;

import com.example.careeix.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    Optional<User> findByUserId(Long userId);


    List<User> findByUserJob(String userJob);

    Optional<User> findBySocialId(String socialId);

    Optional<User> findByUserNickName(String userNickName);

}


