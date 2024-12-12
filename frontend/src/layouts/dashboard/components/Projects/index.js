import { useState, useEffect } from "react";
import axios from "axios";

// @mui material components
import Card from "@mui/material/Card";
import Grid from "@mui/material/Grid";
import Pagination from "@mui/material/Pagination";
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import DialogActions from "@mui/material/DialogActions";
import FormControl from "@mui/material/FormControl";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";

// Material Dashboard 2 React components
import MDBox from "components/MDBox";
import MDTypography from "components/MDTypography";
import MDButton from "components/MDButton";

function Projects() {
  const [stocks, setStocks] = useState([]);
  const [openDialog, setOpenDialog] = useState(false);
  const [selectedStockId, setSelectedStockId] = useState("");
  const [quantity, setQuantity] = useState(1);
  const [maxPricePerShare, setMaxPricePerShare] = useState("");
  const [currentPage, setCurrentPage] = useState(1);
  const stocksPerPage = 8;

  // Retrieve userId from localStorage
  const userId = localStorage.getItem("userId");

  useEffect(() => {
    const fetchStocks = async () => {
      try {
        const response = await axios.get("http://localhost:8000/stocks");

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
      }
    };

    fetchStocks();
  }, []);

  const handleOpenDialog = (stockId) => {
    setSelectedStockId(stockId);
    setQuantity(1);
    setMaxPricePerShare("");
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
  };

  const handleConfirmRequest = async () => {
    try {
      await axios.post("http://localhost:8000/requests", {
        quantity,
        maxPricePerShare: parseFloat(maxPricePerShare),
        stockId: selectedStockId,
        userId,
      });
      alert("Request created successfully!");
      handleCloseDialog();
    } catch (err) {
      console.error("Error creating request:", err);
      alert("Failed to create request. Check console for details.");
    }
  };

  const indexOfLastStock = currentPage * stocksPerPage;
  const indexOfFirstStock = indexOfLastStock - stocksPerPage;
  const currentStocks = stocks.slice(indexOfFirstStock, indexOfLastStock);

  const handlePageChange = (event, value) => {
    setCurrentPage(value);
  };

  const handleQuantityChange = (event) => {
    const value = Math.max(1, parseInt(event.target.value, 10) || 1);
    setQuantity(value);
  };

  return (
    <Card>
      <MDBox display="flex" justifyContent="space-between" alignItems="center" p={3} mb={-5}>
        <MDTypography variant="h3" gutterBottom>
        Stocks
        </MDTypography>
      </MDBox>
      <MDBox p={2}>
        <Grid container spacing={3}>
          {currentStocks.map((stock) => (
            <Grid item xs={12} sm={6} md={3} key={stock.id}>
              <Card>
                <MDBox p={2}
                display="flex"
                flexDirection="column"
                justifyContent="center"
                alignItems="center"
                >
                  <MDBox mb={1}>
                    <MDTypography variant="h5" color="black" fontWeight="bold">
                      {stock.companyName}
                    </MDTypography>
                  </MDBox>
                  
                  <MDBox mb={1}>
                    <MDTypography variant="button" color="black" fontWeight="regular">
                      Available Stocks: {stock.offers.reduce((sum, offer) => sum + offer.quantity, 0)}
                    </MDTypography>
                  </MDBox>
                  <MDBox mb={1}>
                    <MDTypography variant="button" color="black" fontWeight="regular">
                      Stock Price: ${stock.offers[0]?.pricePerShare || "N/A"}
                    </MDTypography>
                  </MDBox>
                  <MDButton
                    variant="gradient"
                    color="info"
                    size="small"
                    onClick={() => handleOpenDialog(stock.id)}
                  >
                    Create Request
                  </MDButton>
                </MDBox>
              </Card>
            </Grid>
          ))}
        </Grid>
        <MDBox mt={3} display="flex" justifyContent="center">
          <Pagination
            count={Math.ceil(stocks.length / stocksPerPage)}
            page={currentPage}
            onChange={handlePageChange}
            color="primary"
          />
        </MDBox>
      </MDBox>

      <Dialog open={openDialog} onClose={handleCloseDialog} fullWidth maxWidth="sm">
        <DialogTitle
          style={{ textAlign: "center", padding: "16px" }}
        >
          <MDTypography variant="h5" fontWeight="bold">
            Create a Stock Request
          </MDTypography>
        </DialogTitle>
        <DialogContent style={{ padding: "24px" }}>
          <MDBox mb={3}>
            <TextField
              label="Stock Name"
              fullWidth
              variant="outlined"
              value={stocks.find((stock) => stock.id === selectedStockId)?.companyName || ""}
              InputProps={{ readOnly: true, style: { pointerEvents: "none" } }}
              style={{ backgroundColor: "#ffffff" }}
            />
          </MDBox>
          <MDBox display="flex" alignItems="center" justifyContent="center" mb={3}>
            <Button
              variant="outlined"
              onClick={() => setQuantity(Math.max(1, quantity - 1))}
              style={{
                minWidth: "50px",
                fontWeight: "bold",
                color: "#000",
              }}
            >
              -
            </Button>
            <TextField
              value={quantity}
              onChange={handleQuantityChange}
              type="number"
              inputProps={{ min: 1, style: { textAlign: "center", fontWeight: "bold" } }}
              style={{ width: "100px", margin: "0 10px" }}
            />
            <Button
              variant="outlined"
              onClick={() => setQuantity(quantity + 1)}
              style={{
                minWidth: "50px",
                fontWeight: "bold",
                color: "#000",
              }}
            >
              +
            </Button>
          </MDBox>
          <MDBox mb={3}>
            <TextField
              label="Max Price Per Share"
              fullWidth
              variant="outlined"
              type="number"
              value={maxPricePerShare}
              onChange={(e) => setMaxPricePerShare(e.target.value)}
              style={{ backgroundColor: "#ffffff" }}
            />
          </MDBox>
        </DialogContent>
        <DialogActions
          style={{ justifyContent: "space-between", padding: "16px 24px" }}
        >
          <Button
            onClick={handleCloseDialog}
            variant="outlined"
            style={{ padding: "8px 24px", color: "#d32f2f", borderColor: "#d32f2f" }}
          >
            Cancel
          </Button>
          <Button
            onClick={handleConfirmRequest}
            variant="contained"
            style={{ padding: "8px 24px", backgroundColor: "#2e7d32", color: "white" }}
            disabled={!selectedStockId || !maxPricePerShare}
          >
            Confirm
          </Button>
        </DialogActions>
      </Dialog>
    </Card>
  );
}

export default Projects;