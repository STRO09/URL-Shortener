package service;

import java.util.Optional;
import java.util.Random;

import models.URLMapping;
import repository.URLMappingRepository;

public class URLMappingService {

	private URLMappingRepository repository;
	private final String ALPHA_NUMERIC = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private final int SHORT_CODE_LENGTH = 6;
	
	
	public URLMappingService(URLMappingRepository repository) {
		super();
		this.repository = repository;
	}
	
	private String generateRandomCode() {
		Random random = new Random();
		StringBuilder builder = new StringBuilder();
		for(int i=0;i< SHORT_CODE_LENGTH;i++) {
			builder.append(ALPHA_NUMERIC.charAt(random.nextInt(ALPHA_NUMERIC.length())));
		}
		return builder.toString();
	}
	
	private String generateShortCode() {
		String code;
		do {
			code = generateRandomCode();
		}
		while(repository.findbyShortCode(code).isPresent());
		return code;
		
	}
	
	private URLMapping createShortUrl(String longurl) {
		String shortcode = generateRandomCode();
		URLMapping mapping = new URLMapping();
		mapping.setLongurl(longurl);
		mapping.setShortcode(shortcode);
		return repository.save(mapping);
	}
	
	public Optional<URLMapping> getByShortCode(String shortcode) {
		return repository.findbyShortCode(shortcode);
	}
	
	
}
