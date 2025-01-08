/**
=========================================================
* Material Dashboard 2 React - v2.2.0
=========================================================

* Product Page: https://www.creative-tim.com/product/material-dashboard-react
* Copyright 2023
* Coded by www.creative-tim.com
 =========================================================
*/

import { useState, useEffect } from "react";
import axios from "axios";

// @mui material components
import Card from "@mui/material/Card";

// Material Dashboard 2 React components
import MDBox from "components/MDBox";
import MDTypography from "components/MDTypography";

// Material Dashboard 2 React examples
import DataTable from "examples/Tables/DataTable";

// Data builder for table
import data from "./stocksdata"; // Adjust path if needed

function MyStocks() {
  const [stocks, setStocks] = useState([]);
  const [error, setError] = useState("");

  // Retrieve userId from localStorage
  const userId = localStorage.getItem("userId");

  useEffect(() => {
    const fetchStocks = async () => {
      try {
        // You can fetch from an endpoint like:
        // GET /users/{userId}/transactions/fulfilled
        // or filter them from /users/{userId}/transactions if needed
        const response = await axios.get(
          `http://localhost:8000/users/${userId}/transactions/fulfilled`
        );

        // The response might be an array of transaction objects. 
        // If that's how you're returning them, set them directly:
        setStocks(response.data);
      } catch (err) {
        console.error("Error fetching user stocks:", err);
        setError("Failed to fetch your stocks");
      }
    };

    fetchStocks();
  }, [userId]);

  // pass your data to the table builder
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
