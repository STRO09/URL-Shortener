export async function shortenUrl(longurl) {
  const response = await fetch("http://localhost:8080/api/shorten", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ longurl }),
  });

  if (!response.ok) throw new Error("Failed to shorten URL");

  return response.json();
}
