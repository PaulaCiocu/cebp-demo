import { useState, useEffect } from "react";
import axios from "axios";

import Card from "@mui/material/Card";

import MDBox from "components/MDBox";
import MDTypography from "components/MDTypography";

import DataTable from "examples/Tables/DataTable";

import data from "./stocksdata"; 

function MyStocks() {
  const [stocks, setStocks] = useState([]);
  const [error, setError] = useState("");

  const userId = localStorage.getItem("userId");

  useEffect(() => {
    const fetchStocks = async () => {
      try {
        const response = await axios.get(
          `http://localhost:8000/users/${userId}/transactions/fulfilled`
        );
        setStocks(response.data);
      } catch (err) {
        console.error("Error fetching user stocks:", err);
        setError("Failed to fetch your stocks");
      }
    };

    fetchStocks();
  }, [userId]);

  const { columns, rows } = data(stocks);

  return (
    <Card>
      <MDBox display="flex" justifyContent="space-between" alignItems="center" p={3}>
        <MDTypography variant="h6" gutterBottom>
          My Stocks
        </MDTypography>
      </MDBox>

      <MDBox>
        {error && (
          <MDTypography variant="button" color="error" p={2}>
            {error}
          </MDTypography>
        )}
        <DataTable
          table={{ columns, rows }}
          showTotalEntries={false}
          isSorted={false}
          noEndBorder
          entriesPerPage={false}
        />
      </MDBox>
    </Card>
  );
}

export default MyStocks;
