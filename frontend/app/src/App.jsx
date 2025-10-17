import { useState, useEffect } from "react";
import { shortenUrl, checkalias } from "./api/url";

function App() {
  const [longurl, setLongUrl] = useState("");
  const [shortUrl, setShortUrl] = useState("");
  const [copied, setCopied] = useState(false);
  const [is404, setIs404] = useState(false);
  const [alias, setAlias] = useState("");
  const [aliasStatus, setAliasStatus] = useState(null);
  const [aliasStatusMessage, setAliasStatusMessage] = useState("");

  // simple in-memory cache object
  const cache = {};

  const handleAliasChange = (value) => {
    setAlias(value);
    checkAliasAvailability(value);
  };

  let timeout;
  const checkAliasAvailability = (value) => {
    clearTimeout(timeout);
    timeout = setTimeout(async () => {
      if (!value) return;
      if (cache[value]) {
        setAliasStatus(cache[value]);
        setAliasStatusMessage(
          cache[value] === "available" ? "Available ✅" : "Taken ❌"
        );
        return;
      }
      try {
        const data = await checkalias(value);
        setAliasStatus(data.available ? "available" : "taken");
        setAliasStatusMessage(data.available ? "Available ✅" : "Taken ❌");
        cache[value] = data.available ? "available" : "taken"; // cache it
      } catch (err) {
        console.error(err);
      }
    }, 400); // debounce delay
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setCopied(false);

    try {
      const data = await shortenUrl(longurl, alias);
      const fullShortUrl = `http://localhost:8080/api/${data.shortcode}`;
      setShortUrl(fullShortUrl);
    } catch (err) {
      console.error("Error:", err);
      alert("Something went wrong. Check console for details.");
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

  if (is404) {
    return (
      <div style={{ textAlign: "center", marginTop: "50px" }}>
        <h1>404 - Page Not Found</h1>
        <button onClick={() => (window.location.href = "/")}>Go Home</button>
      </div>
    );
  }

  return (
    <div
      style={{
        minHeight: "100vh",
        display: "flex",
        flexDirection: "column",
        justifyContent: "center",
        alignItems: "center",
      }}
    >
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
          style={{ display: "flex", gap: "10px", justifyContent: "center" }}
        >
          <input
            type="url"
            placeholder="https://example.com"
            value={longurl}
            onChange={(e) => setLongUrl(e.target.value)}
            required
            style={{
              width: "100%",
              padding: "10px 15px",
              borderRadius: "10px",
              border: "none",
              outline: "none",
              fontSize: "15px",
            }}
          />
          <input
            type="text"
            placeholder="Custom alias (optional)"
            value={alias}
            onChange={(e) => handleAliasChange(e.target.value)}
          />
          <p style={{ color: aliasStatus === "available" ? "green" : "red" }}>
            {alias && aliasStatusMessage}
          </p>
          <button
            type="submit"
            disabled={alias && aliasStatus === "taken"}
            style={{
              background: "#fff",
              color: "#764ba2",
              border: "none",
              borderRadius: "10px",
              padding: "10px 20px",
              fontWeight: "bold",
              cursor:
                alias && aliasStatus === "taken" ? "not-allowed" : "pointer",
              transition: "0.3s",
              opacity: alias && aliasStatus === "taken" ? 0.5 : 1,
            }}
            onMouseOver={(e) => {
              if (!(alias && aliasStatus === "taken"))
                e.target.style.background = "#f1f1f1";
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
      </div>
    </div>
  );
}

export default App;
