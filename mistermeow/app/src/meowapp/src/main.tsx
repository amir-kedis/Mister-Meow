import React from "react";
import ReactDOM from "react-dom/client";
import "./index.css";
import { QueryClient, QueryClientProvider } from "react-query";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import { ThemeProvider } from "./contexts/themeContext";
import Home from "./Home.tsx";
import SRP, { loader as SRPLoader } from "./SRP.tsx";
import ErrorPage from "./error-page.tsx";

const queryClient = new QueryClient();

const BrowserRouter = createBrowserRouter([
  {
    path: "/",
    element: <Home />,
    errorElement: <ErrorPage />,
  },
  {
    path: "/search/:query/:page",
    element: <SRP />,
    loader: SRPLoader as any,
    errorElement: <ErrorPage />,
  },
]);

ReactDOM.createRoot(document.getElementById("root")!).render(
  <React.StrictMode>
    <QueryClientProvider client={queryClient}>
      <ThemeProvider>
        <div className="dark rose black hidden"></div>
        <RouterProvider router={BrowserRouter} />
      </ThemeProvider>
    </QueryClientProvider>
  </React.StrictMode>
);
