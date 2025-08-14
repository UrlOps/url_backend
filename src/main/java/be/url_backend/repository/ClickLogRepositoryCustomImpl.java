package be.url_backend.repository;

import be.url_backend.domain.QClickLog;
import be.url_backend.dto.response.DailyStatsDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ClickLogRepositoryCustomImpl implements ClickLogRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<DailyStatsDto> findDailyClickStats() {
        QClickLog clickLog = QClickLog.clickLog;

        StringTemplate formattedDate = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})",
                clickLog.createdAt,
                "%Y-%m-%d");

        return queryFactory
                .select(Projections.constructor(DailyStatsDto.class,
                        clickLog.urlMapping.id,
                        clickLog.createdAt.as("date"),
                        clickLog.count().as("clickCount")
                ))
                .from(clickLog)
                .groupBy(clickLog.urlMapping.id, formattedDate)
                .orderBy(formattedDate.desc())
                .fetch();
    }
} 