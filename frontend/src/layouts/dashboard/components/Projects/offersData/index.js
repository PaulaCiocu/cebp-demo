/* eslint-disable react/prop-types */
/* eslint-disable react/function-component-definition */
/**
=========================================================
* Material Dashboard 2 React - v2.2.0
=========================================================

* Product Page: https://www.creative-tim.com/product/material-dashboard-react
* (C) 2023 Creative Tim
 =========================================================
*/

import MDTypography from "components/MDTypography";

export default function data(offers) {
  return {
    columns: [
      { Header: "company", accessor: "companyName", width: "45%", align: "left" },
      { Header: "available stocks", accessor: "quantity", width: "10%", align: "left" },
      { Header: "max price/stock", accessor: "pricePerShare", width: "15%", align: "left" },
      { Header: "is fulfilled", accessor: "isFulfilled", width: "10%", align: "left" },
    ],

    rows: offers.map((off) => ({
      companyName: (
        <MDTypography variant="button" fontWeight="medium" lineHeight={1}>
          {off.stock.companyName}
        </MDTypography>
      ),
      quantity: (
        <MDTypography variant="caption" color="text" fontWeight="medium">
          {off.quantity}
        </MDTypography>
      ),
      pricePerShare: (
        <MDTypography variant="button" fontWeight="medium" lineHeight={1}>
          {off.pricePerShare}
        </MDTypography>
      ),
      isFulfilled: (
        <MDTypography variant="button" fontWeight="medium" lineHeight={1}>
          {off.isFulfilled ? "Yes" : "No"}
        </MDTypography>
      ),
    })),
  };
}
