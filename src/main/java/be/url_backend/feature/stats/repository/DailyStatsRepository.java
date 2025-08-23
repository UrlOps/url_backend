package be.url_backend.feature.stats.repository;

import be.url_backend.feature.stats.DailyStats;
import be.url_backend.feature.url.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface DailyStatsRepository extends JpaRepository<DailyStats, Long>, DailyStateRepositoryCustom {
    Optional<DailyStats> findByUrlMappingAndDate(UrlMapping urlMapping, LocalDate date);
} 