export async function shortenUrl(longurl, customalias = "") {
  const response = await fetch("http://localhost:8080/api/shorten", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ longurl, customalias }),
  });

  if (!response.ok) throw new Error("Failed to shorten URL");

  return response.json();
}

export async function checkalias(customalias) {
  const response = await fetch(
    `http://localhost:8080/api/check-alias/${customalias}`
  );
  return response.json();
}
