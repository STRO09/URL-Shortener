import { useState } from "react";

function App() {
  const [longurl, setLongUrl] = useState("");
  const [shortUrl, setShortUrl] = useState("");
  const [copied, setCopied] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setCopied(false);

    try {
      const response = await fetch("http://localhost:8080/api/shorten", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ longurl }),
      });

      if (!response.ok) throw new Error("Failed to shorten URL");

      const data = await response.json();

      // Construct the full redirect link
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
    <div style={{ textAlign: "center", marginTop: "50px" }}>
      <h1>URL SHORTENER</h1>

      <form onSubmit={handleSubmit}>
        <input
          type="url"
          id="website"
          name="longurl"
          placeholder="https://example.com"
          value={longurl}
          onChange={(e) => setLongUrl(e.target.value)}
          required
          style={{ width: "300px", padding: "8px" }}
        />
        <button type="submit" style={{ marginLeft: "10px" }}>
          Submit
        </button>
      </form>

      {shortUrl && (
        <div style={{ marginTop: "20px" }}>
          <p>
            Short URL:{" "}
            <a
              href={shortUrl}
              target="_blank"
              rel="noopener noreferrer"
              style={{ color: "#007bff", textDecoration: "none" }}
            >
              {shortUrl}
            </a>
          </p>

          <button onClick={handleCopy}>
            {copied ? "✅ Copied!" : "Copy"}
          </button>
        </div>
      )}
    </div>
  );
}

export default App;
