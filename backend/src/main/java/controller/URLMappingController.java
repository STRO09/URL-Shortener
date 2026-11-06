package controller;


import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.google.zxing.WriterException;

import models.URLMapping;
import service.URLMappingService;
import utils.AliasValidator;
import utils.QRCodeGenerator;
import utils.URLNormalizer;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class URLMappingController {

	private final URLMappingService service;
	@Value("${app.base-url}")
    private String baseUrl;

	public URLMappingController(URLMappingService service) {
		this.service = service;
	}

	// POST /api/shorten — to shorten a URL
	@PostMapping("/shorten")
	public ResponseEntity<Map<String, String>> createShortUrl(@RequestBody Map<String, String> request) {
	    String longUrl = request.get("longurl");
	    String customAlias = request.get("customalias"); // optional

	    // ── Input validation (400 Bad Request) ──────────────────────────────────
	    Map<String, String> error = new HashMap<>();

	    // 1. Validate & normalize the URL first
	    try {
	        URLNormalizer.normalize(longUrl); // throws IllegalArgumentException if bad
	    } catch (IllegalArgumentException e) {
	        error.put("error", e.getMessage());
	        return ResponseEntity.badRequest().body(error);
	    }

	    // 2. Validate the alias format if one was supplied
	    boolean hasAlias = customAlias != null && !customAlias.trim().isEmpty();
	    if (hasAlias) {
	        try {
	            AliasValidator.validate(customAlias);
	        } catch (IllegalArgumentException e) {
	            error.put("error", e.getMessage());
	            return ResponseEntity.badRequest().body(error);
	        }
	    }
	    // ────────────────────────────────────────────────────────────────────────

	    try {
	        URLMapping mapping = service.createShortUrl(longUrl, customAlias);
	        String shortUrl = baseUrl + "/api/" + mapping.getShortcode();

	        Map<String, String> response = new HashMap<>();
	        response.put("shortcode", mapping.getShortcode());

	        String qrCodeBase64 = "";
			try {
				qrCodeBase64 = QRCodeGenerator.generateQRCode(shortUrl, 200, 200);
			} catch (WriterException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	        response.put("qrCode", qrCodeBase64);
	        return ResponseEntity.ok(response);

	    } catch (IllegalArgumentException e) {
	        // "Custom alias already taken" → 409 Conflict
	        error.put("error", e.getMessage());
	        return ResponseEntity.status(409).body(error);
	    }
	}

	/**
	 * GET /api/check-alias/{alias}
	 * Returns { "available": true/false } when the alias format is valid.
	 * Returns 400 with { "error": "..." } when the alias format is invalid —
	 * the frontend should show that inline rather than "Available/Taken".
	 */
	@GetMapping("/check-alias/{alias}")
	public ResponseEntity<?> checkAlias(@PathVariable String alias) {
	    // Validate format before hitting the DB
	    if (!AliasValidator.isValid(alias)) {
	        Map<String, String> error = new HashMap<>();
	        error.put("error",
	            "Alias may only contain letters, digits, hyphens (-), and underscores (_).");
	        return ResponseEntity.badRequest().body(error);
	    }

		boolean exists = service.AliasExistornot(alias);
	    Map<String, Boolean> response = new HashMap<>();
	    response.put("available", !exists);
	    return ResponseEntity.ok(response);
	}

	// GET /api/{shortcode} — redirect to original
	@GetMapping("/{shortcode}")
	public ResponseEntity<?> redirect(@PathVariable String shortcode) {
		return service.getByShortCode(shortcode)
				.map(mapping -> ResponseEntity.status(302).location(URI.create(mapping.getLongurl())).build())
				.orElse(ResponseEntity.notFound().build());
	}

}
