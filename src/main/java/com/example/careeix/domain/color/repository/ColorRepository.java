package com.example.careeix.domain.color.repository;

import com.example.careeix.domain.color.entity.Color;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ColorRepository extends JpaRepository<Color, Long>{

    @Query(value = "SELECT * FROM color order by RAND() limit 1", nativeQuery = true)
    @NotNull
    List<Color> findAll();

}


