import { useState, useEffect } from "react";
import Grid from "@mui/material/Grid";
import MDBox from "components/MDBox";
import DashboardLayout from "examples/LayoutContainers/DashboardLayout";
import ComplexStatisticsCard from "examples/Cards/StatisticsCards/ComplexStatisticsCard";
import axios from "axios";
import MDTypography from "components/MDTypography";
import Projects from "layouts/dashboard/components/Projects";

function DashboardStock() {
  const [stocks, setStocks] = useState([]);
  const [stockCount, setStockCount] = useState("...");
  const [balance, setBalance] = useState("...");
  const [error, setError] = useState(null);

  const userId = localStorage.getItem("userId");

  useEffect(() => {
    const fetchStocks = async () => {
      try {
        const response = await axios.get("http://localhost:8000/stocks");
        setStockCount(response.data.length);
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

    const fetchUserBalance = async () => {
      if (!userId) return;
      try {
        const response = await axios.get(`http://localhost:8000/users/${userId}`);
        setBalance(response.data.balance);
      } catch (err) {
        console.error("Error fetching user balance:", err);
        setBalance("N/A");
      }
    };

    fetchStocks();
    fetchUserBalance();
  }, [userId]);

  return (
    <DashboardLayout>
      <MDBox py={3}>
        <Grid container spacing={3}>
          {/* Stock Providers Card */}
          <Grid item xs={12} md={6} lg={3}>
            <MDBox mb={1.5}>
              <ComplexStatisticsCard
                color="success"
                icon="store"
                title={
                  <MDTypography variant="h5" fontWeight="regular">
                    Stock Providers
                  </MDTypography>
                }
                count={stockCount}
                percentage={{
                  label: (
                    <MDTypography variant="body2" fontWeight="regular">
                      Stocks you can invest into
                    </MDTypography>
                  ),
                }}
              />
            </MDBox>
          </Grid>

          {/* User Balance Card */}
          <Grid item xs={12} md={6} lg={3}>
            <MDBox mb={1.5}>
              <ComplexStatisticsCard
                color="info"
                icon="account_balance_wallet"
                title={
                  <MDTypography variant="h5" fontWeight="regular">
                    My Balance
                  </MDTypography>
                }
                count={parseFloat(balance).toFixed(2)}
                percentage={{
                  label: (
                    <MDTypography variant="body2" fontWeight="regular">
                      Your available funds
                    </MDTypography>
                  ),
                }}
              />
            </MDBox>
          </Grid>
        </Grid>

        <MDBox mt={4}>
          <Grid container spacing={3}>
            <Grid item xs={12} md={6} lg={12}>
              {/* Pass stocks to Projects so it can handle greyed out logic */}
              <Projects stocks={stocks} />
            </Grid>
          </Grid>
        </MDBox>
      </MDBox>
    </DashboardLayout>
  );
}

export default DashboardStock;
