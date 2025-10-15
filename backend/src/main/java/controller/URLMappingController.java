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
    public ResponseEntity<Map<String, String>>  createShortUrl(@RequestBody URLMapping request) {
        URLMapping mapping = service.createShortUrl(request.getLongurl());
        Map<String, String> response = new HashMap<>();
        response.put("shortcode", mapping.getShortcode());
        return ResponseEntity.ok(response);

    }

    // GET /api/{shortcode} — redirect to original
    @GetMapping("/{shortcode}")
    public ResponseEntity<?> redirect(@PathVariable String shortcode) {
        return service.getByShortCode(shortcode)
                .map(mapping -> ResponseEntity
                        .status(302)
                        .location(URI.create(mapping.getLongurl()))
                        .build())
                .orElse(ResponseEntity.notFound().build());
    }
}
