import MDButton from "components/MDButton";
import Tooltip from "@mui/material/Tooltip";

export default function data(
  requests,
  handleFinishTransactionClick,
  stocksWithOffers,
  balance,
  handleEditRequestClick,
  handleDeleteRequestClick
) {
  return {
    columns: [
      { Header: "Company", accessor: "companyName", align: "left" },
      { Header: "Quantity", accessor: "quantity", align: "left" },
      { Header: "Max Price/Share", accessor: "maxPricePerShare", align: "left" },
      { Header: "Found Offer Share Price", accessor: "offerSharePrice", align: "left" },
      { Header: "Action", accessor: "action", align: "center" },
    ],

    rows: requests.map((req) => {
      // If the request was already fulfilled, we handle it differently, etc.
      if (req.isFulfilled) {
        return {
          companyName: req.stock.companyName,
          quantity: req.quantity,
          maxPricePerShare: req.maxPricePerShare,
          offerSharePrice: "N/A",
          action: "Fulfilled",
        };
      }

      // Find matching stock & offer
      const matchingStock = stocksWithOffers.find((s) => s.id === req.stock.id);

      // We'll track if Finish should be disabled separately:
      let finishDisabled = false;
      let tooltipMessage = "";

      let offerPriceDisplay = "N/A";
      if (!matchingStock || !matchingStock.offers || matchingStock.offers.length === 0) {
        // No offers available
        finishDisabled = true;
        tooltipMessage = "No matching offers available.";
      } else {
        const matchingOffer = matchingStock.offers[0];
        offerPriceDisplay = matchingOffer.pricePerShare;

        // Calculate effective price and required amount
        const effectivePrice = Math.min(req.maxPricePerShare, matchingOffer.pricePerShare);
        const requiredAmount = req.quantity * effectivePrice;

        if (balance === null) {
          finishDisabled = true;
          tooltipMessage = "User balance not available.";
        } else if (req.maxPricePerShare < matchingOffer.pricePerShare) {
          finishDisabled = true;
          tooltipMessage = "User's max price is lower than the offer price.";
        } else if (requiredAmount > balance) {
          finishDisabled = true;
          tooltipMessage = "Not enough balance.";
        }
      }

      // The Finish Transaction button
      const finishButton = finishDisabled ? (
        <Tooltip title={tooltipMessage} arrow>
          {/* Wrapping the button in a <span> to allow disabling with a tooltip */}
          <span>
            <MDButton variant="gradient" color="info" size="small" disabled>
              Finish Transaction
            </MDButton>
          </span>
        </Tooltip>
      ) : (
        <MDButton
          variant="gradient"
          color="info"
          size="small"
          onClick={() => handleFinishTransactionClick(req)}
        >
          Finish Transaction
        </MDButton>
      );

      // Edit button (always enabled)
      const editButton = (
        <MDButton
          variant="gradient"
          color="warning"
          size="small"
          onClick={() => handleEditRequestClick(req)}
        >
          Edit
        </MDButton>
      );

      // Delete button (always enabled)
      const deleteButton = (
        <MDButton
          variant="gradient"
          color="error"
          size="small"
          onClick={() => handleDeleteRequestClick(req)}
        >
          Delete
        </MDButton>
      );

      // Put all three buttons side by side
      return {
        companyName: req.stock.companyName,
        quantity: req.quantity,
        maxPricePerShare: req.maxPricePerShare,
        offerSharePrice: offerPriceDisplay,
        action: (
          <div style={{ display: "flex", gap: "0.5rem" }}>
            {finishButton}
            {editButton}
            {deleteButton}
          </div>
        ),
      };
    }),
  };
}

