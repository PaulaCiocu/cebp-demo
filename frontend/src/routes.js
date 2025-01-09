import SignIn from "layouts/authentication/sign-in";
import SignUp from "layouts/authentication/sign-up";

import Icon from "@mui/material/Icon";
import LocalOfferIcon from "@mui/icons-material/LocalOffer";
import PublicRoute from "core/publicroute";
import PrivateRoute from "core/privateroute";
import DashboardStock from "layouts/dashboard";
import MyRequests from "layouts/requests";
import MyTransactions from "layouts/transactions";
import UserProfile from "layouts/profile";
import MyOffers from "layouts/offers";
import MyStocks from "layouts/stocks";

const routes = [
  {
    type: "collapse",
    hidden: true,
    name: "Sign Up",
    key: "sign-up",
    icon: <Icon fontSize="small">assignment</Icon>,
    route: "/authentication/sign-up",
    component: (
      <PublicRoute>
        <SignUp />
      </PublicRoute>
    ),
  },
  {
    type: "collapse",
    name: "Dashboard",
    key: "dashboard",
    icon: <Icon fontSize="small">dashboard</Icon>,
    route: "/dashboard",
    component: (
      <PrivateRoute>
        <DashboardStock />
      </PrivateRoute>
    ),
  },
  {
    type: "collapse",
    name: "Pending orders",
    key: "requests",
    icon: <Icon fontSize="small">pending</Icon>,
    route: "/requests",
    component: (
      <PrivateRoute>
        <MyRequests />
      </PrivateRoute>
    ),
  },
  {
    type: "collapse",
    name: "My Offers",
    key: "offers",
    icon: <LocalOfferIcon fontSize="small" />,
    route: "/offers",
    component: (
      <PrivateRoute>
        <MyOffers />
      </PrivateRoute>
    ),
  },
  {
    type: "collapse",
    name: "My Stocks",
    key: "my-stocks",
    icon: <Icon fontSize="small">inventory</Icon>,
    route: "/my-stocks",
    component: <MyStocks />,
  },
  {
    type: "collapse",
    name: "Transactions",
    key: "transactions",
    icon: <Icon fontSize="small">receipt_long</Icon>,
    route: "/transactions",
    component: <MyTransactions />,
  },
  {
    type: "collapse",
    name: "Profile",
    key: "profile",
    icon: <Icon fontSize="small">person</Icon>,
    route: "/profile",
    component: <UserProfile />,
  },
  {
    type: "collapse",
    name: "Sign In",
    hidden: true,
    key: "sign-in",
    icon: <Icon fontSize="small">login</Icon>,
    route: "/login",
    component: (
      <PublicRoute>
        <SignIn />
      </PublicRoute>
    ),
  },
];

export default routes;
