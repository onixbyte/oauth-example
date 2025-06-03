package com.onixbyte.oauth.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.onixbyte.oauth.data.request.MsalAuthoriseRequest;
import com.onixbyte.oauth.service.MsalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authorisation")
public class AuthorisationController {

    private static final Logger log = LoggerFactory.getLogger(AuthorisationController.class);

    private final MsalService msalService;

    public AuthorisationController(MsalService msalService) {
        this.msalService = msalService;
    }

    /**
     * Authorisation for Microsoft Entra ID.
     *
     * @param msalAuthoriseRequest authorisation request for Microsoft Entra ID
     */
    @PostMapping("/msal")
    public void msalAuthorisation(@RequestBody MsalAuthoriseRequest msalAuthoriseRequest) {
        try {
            // get id token from frontend
            var idToken = msalAuthoriseRequest.idToken();

            // decode JWT to retrieve kid in header
            var decodedToken = JWT.decode(idToken);
            var keyId = decodedToken.getKeyId();
            if (keyId == null) {
                throw new RuntimeException("Token header missing key id.");
            }

            // get RSAPublicKey from Microsoft JWKS cache according to kid
            var publicKey = msalService.getPublicKey(keyId);

            // create algorithm instance
            var algorithm = Algorithm.RSA256(publicKey, null);

            // create verifier and verify `issuer` and `aud`
            var verifier = JWT.require(algorithm)
                    // format of the issuer is `https://login.microsoftonline.com/{tenantId}/v2.0`
                    .withIssuer("https://login.microsoftonline.com/" + msalService.getTenantId() + "/v2.0")
                    // client id is used for audience
                    .withAudience(msalService.getClientId())
                    .build();

            // verify token
            verifier.verify(idToken);

            // get open id from token
            var msalOpenId = decodedToken.getClaim("oid").asString();



            log.info("User logged in with Microsoft Entra ID: {}", msalOpenId);
        } catch (Exception e) {
            log.error("Token validation failed", e);
        }
    }
}
