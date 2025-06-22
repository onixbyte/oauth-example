import webClient from "@/service/web-client"
import { UserResponse } from "@/type/user-response.ts"

export async function msalLogin(idToken: string): Promise<{
  authorisationToken: string
  user: UserResponse
}> {
  const { data, headers } = await webClient.post<UserResponse>("/authentication/msal", {
    idToken,
  })

  const authorisationToken = (headers as Record<string, string | undefined>).authorization ?? ''

  return {
    authorisationToken,
    user: data
  }
}
