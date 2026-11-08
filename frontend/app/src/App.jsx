import { useState, useEffect, useRef, useCallback } from "react";
import { shortenUrl, checkalias } from "./api/url";
import { checkHealth } from "./api/health";

// ---------------------------------------------------------------------------
// Alias validation — mirrors the backend AliasValidator rules exactly:
//   • Letters (a-z, A-Z), digits (0-9), hyphens (-), underscores (_)
//   • 1 – 15 characters
// ---------------------------------------------------------------------------
const ALIAS_PATTERN = /^[a-zA-Z0-9_-]{1,15}$/;

function validateAliasFormat(value) {
  if (!value) return null; // empty = no alias, that's fine
  if (value.length > 15) return "Alias must be 15 characters or fewer.";
  if (!/^[a-zA-Z0-9_-]+$/.test(value))
    return "Only letters, digits, hyphens (-) and underscores (_) are allowed.";
  return null; // null = no format error
}

function isServerUnavailableError(error) {
  return !error?.response || error.response.status >= 500;
}

function ServerMarquee() {
  const message =
    "This API may take a little while to wake up after 15 minutes of inactivity. Thanks for your patience.";

  return (
    <div className="server-marquee" role="status">
      <div className="server-marquee__track">
        <span>{message}</span>
        <span aria-hidden="true">{message}</span>
      </div>
    </div>
  );
}

function App() {
  const [longurl, setLongUrl] = useState("");
  const [shortUrl, setShortUrl] = useState("");
  const [copied, setCopied] = useState(false);
  const [is404, setIs404] = useState(false);
  const [alias, setAlias] = useState("");
  // "available" | "taken" | "invalid" | null
  const [aliasStatus, setAliasStatus] = useState(null);
  const [aliasStatusMessage, setAliasStatusMessage] = useState("");
  const [qrCode, setQrCode] = useState("");
  const [serverStatus, setServerStatus] = useState("checking");

  // Persistent cache that survives re-renders (unlike const cache = {})
  const cacheRef = useRef({});
  const timeoutRef = useRef(null);

  const verifyServer = useCallback(async () => {
    setServerStatus("checking");
    try {
      await checkHealth();
      setServerStatus("available");
    } catch (err) {
      console.error("Health check failed:", err);
      setServerStatus("unavailable");
    }
  }, []);

  const checkAliasAvailability = (value) => {
    clearTimeout(timeoutRef.current);

    // ── Instant format check (no debounce needed) ──────────────────────────
    const formatError = validateAliasFormat(value);
    if (formatError) {
      setAliasStatus("invalid");
      setAliasStatusMessage(formatError);
      return; // don't hit the API for a bad format
    }

    if (!value) {
      setAliasStatus(null);
      setAliasStatusMessage("");
      return;
    }

    // ── Debounced availability check ───────────────────────────────────────
    timeoutRef.current = setTimeout(async () => {
      const cache = cacheRef.current;
      if (cache[value] !== undefined) {
        setAliasStatus(cache[value]);
        setAliasStatusMessage(
          cache[value] === "available" ? "Available ✅" : "Taken ❌",
        );
        return;
      }
      try {
        const data = await checkalias(value);
        const status = data.available ? "available" : "taken";
        setAliasStatus(status);
        setAliasStatusMessage(
          status === "available" ? "Available ✅" : "Taken ❌",
        );
        cache[value] = status;
      } catch (err) {
        console.error(err);
        if (isServerUnavailableError(err)) setServerStatus("unavailable");
      }
    }, 400);
  };

  const handleAliasChange = (value) => {
    setAlias(value);
    checkAliasAvailability(value);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setCopied(false);

    // Guard: don't submit if alias format is bad or alias is taken
    if (alias && (aliasStatus === "invalid" || aliasStatus === "taken")) return;

    try {
      const data = await shortenUrl(longurl, alias);
      const base =
        import.meta.env.VITE_API_BASE_URL || "http://localhost:8080/api";
      setShortUrl(`${base}/${data.shortcode}`);
      if (data.qrCode) setQrCode(data.qrCode);
    } catch (err) {
      const serverMessage = err?.response?.data?.error;
      console.error("Error:", err);
      if (isServerUnavailableError(err)) {
        setServerStatus("unavailable");
        return;
      }
      alert(
        serverMessage || "Something went wrong. Check console for details.",
      );
    }
  };

  const handleCopy = async () => {
    try {
      await navigator.clipboard.writeText(shortUrl);
      setCopied(true);
    } catch (err) {
      console.error("Failed to copy:", err);
    }
  };

  useEffect(() => {
    if (window.location.pathname !== "/") setIs404(true);
  }, []);

  useEffect(() => {
    verifyServer();
  }, [verifyServer]);

  if (is404) {
    return (
      <div style={{ textAlign: "center", marginTop: "50px" }}>
        <h1>404 - Page Not Found</h1>
        <button onClick={() => (window.location.href = "/")}>Go Home</button>
      </div>
    );
  }

  if (serverStatus !== "available") {
    const isChecking = serverStatus === "checking";

    return (
      <main className="app-shell">
        <ServerMarquee />
        <section className="server-status-card" aria-live="polite">
          <p className="server-status-code">{isChecking ? "..." : "503"}</p>
          <h1>{isChecking ? "Waking up the server" : "Server unavailable"}</h1>
          <p>
            {isChecking
              ? "The first request can take a little while while the server starts."
              : "We couldn't reach the URL shortener API. It may still be starting or temporarily unavailable."}
          </p>
          {!isChecking && (
            <button type="button" onClick={verifyServer}>
              Try again
            </button>
          )}
        </section>
      </main>
    );
  }

  // Derive button disabled state: disabled when alias has a format error or is taken
  const isSubmitDisabled =
    alias && (aliasStatus === "taken" || aliasStatus === "invalid");

  // Message colour: green for available, red for taken/invalid
  const messageColor = aliasStatus === "available" ? "lightgreen" : "tomato";

  return (
    <main className="app-shell">
      <ServerMarquee />
      <div
        style={{
          backgroundColor: "rgba(255, 255, 255, 0.1)",
          backdropFilter: "blur(10px)",
          borderRadius: "20px",
          padding: "40px 50px",
          boxShadow: "0 4px 20px rgba(0,0,0,0.2)",
          textAlign: "center",
          width: "90%",
          maxWidth: "500px",
        }}
      >
        <h1 style={{ marginBottom: "20px", letterSpacing: "1px" }}>
          🔗 URL SHORTENER
        </h1>

        <form
          onSubmit={handleSubmit}
          style={{
            display: "flex",
            gap: "15px",
            justifyContent: "center",
            flexDirection: "column",
            width: "100%",
          }}
        >
          <input
            type="url"
            placeholder="https://example.com"
            value={longurl}
            onChange={(e) => setLongUrl(e.target.value)}
            required
            style={{
              width: "100%",
              padding: "12px 15px",
              borderRadius: "10px",
              border: "none",
              outline: "none",
              fontSize: "15px",
            }}
          />
          <div
            style={{
              width: "100%",
              display: "flex",
              flexDirection: "column",
              gap: "5px",
            }}
          >
            <input
              type="text"
              placeholder="Custom alias (optional, only 15 characters) — letters, digits, - _"
              value={alias}
              maxLength={15}
              onChange={(e) => handleAliasChange(e.target.value)}
              style={{
                width: "100%",
                padding: "12px 15px",
                borderRadius: "10px",
                border:
                  alias && aliasStatus === "invalid"
                    ? "2px solid tomato"
                    : "none",
                outline: "none",
                fontSize: "15px",
              }}
            />
            {/* Status / validation message */}
            <p
              style={{
                color: messageColor,
                margin: 0,
                fontSize: "13px",
                minHeight: "18px",
              }}
            >
              {alias && aliasStatusMessage}
            </p>
          </div>
          <button
            type="submit"
            disabled={!!isSubmitDisabled}
            style={{
              width: "50%",
              marginLeft: "25%",
              background: "#fff",
              color: "#764ba2",
              border: "none",
              borderRadius: "10px",
              padding: "12px 0px",
              fontWeight: "bold",
              cursor: isSubmitDisabled ? "not-allowed" : "pointer",
              transition: "0.3s",
              opacity: isSubmitDisabled ? 0.5 : 1,
            }}
            onMouseOver={(e) => {
              if (!isSubmitDisabled) e.target.style.background = "#f1f1f1";
            }}
            onMouseOut={(e) => (e.target.style.background = "#fff")}
          >
            Shorten
          </button>
        </form>

        {shortUrl && (
          <div style={{ marginTop: "30px" }}>
            <p style={{ marginBottom: "10px", fontSize: "16px" }}>
              Short URL:{" "}
              <a
                href={shortUrl}
                target="_blank"
                rel="noopener noreferrer"
                style={{
                  color: "#ffea00",
                  textDecoration: "none",
                  fontWeight: "bold",
                }}
              >
                {shortUrl}
              </a>
            </p>

            <button
              onClick={handleCopy}
              style={{
                background: copied ? "#28a745" : "#ffea00",
                color: copied ? "white" : "#333",
                border: "none",
                borderRadius: "10px",
                padding: "8px 15px",
                cursor: "pointer",
                transition: "0.3s",
                fontWeight: "bold",
              }}
            >
              {copied ? "✅ Copied!" : "Copy Link"}
            </button>

            <button
              onClick={() => window.location.reload()}
              style={{
                marginLeft: "10px",
                background: "#6c63ff",
                color: "white",
                border: "none",
                borderRadius: "10px",
                padding: "8px 15px",
                cursor: "pointer",
                fontWeight: "bold",
              }}
            >
              Refresh
            </button>
          </div>
        )}
        {shortUrl && qrCode && (
          <div>
            <p>Scan the QR Code:</p>
            <img src={`data:image/png;base64,${qrCode}`} alt="QR Code" />
          </div>
        )}
      </div>
    </main>
  );
}

export default App;
