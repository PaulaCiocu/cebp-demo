import { useState, useEffect } from "react";
import Grid from "@mui/material/Grid";
import MDBox from "components/MDBox";
import DashboardLayout from "examples/LayoutContainers/DashboardLayout";
import ComplexStatisticsCard from "examples/Cards/StatisticsCards/ComplexStatisticsCard";
import axios from "axios";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import Projects from "layouts/dashboard/components/Projects";
import Requests from "layouts/dashboard/components/Projects/index2";
import Offers from "layouts/dashboard/components/Projects/indexOffer";
import MDTypography from "components/MDTypography";

function MyOffers() {
  const [stocks, setStocks] = useState([]);
  const [stockCount, setStockCount] = useState("...");
  const [error, setError] = useState(null);

  const [requests, setRequests] = useState([]);

  // Retrieve userId from localStorage
  const userId = localStorage.getItem("userId");

  useEffect(() => {
    const fetchRequests = async () => {
      try {
        const response = await axios.get(`http://localhost:8000/users/${userId}/offers`);
        setRequests(response.data);
      } catch (err) {
        console.error("Error fetching offers:", err);
        setError("Failed to fetch offers");
      }
    };

    fetchRequests();
  }, [userId]);

  useEffect(() => {
    const fetchStocks = async () => {
      try {
        const response = await axios.get("http://localhost:8000/offers");
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
              console.error(`Error fetching offers for stock ${stock.id}:`, err);
              return { ...stock, offers: [] };
            }
          })
        );
        setStocks(stocksWithOffers);
      } catch (err) {
        console.error("Error fetching stocks:", err);
        setError("Failed to fetch stocks");
      }
    };

    fetchStocks();
  }, []);
  return (
    <>
      {error && requests.length === 0 && (
        <DashboardLayout>
          <MDBox py={3}>
            <MDBox>
              <Grid container spacing={3}>
                <Grid item xs={12} md={6} lg={12}>
                  <MDTypography variant="button" color="text" p={2}>
                    No offers found.
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
                  <Offers />
                </Grid>
              </Grid>
            </MDBox>
          </MDBox>
        </DashboardLayout>
      )}
    </>
  );
}

export default MyOffers;
