import axios from "axios";

const API_URL = "http://localhost:8080/api";

const api = axios.create({
  baseURL: API_URL,
});

//Used to add header to each request
api.interceptors.request.use((config) => {
  const userId = localStorage.getItem("userId");
  const token = localStorage.getItem("token");

  if (token) {
    config.headers["Authorization"] = `Bearer ${token}`;
  }

  if (userId) {
    config.headers["X-User-Id"] = userId;
  }
  return config;
});

let activitiesCache = null;

export const getCachedActivities = () => activitiesCache;

export const setCachedActivities = (activities) => {
  activitiesCache = activities;
};

export const getActivities = () => api.get("/activities");
export const addActivity = (activity) => api.post("/activities", activity);
export const getActivityDetail = (id) =>
  api.get(`/recommendations/activity/${id}`);
