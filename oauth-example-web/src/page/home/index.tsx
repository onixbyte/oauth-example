import { useAppSelector } from "@/store"

export const Home = () => {
  const user = useAppSelector(({ auth }) => auth.user!)

  return (
    <div>
      Hello, <span className="text-green-500">{user.username}</span>
    </div>
  )
}