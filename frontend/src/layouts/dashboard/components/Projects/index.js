/**
=========================================================
* Material Dashboard 2 React - v2.2.0
=========================================================

* Product Page: https://www.creative-tim.com/product/material-dashboard-react
* Copyright 2023 Creative Tim (https://www.creative-tim.com)

Coded by www.creative-tim.com

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

// Material Dashboard 2 React components
import MDBox from "components/MDBox";
import MDTypography from "components/MDTypography";

// Material Dashboard 2 React examples
import DataTable from "examples/Tables/DataTable";

// Data
import data from "layouts/dashboard/components/Projects/data";

function Projects() {
  const [stockCount, setStockCount] = useState(0);
  const [error, setError] = useState("");
  const [menu, setMenu] = useState(null);

  const openMenu = ({ currentTarget }) => setMenu(currentTarget);
  const closeMenu = () => setMenu(null);

  const [stocks, setStocks] = useState([]);

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

  console.log(stocks);
  return (
    <Card>
      <MDBox display="flex" justifyContent="space-between" alignItems="center" p={3}>
        <MDBox>
          <MDTypography variant="h6" gutterBottom>
            Stocks
          </MDTypography>
        </MDBox>
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
    </Card>
  );
}

export default Projects;
