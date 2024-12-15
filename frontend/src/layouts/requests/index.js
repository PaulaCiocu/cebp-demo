import { useState, useEffect } from "react";
import Grid from "@mui/material/Grid";
import MDBox from "components/MDBox";
import DashboardLayout from "examples/LayoutContainers/DashboardLayout";
import axios from "axios";

import Requests from "layouts/dashboard/components/Projects/index2";
import MDTypography from "components/MDTypography";

function MyRequests() {
  const [stocks, setStocks] = useState([]);
  const [stockCount, setStockCount] = useState("...");
  const [error, setError] = useState(null);
  const [rqerror, setRqerror] = useState("");

  const [requests, setRequests] = useState([]);

  const userId = localStorage.getItem("userId");

  useEffect(() => {
    const fetchStocks = async () => {
      try {
        const response = await axios.get("http://localhost:8000/stocks");
        setStockCount(response.data.length);

        // Fetch offers for each stock
        const stocksWithOffers = await Promise.all(
          response.data.map(async (stock) => {
            try {
              const offersResponse = await axios.get(
                `http://localhost:8000/stocks/${stock.id}/offers`
              );
              return { ...stock, offers: offersResponse.data };
            } catch (err) {
              console.error(`You do not have any pending orders ${stock.id}:`, err);
              return { ...stock, offers: [] };
            }
          })
        );
        setStocks(stocksWithOffers);
      } catch (err) {
        console.error("Error fetching stocks:", err);
        setError("");
      }
    };

    fetchStocks();

    const fetchRequests = async () => {
      try {
        const response = await axios.get(`http://localhost:8000/users/${userId}/requests`);
        setRequests(response.data);
      } catch (err) {
        console.error("Error fetching requests:", err);
      }
    };

    if (userId) {
      fetchRequests();
    } else {
      setRqerror("No userId found in localStorage");
    }
  }, [userId]);

  return (
    <>
      {error && (
        <DashboardLayout>
          <MDBox py={3}>
            <MDBox>
              <Grid container spacing={3}>
                <Grid item xs={12} md={6} lg={12}>
                  <MDTypography variant="button" color="error" p={2}>
                    No requests found.
                  </MDTypography>
                </Grid>
              </Grid>
            </MDBox>
          </MDBox>
        </DashboardLayout>
      )}
      {!rqerror && requests.length === 0 && (
        <DashboardLayout>
          <MDBox py={3}>
            <MDBox>
              <Grid container spacing={3}>
                <Grid item xs={12} md={6} lg={12}>
                  <MDTypography variant="button" color="text" p={2}>
                    No requests found.
                  </MDTypography>
                </Grid>
              </Grid>
            </MDBox>
          </MDBox>
        </DashboardLayout>
      )}
      {requests.length > 0 && (
        <DashboardLayout>
          <MDBox py={3}>
            <MDBox>
              <Grid container spacing={3}>
                <Grid item xs={12} md={6} lg={12}>
                  {/* Pass the fetched stocks (with offers) to Requests component */}
                  <Requests stocksWithOffers={stocks} />
                </Grid>
              </Grid>
            </MDBox>
          </MDBox>
        </DashboardLayout>
      )}
    </>
  );
}

export default MyRequests;
