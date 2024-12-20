/**
=========================================================
* Material Dashboard 2 React - v2.2.0
=========================================================

* Product Page: https://www.creative-tim.com/product/material-dashboard-react
* Copyright 2023 Creative Tim
* Coded by www.creative-tim.com
 =========================================================

*/

import { useState, useEffect } from "react";
import axios from "axios";
import PropTypes from "prop-types";

// @mui material components
import Card from "@mui/material/Card";
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import DialogActions from "@mui/material/DialogActions";
import Button from "@mui/material/Button";
import Snackbar from "@mui/material/Snackbar";
import Alert from "@mui/material/Alert";

// Material Dashboard 2 React components
import MDBox from "components/MDBox";
import MDTypography from "components/MDTypography";

// Material Dashboard 2 React examples
import DataTable from "examples/Tables/DataTable";

// Data
import data from "./reqdata"; // Adjust the import path if necessary

function Requests({ stocksWithOffers }) {
  const [requests, setRequests] = useState([]);
  const [error, setError] = useState("");

  const [openDialog, setOpenDialog] = useState(false);
  const [selectedRequest, setSelectedRequest] = useState(null);

  // Snackbar states
  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState("");

  const userId = localStorage.getItem("userId");
  const [balance, setBalance] = useState(null);

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

    const fetchUserBalance = async () => {
      if (!userId) return;
      try {
        const response = await axios.get(`http://localhost:8000/users/${userId}`);
        setBalance(response.data.balance);
      } catch (err) {
        console.error("Error fetching user balance:", err);
        setBalance(null);
      }
    };

    fetchRequests();
    fetchUserBalance();
  }, [userId]);

  const handleFinishTransactionClick = (request) => {
    setSelectedRequest(request);
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setSelectedRequest(null);
  };

  const handleConfirmTransaction = async () => {
    if (!selectedRequest) return;

    const matchingStock = stocksWithOffers.find((s) => s.id === selectedRequest.stock.id);
    if (!matchingStock || !matchingStock.offers || matchingStock.offers.length === 0) {
      setSnackbarMessage("No matching offer found for this request");
      setSnackbarOpen(true);
      handleCloseDialog();
      return;
    }

    const matchingOffer = matchingStock.offers[0];
    const requiredAmount = selectedRequest.quantity * selectedRequest.maxPricePerShare;

    // Double-check conditions before final transaction
    if (
      balance === null ||
      requiredAmount > balance ||
      selectedRequest.maxPricePerShare < matchingOffer.pricePerShare
    ) {
      setSnackbarMessage("Cannot complete transaction due to balance or price conditions.");
      setSnackbarOpen(true);
      handleCloseDialog();
      return;
    }

    try {
      await axios.post("http://localhost:8000/transactions", null, {
        params: {
          offerId: matchingOffer.id,
          requestId: selectedRequest.id,
        },
      });
      setSnackbarMessage("Transaction completed!");
      setSnackbarOpen(true);
    } catch (err) {
      console.error("Error finishing transaction:", err);
      setSnackbarMessage("Failed to complete transaction.");
      setSnackbarOpen(true);
    } finally {
      handleCloseDialog();
    }
  };

  const handleSnackbarClose = (event, reason) => {
    if (reason === "clickaway") return;
    setSnackbarOpen(false);
    window.location.reload();
  };

  // Filter out fulfilled requests so that only pending orders are shown
  const pendingRequests = requests.filter((req) => !req.isFulfilled);

  const { columns, rows } = data(
    pendingRequests,
    handleFinishTransactionClick,
    stocksWithOffers,
    balance
  );

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

      {/* Confirmation Dialog */}
      <Dialog open={openDialog} onClose={handleCloseDialog}>
        <DialogTitle>Confirm Transaction</DialogTitle>
        <DialogContent>
          <MDTypography variant="body1">
            Are you sure you want to finish this transaction?
          </MDTypography>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog} color="inherit">
            Cancel
          </Button>
          <Button onClick={handleConfirmTransaction} variant="contained" color="primary">
            Confirm
          </Button>
        </DialogActions>
      </Dialog>

      {/* Snackbar for toast messages */}
      <Snackbar
        open={snackbarOpen}
        autoHideDuration={5000}
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
    </Card>
  );
}

Requests.propTypes = {
  stocksWithOffers: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.number.isRequired,
      companyName: PropTypes.string,
      totalShares: PropTypes.number,
      remainingShares: PropTypes.number,
      offers: PropTypes.arrayOf(
        PropTypes.shape({
          id: PropTypes.number.isRequired,
          quantity: PropTypes.number.isRequired,
          pricePerShare: PropTypes.number.isRequired,
        })
      ),
    })
  ),
};

Requests.defaultProps = {
  stocksWithOffers: [],
};

export default Requests;
