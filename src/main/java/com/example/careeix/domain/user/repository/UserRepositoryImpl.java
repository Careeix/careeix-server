package com.example.careeix.domain.user.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;


public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public UserRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


//    @Override
//    public List<String> findJobNamesByUserId(Long userId) {
//        List<String> content = queryFactory
//                .select(
//
//                )
//                .from(user)
//                .leftJoin(userThema)
//                .on(user.userId.eq(userThema.user.userId))
//                .leftJoin(thema)
//                .on(diaryThema.thema.themaId.eq(thema.themaId))
//                .where(user.userId.eq(userId), diaryThema.diaryThemaId.isNotNull())
//                .fetch();
//        return content;
//    }
}
