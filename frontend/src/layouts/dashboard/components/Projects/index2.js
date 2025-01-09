import { useState, useEffect } from "react";
import axios from "axios";
import PropTypes from "prop-types";

import Card from "@mui/material/Card";
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import DialogActions from "@mui/material/DialogActions";
import Button from "@mui/material/Button";
import Snackbar from "@mui/material/Snackbar";
import Alert from "@mui/material/Alert";

import MDBox from "components/MDBox";
import MDTypography from "components/MDTypography";

import DataTable from "examples/Tables/DataTable";
import TextField from "@mui/material/TextField";

import data from "./reqdata"; 

function Requests({ stocksWithOffers }) {
  const [requests, setRequests] = useState([]);
  const [error, setError] = useState("");

  const [openDialog, setOpenDialog] = useState(false);
  const [selectedRequest, setSelectedRequest] = useState(null);

  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState("");

  const userId = localStorage.getItem("userId");
  const [balance, setBalance] = useState(null);

  const [openEditDialog, setOpenEditDialog] = useState(false);
  const [editRequest, setEditRequest] = useState(null);

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

  const sortedStocksWithOffers = stocksWithOffers.map((stock) => ({
    ...stock,
    offers: stock.offers.sort((a, b) => a.pricePerShare - b.pricePerShare),
  }));

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

    const matchingStock = sortedStocksWithOffers.find((s) => s.id === selectedRequest.stock.id);
    if (!matchingStock || !matchingStock.offers || matchingStock.offers.length === 0) {
      setSnackbarMessage("No matching offer found for this request");
      setSnackbarOpen(true);
      handleCloseDialog();
      return;
    }

    const matchingOffer = matchingStock.offers.find(
      (offer) => offer.quantity >= selectedRequest.quantity
    );

    if (!matchingOffer) {
      setSnackbarMessage("No offer with sufficient quantity found for this request");
      setSnackbarOpen(true);
      handleCloseDialog();
      return;
    }

    const requiredAmount = selectedRequest.quantity * matchingOffer.pricePerShare;

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

  const handleEditRequestClick = (request) => {
    if (!request) {
      setSnackbarMessage("Invalid request selected for editing.");
      setSnackbarOpen(true);
      return;
    }
    setEditRequest({ ...request });
    setOpenEditDialog(true);
  };
  

  const handleConfirmEdit = async () => {
    if (!editRequest) return;

    try {
      const response = await axios.patch(`http://localhost:8000/requests/${editRequest.id}`, {
        quantity: editRequest.quantity,
        maxPricePerShare: editRequest.maxPricePerShare,
      });

      setRequests((prevRequests) =>
        prevRequests.map((req) => (req.id === editRequest.id ? response.data : req))
      );

      setSnackbarMessage("Request updated successfully!");
      setSnackbarOpen(true);
    } catch (err) {
      console.error("Error updating request:", err);
      setSnackbarMessage("Failed to update request.");
      setSnackbarOpen(true);
    } finally {
      setOpenEditDialog(false);
      setEditRequest(null);
    }
  };

  const handleDeleteRequestClick = async (request) => {
    if (!window.confirm("Are you sure you want to delete this request?")) return;

    try {
      await axios.delete(`http://localhost:8000/requests/${request.id}`);
      setSnackbarMessage("Request deleted successfully.");
      setSnackbarOpen(true);

      setRequests((prev) => prev.filter((r) => r.id !== request.id));
    } catch (err) {
      console.error("Error deleting request:", err);
      setSnackbarMessage("Failed to delete request.");
      setSnackbarOpen(true);
    }
  };

  const handleSnackbarClose = (event, reason) => {
    if (reason === "clickaway") return;
    setSnackbarOpen(false);
    window.location.reload();
  };

  const pendingRequests = requests.filter((req) => !req.isFulfilled);

  const { columns, rows } = data(
    pendingRequests,
    handleFinishTransactionClick,
    sortedStocksWithOffers,
    balance,
    handleEditRequestClick,
    handleDeleteRequestClick
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

      {}
<Dialog open={openEditDialog} onClose={() => setOpenEditDialog(false)}>
  <DialogTitle>Edit Request</DialogTitle>
  <DialogContent>
    <MDTypography variant="body1" gutterBottom>
      Modify the request details below:
    </MDTypography>
    <MDBox display="flex" flexDirection="column" gap={2}>
      <TextField
        label="Quantity"
        type="number"
        value={editRequest?.quantity || ""}
        onChange={(e) =>
          setEditRequest((prev) => ({ ...prev, quantity: Number(e.target.value) }))
        }
        fullWidth
      />
      <TextField
        label="Max Price Per Share"
        type="number"
        value={editRequest?.maxPricePerShare || ""}
        onChange={(e) =>
          setEditRequest((prev) => ({ ...prev, maxPricePerShare: Number(e.target.value) }))
        }
        fullWidth
      />
    </MDBox>
  </DialogContent>
  <DialogActions>
    <Button onClick={() => setOpenEditDialog(false)} color="inherit">
      Cancel
    </Button>
    <Button
      onClick={handleConfirmEdit}
      variant="contained"
      color="primary"
      sx={{ color: "#fff" }}
    >
      Save Changes
    </Button>
  </DialogActions>
</Dialog>


      

      {}
      <Dialog open={openDialog} onClose={handleCloseDialog}>
        <DialogTitle>Confirm Transaction</DialogTitle>
        <DialogContent>
          <MDTypography variant="body1">
            Are you sure you want to finish this transaction?
          </MDTypography>
          {!selectedRequest || !sortedStocksWithOffers.find(
            (s) => s.id === selectedRequest.stock.id &&
            s.offers.some(
              (offer) =>
                offer.quantity >= selectedRequest.quantity &&
                offer.pricePerShare <= selectedRequest.maxPricePerShare
            )
          ) && (
            <MDTypography variant="body2" color="error" mt={2}>
              Cannot proceed due to insufficient offer quantity or price conditions.
            </MDTypography>
          )}
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog} color="inherit">
            Cancel
          </Button>
          <Button
            onClick={handleConfirmTransaction}
            variant="contained"
            color="primary"
            sx={{ color: "#fff" }}
            disabled={
              !selectedRequest ||
              !sortedStocksWithOffers.find(
                (s) => s.id === selectedRequest.stock.id &&
                s.offers.some(
                  (offer) =>
                    offer.quantity >= selectedRequest.quantity &&
                    offer.pricePerShare <= selectedRequest.maxPricePerShare
                )
              )
            }
          >
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
