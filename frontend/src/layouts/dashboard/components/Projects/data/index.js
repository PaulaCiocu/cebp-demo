/* eslint-disable react/prop-types */
/* eslint-disable react/function-component-definition */
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

// Material Dashboard 2 React components
import MDTypography from "components/MDTypography";

export default function data(stocks) {
  return {
    columns: [
      { Header: "company", accessor: "companyName", width: "45%", align: "left" },
      { Header: "total shares", accessor: "totalShares", width: "10%", align: "left" },
    ],

    rows: stocks.map((stock) => ({
      companyName: (
        <MDTypography variant="button" fontWeight="medium" lineHeight={1}>
          {stock.companyName}
        </MDTypography>
      ),
      totalShares: (
        <MDTypography variant="caption" color="text" fontWeight="medium">
          {stock.totalShares}
        </MDTypography>
      ),
    })),
  };
}
