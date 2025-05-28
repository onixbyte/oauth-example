package com.onixbyte.oauth;

import com.onixbyte.oauth.service.MsalService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OauthApplicationTests {

    private static final Logger log = LoggerFactory.getLogger(OauthApplicationTests.class);

    @Autowired
    private MsalService msalService;

    @Test
    void contextLoads() throws Exception {
        var publicKey = msalService.getPublicKey("CNv0OI3RwqlHFEVnaoMAshCH2XE");
        log.info("PublicKey = {}", publicKey);
    }

}
