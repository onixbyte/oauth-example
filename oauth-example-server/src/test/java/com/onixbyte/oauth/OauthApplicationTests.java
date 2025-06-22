package com.onixbyte.oauth;

import com.onixbyte.oauth.properties.TotpProperties;
import com.onixbyte.oauth.service.MsalService;
import com.onixbyte.oauth.service.OtpService;
import com.onixbyte.oauth.service.UserService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({"db", "local"})
class OauthApplicationTests {

    private static final Logger log = LoggerFactory.getLogger(OauthApplicationTests.class);

    @Autowired
    private OtpService otpService;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void contextLoads() {
        var user = userService.getUserById(1386184077064798208L);
        user.setPassword(passwordEncoder.encode("460215ZihluWangHKMO!@#"));
        user.setTotpEnabled(true);
        user.setTotpSecret("JBSWY3DPEHPK3PXP");
        userService.updateUser(user);
    }

}
