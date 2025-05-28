import webClient from "@/service/web-client"

export async function msalLogin(idToken: string) {
  await webClient.post("/authorisation/msal", {
    idToken
  })
}