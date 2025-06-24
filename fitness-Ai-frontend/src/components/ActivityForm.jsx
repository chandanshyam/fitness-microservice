import {
  Box,
  Button,
  FormControl,
  InputLabel,
  MenuItem,
  Select,
  TextField,
} from "@mui/material";
import React, { useState } from "react";
import { addActivity } from "../services/api";

const ActivityForm = ({ onActivitiesAdded }) => {
  const [activity, setActivity] = useState({
    type: "RUNNING",
    duration: "",
    caloriesBurned: "",
    additionalMetrics: {},
  });

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await addActivity(activity);
      onActivitiesAdded();
      setActivity({
        type: "RUNNING",
        duration: "",
        caloriesBurned: "",
      });
    } catch (error) {
      if (error.response) {
        // Server responded with a status other than 2xx
        console.error("Server error:", error.response.data);
      } else if (error.request) {
        // Request was made but no response received
        console.error("Network error:", error.request);
      } else {
        // Something else happened
        console.error("Error:", error.message);
      }
    }
  };

  return (
    <Box
      sx={{
        background:
          "linear-gradient(135deg,rgb(255, 255, 255) 0%,rgb(243, 245, 247) 100%)",
        borderRadius: 3,
        p: 4,
        mb: 4,
        boxShadow: 3,
      }}
    >
      <Box component="form" onSubmit={handleSubmit} sx={{ mb: 0 }}>
        <FormControl fullWidth sx={{ mb: 2 }}>
          <InputLabel id="activity-type-label">Activity Type</InputLabel>
          <Select
            labelId="activity-type-label"
            id="activity-type"
            label="Activity Type"
            value={activity.type}
            onChange={(e) => setActivity({ ...activity, type: e.target.value })}
          >
            <MenuItem value="RUNNING">Running</MenuItem>
            <MenuItem value="WALKING">Walking</MenuItem>
            <MenuItem value="CYCLING">Cycling</MenuItem>
            <MenuItem value="SWIMMING">Swimming</MenuItem>
            <MenuItem value="YOGA">Yoga</MenuItem>
            <MenuItem value="STRENGTH_TRAINING">Strength Training</MenuItem>
            <MenuItem value="HIKING">Hiking</MenuItem>
            <MenuItem value="ROWING">Rowing</MenuItem>
            <MenuItem value="DANCING">Dancing</MenuItem>
            <MenuItem value="PILATES">Pilates</MenuItem>
          </Select>
        </FormControl>
        <TextField
          fullWidth
          label="Duration Minutes"
          type="number"
          sx={{ mb: 2 }}
          value={activity.duration}
          onChange={(e) =>
            setActivity({ ...activity, duration: e.target.value })
          }
        />
        <TextField
          fullWidth
          label="Calories Burned"
          type="number"
          sx={{ mb: 2 }}
          value={activity.caloriesBurned}
          onChange={(e) =>
            setActivity({ ...activity, caloriesBurned: e.target.value })
          }
        />
        <Button type="submit" variant="contained">
          Add Activity
        </Button>
      </Box>
    </Box>
  );
};

export default ActivityForm;
