package service;

import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;

import models.URLMapping;
import repository.URLMappingRepository;
import utils.URLNormalizer;

@Service
public class URLMappingService {

	private final URLMappingRepository repository;
	private final String ALPHA_NUMERIC = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private final int SHORT_CODE_LENGTH = 6;

	public URLMappingService(URLMappingRepository repository) {
		super();
		this.repository = repository;
	}

	private String generateRandomCode() {
		Random random = new Random();
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < SHORT_CODE_LENGTH; i++) {
			builder.append(ALPHA_NUMERIC.charAt(random.nextInt(ALPHA_NUMERIC.length())));
		}
		return builder.toString();
	}

	private String generateShortCode() {
		String code;
		do {
			code = generateRandomCode();
		} while (repository.existsByshortcode(code));
		return code;
	}

	/**
	 * Returns or creates a short URL mapping:
	 *
	 * <ul>
	 *   <li><b>No alias:</b> return any existing row for this URL (reuse its
	 *       shortcode). If none exists, generate a new one and persist it.</li>
	 *   <li><b>Custom alias:</b> always insert a new independent row so existing
	 *       shortcodes for the same URL are never touched or invalidated.</li>
	 * </ul>
	 *
	 * @param longurl     destination URL (normalized before use)
	 * @param customalias optional alias; {@code null} or blank → auto-generate
	 * @return the existing or newly saved {@link URLMapping}
	 * @throws IllegalArgumentException if the alias is already taken (→ 409)
	 */
	public URLMapping createShortUrl(String longurl, String customalias) {

		String normalizedUrl = URLNormalizer.normalize(longurl);

		// Treat blank alias the same as no alias
		String alias = (customalias != null && !customalias.trim().isEmpty())
				? customalias.trim()
				: null;

		if (alias != null) {
			// Custom alias: check availability then always insert a new row
			if (repository.existsByshortcode(alias)) {
				throw new IllegalArgumentException("Custom alias already taken!");
			}
			URLMapping mapping = new URLMapping();
			mapping.setLongurl(normalizedUrl);
			mapping.setShortcode(alias);
			return repository.save(mapping);

		} else {
			// No alias: reuse any existing shortcode for this URL, or create one
			Optional<URLMapping> existing = repository.findFirstBylongurl(normalizedUrl);
			if (existing.isPresent()) {
				return existing.get();
			}
			URLMapping mapping = new URLMapping();
			mapping.setLongurl(normalizedUrl);
			mapping.setShortcode(generateShortCode());
			return repository.save(mapping);
		}
	}

	public Optional<URLMapping> getByShortCode(String shortcode) {
		return repository.findByshortcode(shortcode);
	}

	public boolean AliasExistornot(String customalias) {
		return repository.existsByshortcode(customalias);
	}

}
