import axios from "axios";
const BASE_URL = import.meta.env.VITE_API_BASE_URL;
// const BASE_URL = "http://localhost:8080/api";

const api = axios.create({
  baseURL: BASE_URL,
  timeout: 90000,
  headers: {
    "Content-Type": "application/json",
  },
});
export async function checkHealth() {
  const response = await api.get("/health");
  if (response.data !== "Server is running!") {
    throw new Error("Health check returned an unexpected response.");
  }
  return response.data;
}
