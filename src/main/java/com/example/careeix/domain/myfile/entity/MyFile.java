package com.example.careeix.domain.myfile.entity;

import com.example.careeix.config.BaseEntity;
import com.example.careeix.domain.user.entity.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyFile extends BaseEntity {

        @Setter(value = AccessLevel.NONE)
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long myFileId;
        private String fileKey;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_userId")
        private User user;


        private boolean isDeleted;

        public void setUser(User user) {
        this.user = user;
        user.getMyFiles().add(this);
    }



    public static MyFile toEntity(String key) {
     return MyFile.builder()
                .fileKey(key)
                .build();
    }
}
