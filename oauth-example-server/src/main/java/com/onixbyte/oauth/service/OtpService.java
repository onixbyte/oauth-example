package com.onixbyte.oauth.service;

import com.onixbyte.oauth.enums.TotpAlgorithm;
import com.onixbyte.oauth.exception.BizException;
import com.onixbyte.oauth.properties.TotpProperties;
import org.apache.commons.codec.binary.Base32;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;

@Service
public class OtpService {

    private static final Logger log = LoggerFactory.getLogger(OtpService.class);

    private final TotpAlgorithm totpAlgorithm;

    private final Duration timeStep;

    private final Integer digits;

    public OtpService(TotpProperties totpProperties) {
        this.digits = totpProperties.getDigits();
        this.timeStep = totpProperties.getTimeStep();
        this.totpAlgorithm = totpProperties.getTotpAlgorithm();
    }

    public String generateTotp(String totpSecret) {
        try {
            var counter = (System.currentTimeMillis() / 1000) / timeStep.toSeconds();
            var key = new Base32().decode(totpSecret);

            var secretKey = new SecretKeySpec(key, totpAlgorithm.getAlgorithm());
            var mac = Mac.getInstance(totpAlgorithm.getAlgorithm());
            mac.init(secretKey);

            var counterBytes = new byte[8];
            for (var i = 0; i < 8; i++) {
                counterBytes[7 - i] = (byte) (counter >> (8 * i));
            }

            var hash = mac.doFinal(counterBytes);
            var offset = hash[hash.length - 1] & 0x0F;
            var binary = ((hash[offset] & 0x7F) << 24 | (hash[offset + 1] & 0xFF) << 16 | (hash[offset + 2] & 0xFF) << 8 | (hash[offset + 3] & 0xFF));

            var otp = binary % (int) Math.pow(10, digits);
            return String.format("%0" + digits + "d", otp);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            log.error("Error.", e);
            throw new BizException(HttpStatus.UNAUTHORIZED, "TOTP Secret incorrect, please try again later.");
        }
    }
}
