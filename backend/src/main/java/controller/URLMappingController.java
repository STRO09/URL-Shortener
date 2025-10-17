package controller;


import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import models.URLMapping;
import service.URLMappingService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class URLMappingController {

	private final URLMappingService service;

	public URLMappingController(URLMappingService service) {
		this.service = service;
	}

	// POST /api/shorten — to shorten a URL
	@PostMapping("/shorten")
	public ResponseEntity<Map<String, String>> createShortUrl(@RequestBody Map<String, String> request) {
	    String longUrl = request.get("longurl");
	    String customAlias = request.get("customalias"); // optional

	    try {
	        URLMapping mapping = service.createOrUpdateShortUrl(longUrl, customAlias);

	        Map<String, String> response = new HashMap<>();
	        response.put("shortcode", mapping.getShortcode());
	        return ResponseEntity.ok(response);
	    } catch (IllegalArgumentException e) {
	        // Custom alias already taken
	        Map<String, String> error = new HashMap<>();
	        error.put("error", e.getMessage());
	        return ResponseEntity.status(409).body(error); // 409 Conflict
	    }
	}


	// GET /api/{shortcode} — redirect to original
	@GetMapping("/{shortcode}")
	public ResponseEntity<?> redirect(@PathVariable String shortcode) {
		return service.getByShortCode(shortcode)
				.map(mapping -> ResponseEntity.status(302).location(URI.create(mapping.getLongurl())).build())
				.orElse(ResponseEntity.notFound().build());
	}
	
	
	
	
	@GetMapping("/check-alias/{alias}")
	public ResponseEntity<Map<String, Boolean>> checkAlias(@PathVariable String alias) {
		boolean exists = service.isAliasAvailable(alias);
	    Map<String, Boolean> response = new HashMap<>();
	    response.put("available", !exists);
	    return ResponseEntity.ok(response);

	}

}
