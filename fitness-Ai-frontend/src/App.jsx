import {
  Box,
  Button,
  ThemeProvider,
  createTheme,
  CssBaseline,
  IconButton,
  CardContent,
  Typography,
} from "@mui/material";
import { useContext, useEffect, useState } from "react";
import { AuthContext } from "react-oauth2-code-pkce";
import { useDispatch } from "react-redux";
import {
  BrowserRouter as Router,
  Navigate,
  Route,
  Routes,
  useLocation,
} from "react-router";
import { setCredentials } from "./store/authSlice";
import ActivityForm from "./components/ActivityForm";
import ActivityList from "./components/ActivityList";
import ActivityDetail from "./components/ActivityDetail";
import { Brightness4, Brightness7, Logout } from "@mui/icons-material";
import { useTheme } from "@mui/material/styles";

const ActivitiesPage = () => (
  <Box
    component="section"
    sx={{
      p: 2,
      border: "1px dashed grey",
      position: "relative",
      background: "linear-gradient(135deg, #e0c3fc 0%, #8ec5fc 100%)",
      boxShadow: 0,
      m: 2,
    }}
  >
    <Box sx={{ pt: 7 }}>
      <ActivityForm onActivitiesAdded={() => {}} />
      <ActivityList />
    </Box>
  </Box>
);

function App() {
  const { token, tokenData, login, logOut, isAuthenticated } =
    useContext(AuthContext);
  const dispatch = useDispatch();
  const [authReady, setauthReady] = useState(false);
  const [mode, setMode] = useState("light");
  const theme = createTheme({
    palette: {
      mode,
    },
  });

  useEffect(() => {
    if (token) {
      dispatch(setCredentials({ token, user: tokenData }));
      setauthReady(true);
    }
  }, [token, tokenData, dispatch]);

  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Box
        sx={{
          position: "absolute",
          top: 16,
          right: 16,
          zIndex: 10,
          display: "flex",
          gap: 1,
        }}
      >
        <IconButton
          onClick={() => setMode(mode === "light" ? "dark" : "light")}
          color="inherit"
        >
          {mode === "dark" ? <Brightness7 /> : <Brightness4 />}
        </IconButton>
        {token && (
          <IconButton
            onClick={() => {
              logOut();
            }}
            color="inherit"
            title="Logout"
          >
            <Logout />
          </IconButton>
        )}
      </Box>
      <Router>
        {!token ? (
          <Box
            sx={{
              minHeight: "100vh",
              minWidth: "100vw",
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
              background: "linear-gradient(135deg, #e0c3fc 0%, #8ec5fc 100%)",
            }}
          >
            <Box
              component={CardContent}
              sx={{
                bgcolor: "background.paper",
                boxShadow: 6,
                borderRadius: 3,
                p: 5,
                minWidth: 350,
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                gap: 3,
              }}
            >
              <Typography
                variant="h4"
                fontWeight="bold"
                color="primary"
                gutterBottom
              >
                Welcome to Fitness AI
              </Typography>
              <Typography variant="body1" color="text.secondary" align="center">
                Track your workouts, get AI-powered recommendations, and level
                up your fitness journey!
              </Typography>
              <Button
                variant="contained"
                size="large"
                sx={{
                  mt: 2,
                  px: 5,
                  py: 1.5,
                  fontWeight: "bold",
                  fontSize: "1.1rem",
                }}
                onClick={() => {
                  login();
                }}
              >
                {" "}
                Login
              </Button>
            </Box>
          </Box>
        ) : (
          <div>
            {
              /* <pre>{JSON.stringify(tokenData, null, 2)}</pre> */
              <Routes>
                <Route path="/activities" element={<ActivitiesPage />} />
                <Route path="/activities/:id" element={<ActivityDetail />} />
                <Route
                  path="/"
                  element={
                    token ? (
                      <Navigate to="/activities" replace />
                    ) : (
                      <div>Welcome! Please Login</div>
                    )
                  }
                />
              </Routes>
            }
          </div>
        )}
      </Router>
    </ThemeProvider>
  );
}

export default App;
