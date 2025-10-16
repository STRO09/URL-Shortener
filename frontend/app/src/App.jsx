import { useState } from "react";
import { shortenUrl } from "./api/url";

function App() {
  const [longurl, setLongUrl] = useState("");
  const [shortUrl, setShortUrl] = useState("");
  const [copied, setCopied] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setCopied(false);

    try {
      const data = await shortenUrl(longurl);
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
          <button
            type="submit"
            style={{
              background: "#fff",
              color: "#764ba2",
              border: "none",
              borderRadius: "10px",
              padding: "10px 20px",
              fontWeight: "bold",
              cursor: "pointer",
              transition: "0.3s",
            }}
            onMouseOver={(e) => (e.target.style.background = "#f1f1f1")}
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
          </div>
        )}
      </div>
    </div>
  );
}

export default App;
