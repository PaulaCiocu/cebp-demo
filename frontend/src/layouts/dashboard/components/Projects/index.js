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
import InputLabel from "@mui/material/InputLabel";
import Select from "@mui/material/Select";
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
                "http://localhost:8000/stocks/${stock.id}/offers"
              );
              return { ...stock, offers: offersResponse.data };
            } catch (err) {
              console.error("Error fetching offers for stock ${stock.id}:", err);
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

  return (
    <Card>
      <MDBox display="flex" justifyContent="space-between" alignItems="center" p={3}>
        <MDTypography variant="h6" gutterBottom>
          Stocks
        </MDTypography>
      </MDBox>
      <MDBox p={2}>
        <Grid container spacing={3}>
          {currentStocks.map((stock) => (
            <Grid item xs={12} sm={6} md={3} key={stock.id}>
              <Card>
                <MDBox p={2}>
                  <MDBox mb={1}>
                    <MDTypography variant="button" fontWeight="medium">
                      {stock.companyName}
                    </MDTypography>
                  </MDBox>
                  <MDBox mb={1}>
                    <MDTypography variant="caption" color="text">
                      Total Shares: {stock.totalShares}
                    </MDTypography>
                  </MDBox>
                  <MDBox mb={1}>
                    <MDTypography variant="caption" color="text">
                      Offers: {stock.offers.reduce((sum, offer) => sum + offer.quantity, 0)}
                    </MDTypography>
                  </MDBox>
                  <MDBox mb={1}>
                    <MDTypography variant="caption" color="text">
                      Stock Price: ${stock.offers[0]?.pricePerShare || "N/A"}
                    </MDTypography>
                  </MDBox>
                  <MDButton
                    variant="gradient"
                    color="info"
                    fullWidth
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
        <DialogTitle>Create a Stock Request</DialogTitle>
        <DialogContent>
          <FormControl fullWidth margin="normal">
            <InputLabel>Stock</InputLabel>
            <Select value={selectedStockId} onChange={(e) => setSelectedStockId(e.target.value)}>
              {stocks.map((stock) => (
                <option key={stock.id} value={stock.id}>
                  {stock.companyName}
                </option>
              ))}
            </Select>
          </FormControl>

          <MDBox display="flex" alignItems="center" mt={2} mb={2}>
            <Button variant="outlined" onClick={() => setQuantity(Math.max(1, quantity - 1))}>
              -
            </Button>
            <MDTypography variant="h6" mx={2}>
              {quantity}
            </MDTypography>
            <Button variant="outlined" onClick={() => setQuantity(quantity + 1)}>
              +
            </Button>
          </MDBox>

          <TextField
            label="Max Price Per Share"
            fullWidth
            variant="outlined"
            type="number"
            value={maxPricePerShare}
            onChange={(e) => setMaxPricePerShare(e.target.value)}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog} color="inherit">
            Cancel
          </Button>
          <Button
            onClick={handleConfirmRequest}
            variant="contained"
            color="primary"
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
