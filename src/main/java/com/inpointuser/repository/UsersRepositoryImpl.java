package com.inpointuser.repository;

import com.inpointuser.domain.QUsers;
import com.inpointuser.service.dto.UsersDTO;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class UsersRepositoryImpl implements UsersRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QUsers qUsers = QUsers.users;

    public UsersRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UsersDTO> usersPage(Pageable pageable) {
        List<UsersDTO> usersList = queryFactory
                .select(Projections.constructor(UsersDTO.class,
                        qUsers.name, qUsers.profileViewCount, qUsers.createdAt
                ))
                .from(qUsers)
                .orderBy(SortCondition(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(qUsers.count())
                .from(qUsers)
                .fetchOne();

        long totalCount = Optional.ofNullable(total).orElse(0L);

        return new PageImpl<>(usersList, pageable, totalCount);
    }

    private OrderSpecifier<?> SortCondition(Pageable page) {
        if (!page.getSort().isEmpty()) {
            for (Sort.Order order : page.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                switch (order.getProperty()) {
                    case "name":
                        return new OrderSpecifier<>(direction, qUsers.name);
                    case "view":
                        return new OrderSpecifier<>(direction, qUsers.profileViewCount);
                    case "created":
                        return new OrderSpecifier<>(direction, qUsers.createdAt);
                }
            }
        }
        return null;
    }
}

