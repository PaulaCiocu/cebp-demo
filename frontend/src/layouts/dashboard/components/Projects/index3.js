/**
=========================================================
* Material Dashboard 2 React - v2.2.0
=========================================================

* Product Page: https://www.creative-tim.com/product/material-dashboard-react
* Copyright 2023 Creative Tim
* Coded by www.creative-tim.com
 =========================================================

* The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
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

// Data
import data from "./transdata"; // Make sure this file is created and exports { columns, rows }

function Transactions() {
  const [transactions, setTransactions] = useState([]);
  const [error, setError] = useState("");

  // Retrieve userId from localStorage
  const userId = localStorage.getItem("userId");

  useEffect(() => {
    const fetchTransactions = async () => {
      try {
        const response = await axios.get(`http://localhost:8000/users/${userId}/transactions`);
        setTransactions(response.data);
      } catch (err) {
        console.error("Error fetching transactions:", err);
        setError("Failed to fetch transactions");
      }
    };

    if (userId) {
      fetchTransactions();
    } else {
      setError("No userId found in localStorage");
    }
  }, [userId]);

  const { columns, rows } = data(transactions);

  return (
    <Card>
      <MDBox display="flex" justifyContent="space-between" alignItems="center" p={3}>
        <MDTypography variant="h6" gutterBottom>
          My Transactions
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

export default Transactions;
