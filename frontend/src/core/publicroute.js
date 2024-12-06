import React from "react";
import { Navigate } from "react-router-dom";
import PropTypes from "prop-types"; // Make sure you've installed this with `npm install prop-types`

function PublicRoute({ children }) {
  const isAuthenticated = localStorage.getItem("isAuthenticated") === "true";
  return isAuthenticated ? <Navigate to="/dashboard" replace /> : children;
}

PublicRoute.propTypes = {
  children: PropTypes.node.isRequired,
};

export default PublicRoute;
