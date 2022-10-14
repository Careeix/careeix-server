package com.example.careeix.domain.user.repository;

import com.example.careeix.domain.user.entity.User;
import com.example.careeix.domain.user.entity.UserJob;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface UserJobRepository extends JpaRepository<UserJob , Long> {
    List<UserJob> findByUser_UserId(Long userId);

    @Transactional
    void deleteAllByUser(User user);
}
