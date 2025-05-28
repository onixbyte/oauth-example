import { StrictMode } from "react"
import { createRoot } from "react-dom/client"
import { Provider } from "react-redux"
import { BrowserRouter, Route, Routes } from "react-router"
import { MsalProvider } from "@azure/msal-react"
import { store } from "@/store"
import "./index.css"
import { ProtectedRoute } from "@/components/protected-route"
import { msalInstance } from "@/config/msal-config"
import { Login } from "@/page/login"

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <Provider store={store}>
      <MsalProvider instance={msalInstance}>
        <BrowserRouter>
          <Routes>
            <Route path="/login" element={<Login />}></Route>
            <Route element={<ProtectedRoute />}>
              <Route path="/" element={null}></Route>
            </Route>
          </Routes>
        </BrowserRouter>
      </MsalProvider>
    </Provider>
  </StrictMode>,
)
