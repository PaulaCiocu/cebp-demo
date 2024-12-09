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

export default function data(requests) {
  return {
    columns: [
      { Header: "company", accessor: "companyName", width: "45%", align: "left" },
      { Header: "quantity", accessor: "quantity", width: "10%", align: "left" },
      { Header: "max price/share", accessor: "maxPricePerShare", width: "15%", align: "left" },
      { Header: "is fulfilled", accessor: "isFulfilled", width: "10%", align: "left" },
    ],

    rows: requests.map((req) => ({
      companyName: (
        <MDTypography variant="button" fontWeight="medium" lineHeight={1}>
          {req.stock.companyName}
        </MDTypography>
      ),
      quantity: (
        <MDTypography variant="caption" color="text" fontWeight="medium">
          {req.quantity}
        </MDTypography>
      ),
      maxPricePerShare: (
        <MDTypography variant="button" fontWeight="medium" lineHeight={1}>
          {req.maxPricePerShare}
        </MDTypography>
      ),
      isFulfilled: (
        <MDTypography variant="button" fontWeight="medium" lineHeight={1}>
          {req.isFulfilled ? "Yes" : "No"}
        </MDTypography>
      ),
    })),
  };
}
