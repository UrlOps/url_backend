package be.url_backend.repository;

import be.url_backend.domain.ClickLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClickLogRepository extends JpaRepository<ClickLog, Long>, ClickLogRepositoryCustom {

} 