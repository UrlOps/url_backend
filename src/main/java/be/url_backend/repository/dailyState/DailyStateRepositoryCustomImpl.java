package be.url_backend.repository.dailyState;

import be.url_backend.dto.response.DailyStatsDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static be.url_backend.domain.QDailyStats.dailyStats;

@Repository
@RequiredArgsConstructor
public class DailyStateRepositoryCustomImpl implements DailyStateRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<DailyStatsDto> searchDailyStats(Pageable pageable, LocalDate startDate, LocalDate endDate) {
        StringTemplate formattedDate = Expressions.stringTemplate(
                "FUNCTION('DATE_FORMAT', {0}, {1})",
                dailyStats.date,
                "%Y-%m-%d"
        );

        List<DailyStatsDto> content = queryFactory
                .select(Projections.constructor(DailyStatsDto.class,
                        dailyStats.id,
                        formattedDate,
                        dailyStats.clickCount
                ))
                .from(dailyStats)
                .where(
                        dateGoe(startDate),
                        dateLoe(endDate)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(dailyStats.date.desc())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(dailyStats.count())
                .from(dailyStats)
                .where(
                        dateGoe(startDate),
                        dateLoe(endDate)
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression dateGoe(LocalDate startDate) {
        return startDate != null ? dailyStats.date.goe(startDate) : null;
    }

    private BooleanExpression dateLoe(LocalDate endDate) {
        return endDate != null ? dailyStats.date.loe(endDate) : null;
    }
}
