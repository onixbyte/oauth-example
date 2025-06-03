import { useAppSelector } from "@/store"
import { Outlet } from "react-router"

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
