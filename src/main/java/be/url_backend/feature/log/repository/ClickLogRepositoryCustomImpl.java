package be.url_backend.feature.log.repository;

import be.url_backend.feature.log.ClickLogResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static be.url_backend.feature.log.QClickLog.clickLog;
import static be.url_backend.feature.url.QUrlMapping.urlMapping;

@Repository
@RequiredArgsConstructor
public class ClickLogRepositoryCustomImpl implements ClickLogRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ClickLogResponseDto> searchClickLogs(Pageable pageable, String ipAddress, LocalDate startDate, LocalDate endDate) {
        List<ClickLogResponseDto> content = queryFactory
                .select(Projections.constructor(ClickLogResponseDto.class,
                        clickLog.id,
                        clickLog.userAgent,
                        clickLog.ipAddress,
                        clickLog.createdAt,
                        urlMapping.shortKey,
                        urlMapping.originalUrl
                ))
                .from(clickLog)
                .join(clickLog.urlMapping, urlMapping)
                .where(
                        ipAddressEq(ipAddress),
                        createdAtGoe(startDate),
                        createdAtLoe(endDate)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(clickLog.createdAt.desc())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(clickLog.count())
                .from(clickLog)
                .where(
                        ipAddressEq(ipAddress),
                        createdAtGoe(startDate),
                        createdAtLoe(endDate)
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression ipAddressEq(String ipAddress) {
        return (ipAddress != null && !ipAddress.trim().isEmpty()) ? clickLog.ipAddress.eq(ipAddress) : null;
    }

    private BooleanExpression createdAtGoe(LocalDate startDate) {
        return startDate != null ? clickLog.createdAt.goe(startDate.atStartOfDay()) : null;
    }

    private BooleanExpression createdAtLoe(LocalDate endDate) {
        return endDate != null ? clickLog.createdAt.loe(endDate.atTime(LocalTime.MAX)) : null;
    }
}
