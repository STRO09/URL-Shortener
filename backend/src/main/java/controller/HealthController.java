package controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = { "http://localhost:5173", "https://url-shortener-and-modifier.netlify.app" })
public class HealthController {

	@GetMapping("/health")
	public String checkHealth() {
		return "Server is running!";
	}
}