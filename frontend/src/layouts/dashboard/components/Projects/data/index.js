import MDTypography from "../../../../../components/MDTypography";
import MDButton from "../../../../../components/MDButton";
import { useTheme } from "@mui/material/styles";

export default function data(stocks, handleOpenDialog) {
  return {
    columns: [
      { Header: "company", accessor: "companyName", width: "45%", align: "left" },
      { Header: "total stocks", accessor: "totalShares", width: "10%", align: "left" },
      { Header: "offers", accessor: "quantity", width: "10%", align: "left" },
      { Header: "stock price", accessor: "stockPrice", width: "10%", align: "left" },
      { Header: "Add request", accessor: "addRequest", width: "10%", align: "left" },
    ],
    rows: stocks.map((stock) => {
      const noOffers = !stock.offers || stock.offers.length === 0;
      const totalQuantity = noOffers
        ? 0
        : stock.offers.reduce((sum, offer) => sum + offer.quantity, 0);
      const pricePerShare = noOffers ? "N/A" : stock.offers[0]?.pricePerShare || "N/A";

      const textColor = noOffers ? "text.disabled" : "text";

      return {
        companyName: (
          <MDTypography variant="button" fontWeight="medium" lineHeight={1} color={textColor}>
            {stock.companyName}
          </MDTypography>
        ),
        totalShares: (
          <MDTypography variant="caption" color={textColor} fontWeight="medium">
            {stock.totalShares}
          </MDTypography>
        ),
        quantity: noOffers ? (
          <MDTypography variant="button" fontWeight="medium" lineHeight={1} color={textColor}>
            No offers available
          </MDTypography>
        ) : (
          <MDTypography variant="button" fontWeight="medium" lineHeight={1} color={textColor}>
            {totalQuantity}
          </MDTypography>
        ),
        stockPrice: noOffers ? (
          <MDTypography variant="button" fontWeight="medium" lineHeight={1} color={textColor}>
            N/A
          </MDTypography>
        ) : (
          <MDTypography variant="button" fontWeight="medium" lineHeight={1} color={textColor}>
            ${pricePerShare}
          </MDTypography>
        ),
        addRequest: noOffers ? (
          <MDTypography variant="button" fontWeight="medium" lineHeight={1} color="text.disabled">
            Cannot Request
          </MDTypography>
        ) : (
          <MDButton variant="gradient" color="info" onClick={() => handleOpenDialog(stock.id)}>
            Create Request
          </MDButton>
        ),
      };
    }),
  };
}
