package com.example.careeix.domain.user.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;
//import static com.example.careeix.domain.user.entity.QUser.user;
//import static com.example.careeix.domain.user.entity.QUserDetailJob.userDetailJob1;



public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public UserRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


//    @Override
//    public List<String> findJobNamesByUserId(Long userId) {
//        List<String> content = queryFactory
//                .select(
//                        userDetailJob1.userDetailJob
//                )
//                .from(userDetailJob1)
//                .leftJoin(userDetailJob1)
//                .on(user.userId.eq(userDetailJob1.userId))
//                .where(user.userId.eq(userId), diaryThema.diaryThemaId.isNotNull())
//                .fetch();
//        return content;
//    }
}
