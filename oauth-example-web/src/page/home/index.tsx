import { useAppSelector } from "@/store"

export const Home = () => {
  const user = useAppSelector(({ auth }) => auth.user)

  return (
    <div>
      Hello, {user!.msalOpenId}
    </div>
  )
}