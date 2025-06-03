import { Navigate, Outlet, useLocation } from "react-router"
import { useAppSelector } from "@/store"

export const ProtectedRoute = () => {
  const isAuthenticated = useAppSelector(({ auth }) => auth.isAuthenticated)
  const location = useLocation()

  if (!isAuthenticated) {
    return <Navigate to="/login" state={{ from: location }} replace />
  }

  return <Outlet />
}