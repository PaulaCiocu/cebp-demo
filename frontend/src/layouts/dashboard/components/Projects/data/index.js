import MDTypography from "../../../../../components/MDTypography";
import MDButton from "../../../../../components/MDButton";

export default function data(stocks, handleOpenDialog) {
  return {
    columns: [
      { Header: "company", accessor: "companyName", width: "45%", align: "left" },
      { Header: "total stocks", accessor: "totalShares", width: "10%", align: "left" },
      { Header: "offers", accessor: "quantity", width: "10%", align: "left" },
      { Header: "stock price", accessor: "stockPrice", width: "10%", align: "left" },
      { Header: "Add request", accessor: "addRequest", width: "10%", align: "left" },
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
      quantity: (
        <MDTypography variant="button" fontWeight="medium" lineHeight={1}>
          {stock.offers.reduce((sum, offer) => sum + offer.quantity, 0)}
        </MDTypography>
      ),
      stockPrice: (
        <MDTypography variant="button" fontWeight="medium" lineHeight={1}>
          ${stock.offers[0]?.pricePerShare || "N/A"} {/* Show the first price per share */}
        </MDTypography>
      ),
      addRequest: (
        <MDButton variant="gradient" color="info" onClick={() => handleOpenDialog(stock.id)}>
          Create Request
        </MDButton>
      ),
    })),
  };
}
