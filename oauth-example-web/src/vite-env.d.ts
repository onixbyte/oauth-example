/// <reference types="vite/client" />
interface ImportMetaEnv {
  /**
   * Client ID for Microsoft Entra ID
   */
  readonly VITE_AUTH_CLIENT_ID: string
  /**
   * Tenant ID for Microsoft Entra ID
   */
  readonly VITE_AUTH_TENANT_ID: string
  /**
   * Backend base URL
   */
  readonly VITE_BASE_URL: string
}

interface ImportMeta {
  readonly env: ImportMetaEnv
}
