package be.url_backend.repository;

import be.url_backend.domain.ClickLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClickLogRepository extends JpaRepository<ClickLog, Long>, ClickLogRepositoryCustom {

    @Query("SELECT c FROM ClickLog c JOIN FETCH c.urlMapping WHERE c.ipAddress = :ipAddress AND c.createdAt BETWEEN :start AND :end")
    List<ClickLog> findByIpAddressAndCreatedAtBetweenWithUrlMapping(
            @Param("ipAddress") String ipAddress,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    // 테스트 데이터를 위한 임시 메서드
    @Query("SELECT DISTINCT c.ipAddress FROM ClickLog c")
    List<String> findDistinctIpAddresses();
} 