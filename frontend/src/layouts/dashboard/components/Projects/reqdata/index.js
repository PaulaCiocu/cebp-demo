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
import MDButton from "components/MDButton";
import Tooltip from "@mui/material/Tooltip";

export default function data(requests, handleFinishTransactionClick, stocksWithOffers, balance) {
  return {
    columns: [
      { Header: "Company", accessor: "companyName", align: "left" },
      { Header: "Quantity", accessor: "quantity", align: "left" },
      { Header: "Max Price/Share", accessor: "maxPricePerShare", align: "left" },
      { Header: "Found Offer Share Price", accessor: "offerSharePrice", align: "left" },
      { Header: "Action", accessor: "action", align: "center" },
    ],

    rows: requests.map((req) => {
      if (req.isFulfilled) {
        // Fulfilled requests handling if not filtered out
        return {
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
          offerSharePrice: (
            <MDTypography variant="button" fontWeight="medium" lineHeight={1} color="text.disabled">
              N/A
            </MDTypography>
          ),
          action: (
            <MDTypography variant="button" color="success" fontWeight="medium" lineHeight={1}>
              Fulfilled
            </MDTypography>
          ),
        };
      }

      const matchingStock = stocksWithOffers.find((s) => s.id === req.stock.id);
      let disabled = false;
      let tooltipMessage = "";
      let offerPriceDisplay = "N/A";

      if (!matchingStock || !matchingStock.offers || matchingStock.offers.length === 0) {
        // No offers available
        disabled = true;
        tooltipMessage = "No matching offers available.";
      } else {
        const matchingOffer = matchingStock.offers[0];
        offerPriceDisplay = matchingOffer.pricePerShare;

        // Calculate effective price and required amount
        const effectivePrice = Math.min(req.maxPricePerShare, matchingOffer.pricePerShare);
        const requiredAmount = req.quantity * effectivePrice;

        if (balance === null) {
          disabled = true;
          tooltipMessage = "User balance not available.";
        } else if (req.maxPricePerShare < matchingOffer.pricePerShare) {
          disabled = true;
          tooltipMessage = "User's max price is lower than the offer price.";
        } else if (requiredAmount > balance) {
          disabled = true;
          tooltipMessage = "Not enough balance.";
        }
      }

      const actionButton = (
        <MDButton
          variant="gradient"
          color="info"
          size="small"
          onClick={() => handleFinishTransactionClick(req)}
          disabled={disabled}
        >
          Finish Transaction
        </MDButton>
      );

      return {
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
        offerSharePrice: (
          <MDTypography variant="button" fontWeight="medium" lineHeight={1}>
            {offerPriceDisplay}
          </MDTypography>
        ),
        action: disabled ? (
          <Tooltip title={tooltipMessage} arrow>
            <span>{actionButton}</span>
          </Tooltip>
        ) : (
          actionButton
        ),
      };
    }),
  };
}
