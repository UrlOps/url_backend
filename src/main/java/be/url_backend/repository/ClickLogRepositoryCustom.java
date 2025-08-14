package be.url_backend.repository;

import be.url_backend.dto.response.DailyStatsDto;

import java.util.List;

public interface ClickLogRepositoryCustom {
    List<DailyStatsDto> findDailyClickStats();
} 