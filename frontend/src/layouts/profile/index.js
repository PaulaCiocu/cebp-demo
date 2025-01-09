import { useState, useEffect } from "react";
import axios from "axios";

import Grid from "@mui/material/Grid";
import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import TextField from "@mui/material/TextField";
import Snackbar from "@mui/material/Snackbar";
import Alert from "@mui/material/Alert";
import Avatar from "@mui/material/Avatar";
import { styled } from "@mui/material/styles";

import DashboardLayout from "examples/LayoutContainers/DashboardLayout";
import MDBox from "components/MDBox";
import MDTypography from "components/MDTypography";
import MDButton from "components/MDButton";

import PersonIcon from "@mui/icons-material/Person";

const UserAvatar = styled(Avatar)(({ theme }) => ({
  backgroundColor: theme.palette.info.main,
  width: theme.spacing(7),
  height: theme.spacing(7),
  marginRight: theme.spacing(2),
}));

function UserProfile() {
  const userId = localStorage.getItem("userId");
  const [user, setUser] = useState({ name: "", email: "", balance: 0 });
  const [error, setError] = useState("");
  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState("");

  useEffect(() => {
    const fetchUser = async () => {
      if (!userId) {
        setError("No user ID found in localStorage");
        return;
      }
      try {
        const response = await axios.get(`http://localhost:8000/users/${userId}`);
        setUser(response.data);
      } catch (err) {
        console.error("Error fetching user:", err);
        setError("Failed to fetch user data");
      }
    };
    fetchUser();
  }, [userId]);

  const handleChange = (field) => (event) => {
    setUser((prev) => ({ ...prev, [field]: event.target.value }));
  };

  const handleSave = async () => {
    try {
      await axios.patch(`http://localhost:8000/users/${userId}`, {
        name: user.name,
        email: user.email,
        balance: parseFloat(user.balance),
      });
      setSnackbarMessage("Profile updated successfully!");
      setSnackbarOpen(true);
    } catch (err) {
      console.error("Error updating profile:", err);
      setSnackbarMessage("Failed to update profile.");
      setSnackbarOpen(true);
    }
  };

  const handleSnackbarClose = (event, reason) => {
    if (reason === "clickaway") return;
    setSnackbarOpen(false);
  };

  return (
    <DashboardLayout>
      <MDBox py={3}>
        <Grid container spacing={3} justifyContent="center">
          <Grid item xs={12} md={8} lg={6}>
            <Card sx={{ p: 3, backgroundColor: "#f8f9fa" }}>
              <CardContent>
                <MDBox display="flex" alignItems="center" mb={3}>
                  <UserAvatar>
                    <PersonIcon fontSize="large" />
                  </UserAvatar>
                  <MDTypography variant="h4" fontWeight="medium" color="text">
                    My Profile
                  </MDTypography>
                </MDBox>
                {error && (
                  <MDTypography variant="button" color="error" p={2}>
                    {error}
                  </MDTypography>
                )}
                {!error && (
                  <>
                    <MDBox mb={2}>
                      <MDTypography variant="overline" color="text" fontWeight="medium">
                        Name
                      </MDTypography>
                      <TextField
                        variant="outlined"
                        fullWidth
                        value={user.name}
                        onChange={handleChange("name")}
                        sx={{ mt: 1 }}
                      />
                    </MDBox>

                    <MDBox mb={2}>
                      <MDTypography variant="overline" color="text" fontWeight="medium">
                        Email
                      </MDTypography>
                      <TextField
                        variant="outlined"
                        fullWidth
                        value={user.email}
                        onChange={handleChange("email")}
                        sx={{ mt: 1 }}
                      />
                    </MDBox>

                    <MDBox mb={4}>
                      <MDTypography variant="overline" color="text" fontWeight="medium">
                        Balance
                      </MDTypography>
                      <TextField
                        variant="outlined"
                        fullWidth
                        type="number"
                        value={user.balance}
                        onChange={handleChange("balance")}
                        sx={{ mt: 1 }}
                      />
                    </MDBox>

                    <MDBox mt={3} textAlign="right">
                      <MDButton variant="gradient" color="info" onClick={handleSave}>
                        Save Changes
                      </MDButton>
                    </MDBox>
                  </>
                )}
              </CardContent>
            </Card>
          </Grid>
        </Grid>
      </MDBox>

      <Snackbar
        open={snackbarOpen}
        autoHideDuration={4000}
        onClose={handleSnackbarClose}
        anchorOrigin={{ vertical: "top", horizontal: "center" }}
      >
        <Alert
          onClose={handleSnackbarClose}
          severity="info"
          sx={{ width: "100%", fontSize: "1.1rem", fontWeight: "bold" }}
        >
          {snackbarMessage}
        </Alert>
      </Snackbar>
    </DashboardLayout>
  );
}

export default UserProfile;
