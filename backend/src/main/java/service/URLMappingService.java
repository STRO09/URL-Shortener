package service;

import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;

import models.URLMapping;
import repository.URLMappingRepository;

@Service
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
		while(repository.findByshortcode(code).isPresent());
		return code;
		
	}
	
	public URLMapping createShortUrl(String longurl) {
		
        Optional<URLMapping> existing = repository.findBylongurl(longurl);
        if (existing.isPresent()) {
            return existing.get();
        }
		
		String shortcode = generateShortCode();
		URLMapping mapping = new URLMapping();
		mapping.setLongurl(longurl);
		mapping.setShortcode(shortcode);
		return repository.save(mapping);
	}
	
	public Optional<URLMapping> getByShortCode(String shortcode) {
		return repository.findByshortcode(shortcode);
	}
	
	public boolean isAliasAvailable(String customalias) {
		return repository.existsByshortcode(customalias);
	}
	
	
}
