package be.url_backend.repository;

import be.url_backend.domain.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.cache.annotation.Cacheable;

import java.util.Optional;

public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {
    @Cacheable(value = "url-mapping-cache", key = "#shortKey")
    Optional<UrlMapping> findByShortKey(String shortKey);
}
