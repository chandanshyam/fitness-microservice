import React, { useState, useEffect } from "react";
import { useParams } from "react-router";
import {
  getActivityDetail,
  getActivities,
  getCachedActivities,
} from "../services/api";
import { Box, Typography, Card, CardContent, Divider } from "@mui/material";
import { blue, green, orange, purple, red } from "@mui/material/colors";
import {
  Chart as ChartJS,
  RadialLinearScale,
  PointElement,
  LineElement,
  Filler,
  Tooltip,
  Legend,
} from "chart.js";
import { useTheme } from "@mui/material/styles";
import { Radar } from "react-chartjs-2";

ChartJS.register(
  RadialLinearScale,
  PointElement,
  LineElement,
  Filler,
  Tooltip,
  Legend
);

const ActivityDetail = () => {
  const { id } = useParams();
  const [activity, setActivity] = useState(null);
  const [recommendation, setRecommendation] = useState(null);
  const [fallbackActivity, setFallbackActivity] = useState(null);
  const theme = useTheme();
  const textColor =
    theme.palette.mode === "dark" ? "#90caf9" : theme.palette.text.primary;

  useEffect(() => {
    const fetchActivityDetails = async () => {
      try {
        const response = await getActivityDetail(id);
        setActivity(response.data);
        setRecommendation(response.data.recommendation);
        // If any key fields are missing, fetch the activity list for fallback
        if (
          !response.data.type ||
          !response.data.duration ||
          !response.data.caloriesBurned
        ) {
          let activities = getCachedActivities();
          if (!activities) {
            const listResponse = await getActivities();
            activities = listResponse.data;
          }
          const found = activities.find((a) => String(a.id) === String(id));
          if (found) setFallbackActivity(found);
        }
      } catch (error) {
        console.error(error);
      }
    };
    fetchActivityDetails();
  }, [id]);

  if (!activity) {
    return <Typography>...Loading</Typography>;
  }

  // Use fallback values if missing
  const type = activity.type || fallbackActivity?.type || "";
  const duration = activity.duration || fallbackActivity?.duration || "";
  const caloriesBurned =
    activity.caloriesBurned || fallbackActivity?.caloriesBurned || "";

  // Radar chart for workout split using suggestions
  let radarLabels = [];
  let radarScores = [];
  let chosenSuggestion = null;
  if (Array.isArray(activity.suggestions) && activity.suggestions.length > 0) {
    // Pick a random suggestion
    chosenSuggestion =
      activity.suggestions[
        Math.floor(Math.random() * activity.suggestions.length)
      ];
    // For demo, split suggestion string by common workout types (mock logic)
    // You can improve this logic if your suggestions are structured
    const categories = [
      "Cardio",
      "Strength",
      "Flexibility",
      "Balance",
      "Mobility",
    ];
    radarLabels = categories;
    // Assign random values for demo (or parse from suggestion if structured)
    radarScores = categories.map(() => Math.floor(Math.random() * 6));
  } else {
    radarLabels = ["Cardio", "Strength", "Flexibility", "Balance", "Mobility"];
    radarScores = [3, 2, 4, 1, 2];
  }

  const radarData = {
    labels: radarLabels,
    datasets: [
      {
        label: "Workout Split",
        data: radarScores,
        backgroundColor:
          theme.palette.mode === "dark"
            ? "rgba(144,202,249,0.2)"
            : "rgba(33, 150, 243, 0.2)",
        borderColor: theme.palette.mode === "dark" ? "#90caf9" : blue[500],
        pointBackgroundColor: orange[500],
        pointBorderColor: green[500],
        pointHoverBackgroundColor: red[500],
        pointHoverBorderColor: purple[500],
      },
    ],
  };

  const radarOptions = {
    scale: {
      ticks: { beginAtZero: true, min: 0, max: 5, stepSize: 1 },
      pointLabels: { font: { size: 16 } },
    },
    plugins: {
      legend: { display: false },
    },
    responsive: true,
    maintainAspectRatio: false,
  };

  return (
    <Box sx={{ maxWidth: 800, mx: "auto", p: 2 }}>
      <Card
        sx={{
          mb: 2,
          background: "linear-gradient(90deg, #e3ffe8 0%, #e3f0ff 100%)",
        }}
      >
        <CardContent sx={{ color: textColor }}>
          <Typography variant="h5" gutterBottom sx={{ color: textColor }}>
            Activity Details
          </Typography>
          <Typography sx={{ color: textColor }}>Type: {type}</Typography>
          <Typography sx={{ color: textColor }}>
            Duration: {duration} minutes
          </Typography>
          <Typography sx={{ color: textColor }}>
            Calories Burned: {caloriesBurned}
          </Typography>
          <Typography sx={{ color: textColor }}>
            Date: {new Date(activity.createdAt).toLocaleString()}
          </Typography>
        </CardContent>
      </Card>

      {/* Radar Chart for Workout Split */}
      <Card
        sx={{
          mb: 2,
          background:
            theme.palette.mode === "dark"
              ? "linear-gradient(90deg, #232526 0%, #414345 100%)"
              : "linear-gradient(90deg, #fceabb 0%, #f8b500 100%)",
        }}
      >
        <CardContent>
          <Typography variant="h6" gutterBottom sx={{ color: textColor }}>
            Suggested Workout Split
          </Typography>
          {chosenSuggestion && (
            <Typography
              paragraph
              sx={{ color: textColor, fontStyle: "italic" }}
            >
              Suggestion:{" "}
              {typeof chosenSuggestion === "object"
                ? chosenSuggestion.area || JSON.stringify(chosenSuggestion)
                : chosenSuggestion}
            </Typography>
          )}
          <Box sx={{ height: 350 }}>
            <Radar data={radarData} options={radarOptions} />
          </Box>
        </CardContent>
      </Card>

      {recommendation && (
        <Card
          sx={{
            background: "linear-gradient(90deg, #e3ffe8 0%, #e3f0ff 100%)",
          }}
        >
          <CardContent sx={{ color: textColor }}>
            <Typography variant="h5" gutterBottom sx={{ color: textColor }}>
              AI Recommendation{" "}
            </Typography>
            <Typography variant="h6" sx={{ color: textColor }}>
              Analysis
            </Typography>
            <Typography paragraph sx={{ color: textColor }}>
              {activity.recommendation}
            </Typography>

            <Divider sx={{ my: 2 }} />

            <Typography variant="h6" sx={{ color: textColor }}>
              Improvements
            </Typography>
            {activity?.improvements?.map((improvement, index) => (
              <Typography key={index} paragraph sx={{ color: textColor }}>
                •{" "}
                {typeof improvement === "object"
                  ? improvement.area
                  : improvement}
              </Typography>
            ))}

            <Divider sx={{ my: 2 }} />

            <Typography variant="h6" sx={{ color: textColor }}>
              Suggestions
            </Typography>
            {activity?.suggestions?.map((suggestion, index) => (
              <Typography key={index} paragraph sx={{ color: textColor }}>
                •{" "}
                {typeof suggestion === "object" ? suggestion.area : suggestion}
              </Typography>
            ))}

            <Divider sx={{ my: 2 }} />

            <Typography variant="h6" sx={{ color: textColor }}>
              Safety Guidelines
            </Typography>
            {activity?.safety?.map((safety, index) => (
              <Typography key={index} paragraph sx={{ color: textColor }}>
                • {typeof safety === "object" ? safety.area : safety}
              </Typography>
            ))}
          </CardContent>
        </Card>
      )}
    </Box>
  );
};

export default ActivityDetail;
