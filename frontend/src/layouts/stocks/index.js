import { useEffect, useState, useCallback } from "react";
import axios from "axios";
import Grid from "@mui/material/Grid";
import MDBox from "components/MDBox";
import MDTypography from "components/MDTypography";
import DashboardLayout from "examples/LayoutContainers/DashboardLayout";
import Card from "@mui/material/Card";
import DataTable from "examples/Tables/DataTable";
import Button from "@mui/material/Button";
import Snackbar from "@mui/material/Snackbar";
import Alert from "@mui/material/Alert";

import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import DialogActions from "@mui/material/DialogActions";

function MyStocks() {
  const userId = localStorage.getItem("userId");
  const [fulfilledTx, setFulfilledTx] = useState([]);
  const [error, setError] = useState(null);

  // For "Create Offer" dialog
  const [createOfferDialogOpen, setCreateOfferDialogOpen] = useState(false);
  const [selectedTx, setSelectedTx] = useState(null);
  const [selectedStock, setSelectedStock] = useState(null);

  // Quantity & price for the new offer
  const [offerQuantity, setOfferQuantity] = useState(1);
  const [offerPrice, setOfferPrice] = useState(1);

  // Snackbar for success/error messages
  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState("");

  // 1) Create a reusable function to fetch the user’s transactions
  const fetchTransactions = useCallback(async () => {
    try {
      const { data } = await axios.get(`http://localhost:8000/users/${userId}/transactions`);

      // Filter only those transactions that are "fulfilled" on either the offer OR request side
      // AND have quantity >= 1
      const onlyFulfilled = data.filter((tx) => {
        const isOfferFulfilled = tx.offer?.isFulfilled === true;
        const isRequestFulfilled = tx.request?.isFulfilled === true;
        const hasQuantity = tx.quantity >= 1;

        return (isOfferFulfilled || isRequestFulfilled) && hasQuantity;
      });

      setFulfilledTx(onlyFulfilled);
    } catch (err) {
      console.error("Error fetching user transactions:", err);
      setError("Failed to fetch your stocks");
    }
  }, [userId]);

  // 2) On mount, fetch data
  useEffect(() => {
    fetchTransactions();
  }, [fetchTransactions]);

  // 3) Table columns
  const columns = [
    { Header: "Company", accessor: "companyName", align: "left" },
    { Header: "Quantity", accessor: "quantity", align: "left" },
    { Header: "Price Per Share", accessor: "pricePerShare", align: "left" },
    { Header: "Actions", accessor: "actions", align: "center" },
  ];

  // 4) Build table rows from the filtered transactions
  const rows = fulfilledTx.map((tx) => {
    // If the user is the one who made the offer, tx.offer?.stock is relevant
    // If the user is the one who made the request, maybe tx.request?.stock is relevant.
    // For simplicity, assume the user is always on the 'offer' side or they care about whichever stock is not null
    // If you only want one or the other, adjust accordingly.
    const stock = tx.offer?.stock || tx.request?.stock;
    return {
      companyName: stock?.companyName || "N/A",
      quantity: tx.quantity,
      pricePerShare: tx.pricePerShare,
      actions: (
        <Button
          variant="contained"
          color="primary"
          sx={{ color: "#fff" }} // ensure white text
          onClick={() => handleCreateOfferClick(tx)}
        >
          Create Offer
        </Button>
      ),
    };
  });

  // Handling "Create Offer"
  const handleCreateOfferClick = (tx) => {
    setSelectedTx(tx);
    const stockRef = tx.offer?.stock || tx.request?.stock;
    setSelectedStock(stockRef);

    setOfferQuantity(1);
    setOfferPrice(1);

    setCreateOfferDialogOpen(true);
  };

  // Close the "Create Offer" dialog
  const handleCloseCreateOfferDialog = () => {
    setCreateOfferDialogOpen(false);
    setSelectedTx(null);
    setSelectedStock(null);
    setOfferQuantity(1);
    setOfferPrice(1);
  };

  // Confirm creating the new offer
  const handleConfirmCreateOffer = async () => {
    if (!selectedStock) return;

    try {
      await axios.post("http://localhost:8000/offers", {
        quantity: offerQuantity,
        pricePerShare: offerPrice,
        stockId: selectedTx.offer?.stock?.id,
        userId: userId,
        transactionId: selectedTx.id, // <--- pass the transaction ID
        });

      setSnackbarMessage("Offer created successfully!");
      setSnackbarOpen(true);

      // If you want to see the updated quantity from the backend, re-fetch:
      await fetchTransactions();
    } catch (err) {
      console.error("Error creating offer:", err);
      setSnackbarMessage("Failed to create offer.");
      setSnackbarOpen(true);
    } finally {
      handleCloseCreateOfferDialog();
    }
  };

  // Close snackbar
  const handleSnackbarClose = (event, reason) => {
    if (reason === "clickaway") return;
    setSnackbarOpen(false);
  };

  return (
    <DashboardLayout>
      <MDBox py={3}>
        <Card>
          <MDBox p={2}>
            <MDTypography variant="h6" gutterBottom>
              My Stocks
            </MDTypography>
          </MDBox>

          {error && (
            <MDTypography variant="button" color="error" p={2}>
              {error}
            </MDTypography>
          )}

          <MDBox>
            <DataTable
              table={{ columns, rows }}
              showTotalEntries={false}
              isSorted={false}
              noEndBorder
              entriesPerPage={false}
            />
          </MDBox>
        </Card>
      </MDBox>

      {/* Dialog for creating an offer */}
      <Dialog open={createOfferDialogOpen} onClose={handleCloseCreateOfferDialog}>
        <DialogTitle>Create Offer</DialogTitle>
        <DialogContent>
          <MDTypography variant="body1" mb={2}>
            Create an offer for stock:{" "}
            <strong>{selectedStock?.companyName || "Unknown Stock"}</strong>
          </MDTypography>
          <div style={{ marginBottom: "1rem" }}>
            <label>Quantity: </label>
            <input
              type="number"
              min="1"
              value={offerQuantity}
              onChange={(e) => {
                const val = Number(e.target.value);
                setOfferQuantity(val < 1 ? 1 : val);
              }}
            />
          </div>
          <div style={{ marginBottom: "1rem" }}>
            <label>Price per share: </label>
            <input
              type="number"
              min="1"
              value={offerPrice}
              onChange={(e) => {
                const val = Number(e.target.value);
                setOfferPrice(val < 1 ? 1 : val);
              }}
            />
          </div>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseCreateOfferDialog} color="inherit">
            Cancel
          </Button>
          <Button
            variant="contained"
            color="primary"
            sx={{ color: "#fff" }}
            onClick={handleConfirmCreateOffer}
          >
            Confirm
          </Button>
        </DialogActions>
      </Dialog>

      {/* Snackbar */}
      <Snackbar
        open={snackbarOpen}
        autoHideDuration={4000}
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
    </DashboardLayout>
  );
}

export default MyStocks;
