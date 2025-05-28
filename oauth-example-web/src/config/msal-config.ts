import { Configuration, PublicClientApplication } from "@azure/msal-browser"

const clientId = import.meta.env.VITE_AUTH_CLIENT_ID
const tenantId = import.meta.env.VITE_AUTH_TENANT_ID

const msalConfig: Configuration = {
  auth: {
    clientId,
    authority: `https://login.microsoftonline.com/${tenantId}`,
    redirectUri: "http://localhost:5173"
  },
  cache: {
    cacheLocation: "localStorage",
  }
}

export const msalInstance = new PublicClientApplication(msalConfig)
