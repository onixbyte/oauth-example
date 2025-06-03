import React from "react"
import { useMsal } from "@azure/msal-react"
import { useAppDispatch } from "@/store"
import { doMsalLogin } from "@/service/auth"
import { useNavigate } from "react-router"

export const Login = () => {
  const { instance } = useMsal()
  const dispatch = useAppDispatch()
  const navigate = useNavigate()

  return (
    <div>
      <h1 className="text-center">Login</h1>
      <div className="mx-auto max-w-[680px] w-[680px] flex justify-center gap-2 bg-[#D0D0D0]">
        <button
          onClick={() => void doMsalLogin(instance, dispatch, () => void navigate("/"))}
          className="bg-[#00A3EE] text-white px-4 py-2 rounded hover:bg-[#00ADFF]">
          Login with Microsoft Entra ID
        </button>
      </div>
    </div>
  )
}
