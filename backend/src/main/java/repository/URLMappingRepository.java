package repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import models.URLMapping;

public interface URLMappingRepository extends JpaRepository<URLMapping, Long> {

	/** Look up a mapping by its shortcode — used for redirects and alias checks. */
	Optional<URLMapping> findByshortcode(String shortcode);

	/** Check whether a shortcode/alias is already taken. */
	boolean existsByshortcode(String shortcode);

	/**
	 * Returns any existing row for the given destination URL.
	 * Used to reuse an existing shortcode when no custom alias is requested.
	 */
	Optional<URLMapping> findFirstBylongurl(String longurl);

}
