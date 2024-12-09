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
import Icon from "@mui/material/Icon";
import Menu from "@mui/material/Menu";
import MenuItem from "@mui/material/MenuItem";
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

// Material Dashboard 2 React examples
import DataTable from "examples/Tables/DataTable";

// Data
import data from "layouts/dashboard/components/Projects/data";

function Projects() {
  const [stockCount, setStockCount] = useState(0);
  const [error, setError] = useState("");
  const [menu, setMenu] = useState(null);
  const [stocks, setStocks] = useState([]);

  const [openDialog, setOpenDialog] = useState(false);
  const [selectedStockId, setSelectedStockId] = useState("");
  const [quantity, setQuantity] = useState(1);
  const [maxPricePerShare, setMaxPricePerShare] = useState("");

  // Retrieve userId from localStorage
  const userId = localStorage.getItem("userId");

  const openMenu = ({ currentTarget }) => setMenu(currentTarget);
  const closeMenu = () => setMenu(null);

  useEffect(() => {
    const fetchStocks = async () => {
      try {
        const response = await axios.get("http://localhost:8000/stocks");
        setStockCount(response.data.length);

        // Fetch offers for each stock
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

    fetchStocks();
  }, []);

  const { columns, rows } = data(stocks);

  const handleOpenDialog = () => {
    // Reset dialog state when opening
    if (stocks.length > 0) {
      setSelectedStockId(stocks[0].id);
    }
    setQuantity(1);
    setMaxPricePerShare("");
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
  };

  const handleStockChange = (event) => {
    setSelectedStockId(event.target.value);
    setQuantity(1); // Reset quantity when changing stock
  };

  const incrementQuantity = () => {
    const selectedStock = stocks.find((s) => s.id === selectedStockId);
    const maxQuantity = selectedStock ? selectedStock.offers.length : 0;
    if (quantity < maxQuantity) {
      setQuantity(quantity + 1);
    }
  };

  const decrementQuantity = () => {
    if (quantity > 1) {
      setQuantity(quantity - 1);
    }
  };

  const handleConfirmRequest = async () => {
    const selectedStock = stocks.find((s) => s.id === selectedStockId);
    if (!selectedStock) {
      console.error("No stock selected!");
      return;
    }

    const maxQuantity = selectedStock.offers.length;
    if (quantity > maxQuantity) {
      alert(`Quantity cannot exceed ${maxQuantity}`);
      return;
    }

    try {
      await axios.post("http://localhost:8000/requests", {
        quantity,
        maxPricePerShare: parseFloat(maxPricePerShare),
        stockId: selectedStockId,
        userId, // Now we are sending the userId from localStorage
      });
      alert("Request created successfully!");
      handleCloseDialog();
    } catch (err) {
      console.error("Error creating request:", err);
      alert("Failed to create request. Check console for details.");
    }
  };

  return (
    <Card>
      <MDBox display="flex" justifyContent="space-between" alignItems="center" p={3}>
        <MDTypography variant="h6" gutterBottom>
          Stocks
        </MDTypography>
        <MDButton variant="gradient" color="info" onClick={handleOpenDialog}>
          Create Request
        </MDButton>
      </MDBox>
      <MDBox>
        <DataTable
          table={{ columns, rows }}
          showTotalEntries={false}
          isSorted={false}
          noEndBorder
          entriesPerPage={false}
        />
      </MDBox>

      <Dialog open={openDialog} onClose={handleCloseDialog} fullWidth maxWidth="sm">
        <DialogTitle>Create a Stock Request</DialogTitle>
        <DialogContent>
          {stocks.length === 0 ? (
            <MDTypography variant="caption" color="text">
              No stocks available
            </MDTypography>
          ) : (
            <>
              <FormControl fullWidth margin="normal">
                <InputLabel>Stock</InputLabel>
                <Select value={selectedStockId} label="Stock" onChange={handleStockChange}>
                  {stocks.map((stock) => (
                    <MenuItem key={stock.id} value={stock.id}>
                      {stock.companyName || `Stock #${stock.id}`}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>

              <MDBox display="flex" alignItems="center" mt={2} mb={2}>
                <Button variant="outlined" onClick={decrementQuantity} disabled={quantity <= 1}>
                  -
                </Button>
                <MDTypography variant="h6" mx={2}>
                  {quantity}
                </MDTypography>
                <Button variant="outlined" onClick={incrementQuantity}>
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
            </>
          )}
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog} color="inherit">
            Cancel
          </Button>
          <Button
            onClick={handleConfirmRequest}
            variant="contained"
            color="primary"
            disabled={stocks.length === 0}
          >
            Confirm
          </Button>
        </DialogActions>
      </Dialog>
    </Card>
  );
}

export default Projects;
