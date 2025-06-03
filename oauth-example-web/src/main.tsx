import { StrictMode } from "react"
import { createRoot } from "react-dom/client"
import { Provider } from "react-redux"
import { BrowserRouter, Route, Routes } from "react-router"
import { MsalProvider } from "@azure/msal-react"
import "./index.css"
import { store } from "@/store"
import { msalInstance } from "@/config/msal-config"
import { ProtectedRoute } from "@/components/protected-route"
import { EmptyLayout } from "@/layout/empty-layout"
import { DashboardLayout } from "@/layout/dashboard-layout"
import { Login } from "@/page/login"
import { Home } from "@/page/home"

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <Provider store={store}>
      <MsalProvider instance={msalInstance}>
        <BrowserRouter>
          <Routes>
            <Route path="/login" element={<Login />}></Route>
            <Route element={<ProtectedRoute />}>
              <Route path="/" element={<DashboardLayout />}>
                <Route index element={<Home />}></Route>
              </Route>
            </Route>
          </Routes>
        </BrowserRouter>
      </MsalProvider>
    </Provider>
  </StrictMode>,
)
