package com.example.careeix.domain.color.entity;

import com.example.careeix.config.BaseEntity;
import com.example.careeix.domain.user.entity.UserJob;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Color {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long colorId;
    private String colorCode;

}
