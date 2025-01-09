import MDTypography from "components/MDTypography";

export default function data(transactions) {
  return {
    columns: [
      { Header: "Offer Company", accessor: "offerCompany", align: "left" },
      { Header: "Request Company", accessor: "requestCompany", align: "left" },
      { Header: "Quantity", accessor: "quantity", align: "left" },
      { Header: "Price/Share", accessor: "pricePerShare", align: "left" },
    ],

    rows: transactions.map((t) => ({
      offerCompany: (
        <MDTypography variant="button" fontWeight="medium" lineHeight={1}>
          {t.offer?.stock?.companyName || "N/A"}
        </MDTypography>
      ),
      requestCompany: (
        <MDTypography variant="button" fontWeight="medium" lineHeight={1}>
          {t.request?.stock?.companyName || "N/A"}
        </MDTypography>
      ),
      quantity: (
        <MDTypography variant="button" fontWeight="medium" lineHeight={1}>
          {t.quantity}
        </MDTypography>
      ),
      pricePerShare: (
        <MDTypography variant="button" fontWeight="medium" lineHeight={1}>
          {t.pricePerShare}
        </MDTypography>
      ),
    })),
  };
}
