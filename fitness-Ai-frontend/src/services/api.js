import axios from "axios";

const API_URL = "http://localhost/8080/api";

const api = axios.create({
  baseURL: API_URL,
});

//Used to add header to each request
api.interceptors.request.use((config) => {
  const userId = localStorage.getItem("userId");
  const token = localStorage.getItem("token");

  if (token) {
    config.headers["Authorization"] = `Bearer ${token} `;
  }

  if (userId) {
    config.headers["X-User-ID "] = userId;
  }
});

// export const getActivities = () => api.get("/activity");
export const addActivitiy = (activity) => api.post("/activities", activity);
export const getActivityDetail = () =>
  api.get("/recommendations/activity/${id}");
