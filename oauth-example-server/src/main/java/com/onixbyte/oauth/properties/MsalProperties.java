package com.onixbyte.oauth.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Microsoft Entra ID properties.
 *
 * @author siujamo
 */
@ConfigurationProperties(prefix = "app.auth.msal")
public class MsalProperties {

    /**
     * Tenant ID of Microsoft Entra ID.
     */
    private String tenantId;

    /**
     * Client ID of Microsoft Entra ID.
     */
    private String clientId;

    /**
     * Get tenant ID of Microsoft Entra ID.
     *
     * @return tenant ID of Microsoft Entra ID
     */
    public String getTenantId() {
        return tenantId;
    }

    /**
     * Set tenant ID of Microsoft Entra ID.
     *
     * @param tenantId ID of Microsoft Entra ID
     */
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * Get client ID of Microsoft Entra ID.
     *
     * @return client ID of Microsoft Entra ID
     */
    public String getClientId() {
        return clientId;
    }
    /**
     * Set client ID of Microsoft Entra ID.
     *
     * @param clientId client ID of Microsoft Entra ID
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public MsalProperties() {
    }
}
