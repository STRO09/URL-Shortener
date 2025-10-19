import axios from "axios";
const BASE_URL = import.meta.env.VITE_API_BASE_URL;
// const BASE_URL = "http://localhost:8080/api";
console.log("All envs:", import.meta.env);
console.log("BASE_URL =",BASE_URL);



const api = axios.create({
  baseURL: BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
});
export async function shortenUrl(longurl, customalias = "") {
  const response = await api.post('/shorten', { longurl, customalias });
  return response.data;
}

export async function checkalias(customalias) {
  const response = await api.get(`/check-alias/${customalias}`);
  return response.data;
}
