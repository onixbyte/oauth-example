import { IPublicClientApplication } from "@azure/msal-browser"
import * as AuthenticationApi from "@/service/api/authentication.ts"
import { loginSuccess } from "@/store/auth-slice.ts"
import { AppDispatch } from "@/store"

/**
 * Login with Microsoft Entra ID.
 *
 * @param instance Microsoft Entra ID application instance
 * @param dispatch app dispatcher
 * @param onSuccess callback when login succeeded
 */
export async function doMsalLogin(
  instance: IPublicClientApplication,
  dispatch: AppDispatch,
  onSuccess?: () => void,
) {
  try {
    const response = await instance.loginPopup({
      scopes: ["openid", "profile", "email"],
    })

    const { authorisationToken, user } = await AuthenticationApi.msalLogin(
      response.idToken,
    )
    dispatch(loginSuccess({ user, authorisationToken }))
    if (onSuccess) onSuccess()
  } catch (err) {
    console.error("MSAL login failed", err)
  }
}
