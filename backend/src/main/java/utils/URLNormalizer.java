package utils;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Normalizes long URLs before storage and lookup to prevent duplicate entries
 * for URLs that are semantically identical.
 *
 * <p>Normalizations applied (in order):
 * <ol>
 *   <li>Trim leading/trailing whitespace</li>
 *   <li>Lowercase scheme (http/https) and host</li>
 *   <li>Strip default ports (80 for http, 443 for https)</li>
 *   <li>Remove trailing slash from the path (e.g. /path/ → /path)</li>
 *   <li>Keep path, query string, and port as-is otherwise</li>
 * </ol>
 *
 * <p>Fragment (#...) is intentionally dropped — fragments are client-side only
 * and have no meaning on the server.
 *
 * <p>Examples:
 * <pre>
 *   "https://Example.COM/"       → "https://example.com"
 *   "HTTPS://example.com/path/"  → "https://example.com/path"
 *   "http://example.com:80/"     → "http://example.com"
 *   "https://example.com:443"    → "https://example.com"
 *   "  https://example.com  "    → "https://example.com"
 * </pre>
 */
public class URLNormalizer {

    private URLNormalizer() {
        // Utility class — no instantiation
    }

    /**
     * Normalizes the given URL string.
     *
     * @param rawUrl the raw URL supplied by the user
     * @return the normalized URL string
     * @throws IllegalArgumentException if the URL is null, blank, or malformed
     */
    public static String normalize(String rawUrl) {
        if (rawUrl == null || rawUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("URL cannot be blank.");
        }

        String url = rawUrl.trim();

        // Prepend https:// if no scheme is present so java.net.URL can parse it
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://" + url;
        }

        URL parsed;
        try {
            parsed = new URL(url);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid URL: " + e.getMessage());
        }

        String scheme = parsed.getProtocol().toLowerCase();
        String host   = parsed.getHost().toLowerCase();
        int    port   = parsed.getPort();   // -1 when not explicitly set
        String path   = parsed.getPath();   // may be "" or "/"
        String query  = parsed.getQuery();  // null if no query string

        // Strip default ports so http://x:80 and http://x are treated the same
        if (("http".equals(scheme)  && port == 80)
         || ("https".equals(scheme) && port == 443)) {
            port = -1;
        }

        // Normalize path: collapse root slash, remove trailing slash
        if (path == null || path.equals("/")) {
            path = "";
        } else if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }

        // Rebuild
        StringBuilder normalized = new StringBuilder();
        normalized.append(scheme).append("://").append(host);
        if (port != -1) {
            normalized.append(":").append(port);
        }
        normalized.append(path);
        if (query != null) {
            normalized.append("?").append(query);
        }
        // Fragment intentionally omitted

        return normalized.toString();
    }
}

