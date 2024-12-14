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
import MDTypography from "components/MDTypography";

function DashboardStock() {
  const [stocks, setStocks] = useState([]);
  const [stockCount, setStockCount] = useState("...");
  const [error, setError] = useState(null);

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
    <DashboardLayout>
      <MDBox py={3}>
        <Grid container spacing={3}>
          <Grid item xs={12} md={6} lg={3}>
            <MDBox mb={1.5}>
              <ComplexStatisticsCard
                color="success"
                icon="store"
                title=<MDTypography variant="h5" fontWeight="regular">
                Stock Providers
              </MDTypography>
                count={stockCount}
                percentage={{
                  label: <MDTypography variant="body2" fontWeight="regular">
                  Stocks you can invest into
                </MDTypography>,
                }}
              />
            </MDBox>
          </Grid>
        </Grid>
        <MDBox>
          <Grid container spacing={3}>
            <Grid item xs={12} md={6} lg={12}>
              <Projects />
            </Grid>
          </Grid>
        </MDBox>
      </MDBox>
    </DashboardLayout>
  );
}

export default DashboardStock;

