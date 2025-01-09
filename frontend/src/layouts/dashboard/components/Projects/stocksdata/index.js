import MDTypography from "components/MDTypography";

export default function data(stocks) {
  return {
    columns: [
      { Header: "company", accessor: "companyName", align: "left" },
      { Header: "quantity owned", accessor: "quantityOwned", align: "left" },
      { Header: "price per stock", accessor: "pricePerShare", align: "left" },
    ],

    rows: stocks.map((item) => ({
      companyName: (
        <MDTypography variant="button" fontWeight="medium" lineHeight={1}>
          {item?.stock?.companyName || "Unknown Company"}
        </MDTypography>
      ),
      quantityOwned: (
        <MDTypography variant="caption" color="text" fontWeight="medium">
          {item?.quantity ?? 0}
        </MDTypography>
      ),
      pricePerShare: (
        <MDTypography variant="button" fontWeight="medium" lineHeight={1}>
          {item?.pricePerShare ?? "N/A"}
        </MDTypography>
      ),
    })),
  };
}
