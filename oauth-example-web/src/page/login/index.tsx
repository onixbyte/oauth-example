import React from "react"
import { useMsal } from "@azure/msal-react"
import { InteractionType, PopupRequest } from "@azure/msal-browser"
import { loginSuccess } from "@/store/auth-slice"
import { useAppDispatch } from "@/store"
import * as AuthorisationApi from "@/service/api/authorisation"

const loginRequest: PopupRequest = {
  scopes: ["openid", "profile", "email"],
}

export const Login = () => {
  const { instance } = useMsal()
  const dispatch = useAppDispatch()

  const handleMsalLogin = async () => {
    try {
      const response = await instance.loginPopup(loginRequest)
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
      <h1>登录</h1>
      <button onClick={() => void handleMsalLogin()}>
        使用 Microsoft Entra ID 登录
      </button>
    </div>
  )
}
