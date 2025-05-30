import React from "react"
import { useMsal } from "@azure/msal-react"
import { PopupRequest } from "@azure/msal-browser"
import { loginSuccess } from "@/store/auth-slice"
import { useAppDispatch } from "@/store"
import * as AuthorisationApi from "@/service/api/authorisation"

export const Login = () => {
  const { instance } = useMsal()
  const dispatch = useAppDispatch()

  const doMsalLogin = async () => {
    try {
      const response = await instance.loginPopup({
        scopes: ["openid", "profile", "email"]
      })
      console.log(response)

      await AuthorisationApi.msalLogin(response.idToken)

      // 假设后端返回用户信息，更新redux状态
      dispatch(loginSuccess({ user: response.account.username }))
    } catch (err) {
      console.error("MSAL login failed", err)
    }
  }

  return (
    <div>
      <h1 className="text-center">Login</h1>
      <button
        onClick={() => void doMsalLogin()}
        className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">
        Login with Microsoft Entra ID
      </button>
    </div>
  )
}
