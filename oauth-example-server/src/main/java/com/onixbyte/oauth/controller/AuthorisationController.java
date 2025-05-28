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

    @PostMapping("/msal")
    public void msalAuthorisation(@RequestBody MsalAuthoriseRequest msalAuthoriseRequest) {
        try {
            // 1. 拿到前端发送的idToken
            var idToken = msalAuthoriseRequest.idToken();

            // 2. 解码JWT（不验证），获取header中的 kid
            var decodedJWT = JWT.decode(idToken);
            var keyId = decodedJWT.getKeyId();
            if (keyId == null) {
                throw new RuntimeException("Token header missing key id.");
            }

            // 3. 根据kid从Microsoft JWKS缓存中获取RSAPublicKey
            var publicKey = msalService.getPublicKey(keyId);

            // 4. 创建算法实例
            var algorithm = Algorithm.RSA256(publicKey, null);

            // 5. 创建验证器，校验issuer和aud
            var verifier = JWT.require(algorithm)
                    // 这里issuer的格式通常是：https://login.microsoftonline.com/{tenantId}/v2.0
                    .withIssuer("https://login.microsoftonline.com/" + msalService.getTenantId() + "/v2.0")
                    // 你的应用的ClientId，作为audience
                    .withAudience(msalService.getClientId())
                    .build();

            // 6. 验证token
            verifier.verify(idToken);

            // 7. 认证成功后，你可以取到用户标识，比如 oid claim
            var oid = decodedJWT.getClaim("oid").asString();

            log.info("User logged in with oid: {}", oid);
        } catch (Exception e) {
            log.error("Token validation failed", e);
        }
    }
}
