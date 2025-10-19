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
import utils.QRCodeGenerator;

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

	    try {
	        URLMapping mapping = service.createOrUpdateShortUrl(longUrl, customAlias);
	        String shortUrl = baseUrl + "/api/" + mapping.getShortcode();

	        Map<String, String> response = new HashMap<>();
	        response.put("shortcode", mapping.getShortcode());
	        
	        String qrCodeBase64 = "";
			try {
				qrCodeBase64 = QRCodeGenerator.generateQRCode(shortUrl, 200, 200);
			} catch (WriterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        response.put("qrCode", qrCodeBase64);
	        return ResponseEntity.ok(response);
	    } catch (IllegalArgumentException e) {
	        // Custom alias already taken
	        Map<String, String> error = new HashMap<>();
	        error.put("error", e.getMessage());
	        return ResponseEntity.status(409).body(error); // 409 Conflict
	    }
	}

	@GetMapping("/check-alias/{alias}")
	public ResponseEntity<Map<String, Boolean>> checkAlias(@PathVariable String alias) {
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
