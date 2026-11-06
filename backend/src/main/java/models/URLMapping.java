package models;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.PrePersist;
import javax.persistence.Table;

@Entity
@Table(
    indexes = {
        @Index(name = "idx_url_mapping_longurl", columnList = "longurl")
    }
)
public class URLMapping {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// Not unique: same destination URL can have multiple shortcodes/aliases
	@Column(nullable = false)
	private String longurl;

	// Globally unique across all rows
	@Column(unique = true, nullable = false, length = 15)
	private String shortcode;

	@Column(nullable = false)
	private LocalDateTime createdAt = LocalDateTime.now();

	@Column(nullable = false)
	private LocalDateTime expiresAt;

	@Column
	private int clickCount = 0;

	// Explicit no-arg constructor required by JPA
	public URLMapping() {
	}

	@PrePersist
	public void setExpiresAt() {
		if (expiresAt == null && createdAt != null)
			expiresAt = this.createdAt.plusMonths(1);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLongurl() {
		return longurl;
	}

	public void setLongurl(String longurl) {
		this.longurl = longurl;
	}

	public String getShortcode() {
		return shortcode;
	}

	public void setShortcode(String shortcode) {
		this.shortcode = shortcode;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public int getClickCount() {
		return clickCount;
	}

	public void setClickCount(int clickCount) {
		this.clickCount = clickCount;
	}

}
