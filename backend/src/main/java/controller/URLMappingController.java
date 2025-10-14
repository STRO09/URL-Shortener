package controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import models.URLMapping;
import service.URLMappingService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class URLMappingController {

    private final URLMappingService service;

    public URLMappingController(URLMappingService service) {
        this.service = service;
    }

    // POST /api/shorten — to shorten a URL
    @PostMapping("/shorten")
    public ResponseEntity<URLMapping> createShortUrl(@RequestBody URLMapping request) {
        URLMapping mapping = service.createShortUrl(request.getLongurl());
        return ResponseEntity.ok(mapping);
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
