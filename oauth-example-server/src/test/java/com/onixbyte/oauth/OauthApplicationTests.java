package com.onixbyte.oauth;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({"db", "local"})
class OauthApplicationTests {

    private static final Logger log = LoggerFactory.getLogger(OauthApplicationTests.class);

    @Test
    void contextLoads() {
    }

}
