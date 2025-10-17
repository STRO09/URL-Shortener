package repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import models.URLMapping;

public interface URLMappingRepository extends JpaRepository<URLMapping, Long> {

	Optional<URLMapping> findByshortcode(String shortcode);

	Optional<URLMapping> findBylongurl(String longurl);
	
    boolean existsByshortcode(String shortcode);

}
