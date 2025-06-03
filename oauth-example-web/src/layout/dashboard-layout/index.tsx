import { Outlet } from "react-router"
import { useAppSelector } from "@/store"

export const DashboardLayout = () => {
  const user = useAppSelector(({ auth }) => auth.user!)

  return (
    <>
      {/* top navigation bar */}
      <div>{user.username}</div>
      <Outlet />
    </>
  )
}
