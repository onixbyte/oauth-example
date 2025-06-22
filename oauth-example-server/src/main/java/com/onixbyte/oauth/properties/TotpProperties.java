package com.onixbyte.oauth.properties;

import com.onixbyte.oauth.enums.TotpAlgorithm;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "app.auth.totp")
public class TotpProperties {

    private TotpAlgorithm totpAlgorithm;

    private Duration timeStep;

    private Integer digits;

    public TotpAlgorithm getTotpAlgorithm() {
        return totpAlgorithm;
    }

    public void setTotpAlgorithm(TotpAlgorithm totpAlgorithm) {
        this.totpAlgorithm = totpAlgorithm;
    }

    public Duration getTimeStep() {
        return timeStep;
    }

    public void setTimeStep(Duration timeStep) {
        this.timeStep = timeStep;
    }

    public Integer getDigits() {
        return digits;
    }

    public void setDigits(Integer digits) {
        this.digits = digits;
    }

    public TotpProperties() {
    }

    public TotpProperties(TotpAlgorithm totpAlgorithm, Duration timeStep, Integer digits) {
        this.totpAlgorithm = totpAlgorithm;
        this.timeStep = timeStep;
        this.digits = digits;
    }
}
