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
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import DialogActions from "@mui/material/DialogActions";
import Button from "@mui/material/Button";

// Material Dashboard 2 React components
import MDBox from "components/MDBox";
import MDTypography from "components/MDTypography";
import MDButton from "components/MDButton";

// Material Dashboard 2 React examples
import DataTable from "examples/Tables/DataTable";

// Data
import data from "./reqdata"; // Adjust the import path if necessary

function Requests() {
  const [requests, setRequests] = useState([]);
  const [error, setError] = useState("");

  // Retrieve userId from localStorage
  const userId = localStorage.getItem("userId");

  useEffect(() => {
    const fetchRequests = async () => {
      try {
        const response = await axios.get(`http://localhost:8000/users/${userId}/requests`);
        setRequests(response.data);
      } catch (err) {
        console.error("Error fetching requests:", err);
        setError("Failed to fetch requests");
      }
    };

    fetchRequests();
  }, [userId]);

  const { columns, rows } = data(requests);

  return (
    <Card>
      <MDBox display="flex" justifyContent="space-between" alignItems="center" p={3}>
        <MDTypography variant="h6" gutterBottom>
          Pending orders
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

export default Requests;
