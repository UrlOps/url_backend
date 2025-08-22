package be.url_backend.repository.clickLog;

import be.url_backend.domain.ClickLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClickLogRepository extends JpaRepository<ClickLog, Long>, ClickLogRepositoryCustom {

    // 테스트 데이터를 위한 임시 메서드
    @Query("SELECT DISTINCT c.ipAddress FROM ClickLog c")
    List<String> findDistinctIpAddresses();
} 