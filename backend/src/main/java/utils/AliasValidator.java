package utils;

import java.util.regex.Pattern;

/**
 * Validates custom alias strings for the URL shortener.
 *
 * <p>Allowed characters: letters (a-z, A-Z), digits (0-9), hyphens (-), underscores (_).
 * Length must be between 1 and 15 characters inclusive.
 *
 * <p>These rules are enforced both here (backend) and mirrored in the React
 * frontend so that UI feedback is consistent with server-side rejection.
 */
public class AliasValidator {

    /** Max alias length — must stay in sync with URLMapping.shortcode column length */
    public static final int MAX_LENGTH = 15;

    /** Pattern: only alphanumeric, hyphen, underscore */
    private static final Pattern VALID_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]{1," + MAX_LENGTH + "}$");

    private AliasValidator() {
        // Utility class — no instantiation
    }

    /**
     * Returns {@code true} if the alias passes all format rules, {@code false} otherwise.
     * Does NOT check database availability.
     */
    public static boolean isValid(String alias) {
        return alias != null && VALID_PATTERN.matcher(alias).matches();
    }

    /**
     * Throws {@link IllegalArgumentException} with a descriptive message if the
     * alias is invalid. Use in service/controller layer to produce a 400 response.
     *
     * @param alias the alias to validate
     * @throws IllegalArgumentException if the alias is null, blank, too long, or
     *                                  contains disallowed characters
     */
    public static void validate(String alias) {
        if (alias == null || alias.trim().isEmpty()) {
            throw new IllegalArgumentException("Alias cannot be blank.");
        }
        if (alias.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                "Alias must be " + MAX_LENGTH + " characters or fewer.");
        }
        if (!VALID_PATTERN.matcher(alias).matches()) {
            throw new IllegalArgumentException(
                "Alias may only contain letters, digits, hyphens (-), and underscores (_).");
        }
    }
}

