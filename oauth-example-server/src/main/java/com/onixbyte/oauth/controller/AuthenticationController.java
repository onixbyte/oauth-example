package com.onixbyte.oauth.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.onixbyte.oauth.authentication.token.MsalToken;
import com.onixbyte.oauth.authentication.token.TotpToken;
import com.onixbyte.oauth.authentication.token.UsernamePasswordToken;
import com.onixbyte.oauth.data.request.MsalAuthenticationRequest;
import com.onixbyte.oauth.data.request.NormalAuthenticationRequest;
import com.onixbyte.oauth.data.request.TotpAuthenticationRequest;
import com.onixbyte.oauth.data.response.UserResponse;
import com.onixbyte.oauth.exception.BizException;
import com.onixbyte.oauth.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Autowired
    public AuthenticationController(
            AuthenticationManager authenticationManager,
            TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> normalAuthentication(
            @RequestBody NormalAuthenticationRequest request
    ) {
        var authenticatedToken = (UsernamePasswordToken) authenticationManager.authenticate(
                UsernamePasswordToken.unauthenticated(request.username(), request.password())
        );

        if (!authenticatedToken.isAuthenticated()) {
            throw new BizException(HttpStatus.UNAUTHORIZED, "Incorrect username or password.");
        }

        var token = tokenService.createToken(Duration.ofHours(1), authenticatedToken.getDetails());

        return ResponseEntity.status(HttpStatus.OK)
                .header("Authorization", token)
                .body(authenticatedToken.getDetails().asResponse());
    }

    @PostMapping("/totp")
    public ResponseEntity<UserResponse> totpAuthentication(
            @RequestBody TotpAuthenticationRequest request
    ) {
        var authenticatedToken = (TotpToken) authenticationManager.authenticate(
                TotpToken.unauthenticated(request.userId(), request.totp())
        );

        if (!authenticatedToken.isAuthenticated()) {
            throw new BizException(HttpStatus.UNAUTHORIZED, "TOTP incorrect.");
        }

        var token = tokenService.createToken(Duration.ofHours(1), authenticatedToken.getDetails());

        return ResponseEntity.status(HttpStatus.OK)
                .header("Authorization", token)
                .body(authenticatedToken.getDetails().asResponse());
    }

    /**
     * Authenticate user with Microsoft Entra ID.
     *
     * @param request authentication request with Microsoft Entra ID
     */
    @PostMapping("/msal")
    public ResponseEntity<UserResponse> msalAuthentication(@RequestBody MsalAuthenticationRequest request) {
        // get id token from frontend
        var idToken = request.idToken();
        var authenticatedToken = (MsalToken) authenticationManager.authenticate(
                MsalToken.unauthenticated(idToken)
        );

        var user = authenticatedToken.getDetails();

        log.info("User logged in with Microsoft Entra ID: {}", user.getMsalOpenId());
        var token = tokenService.createToken(Duration.ofDays(1), user);
        return ResponseEntity.status(HttpStatus.OK)
                .header("Authorization", token)
                .body(user.asResponse());
    }
}
