package com.onixbyte.oauth.processor.password;

import com.onixbyte.oauth.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Order(0)
public class MsalPasswordProcessor implements PasswordProcessor {

    private static final Logger log = LoggerFactory.getLogger(MsalPasswordProcessor.class);

    @Override
    public boolean supports(User user) {
        log.info("Testing with Microsoft Entra ID password processor.");
        return Objects.nonNull(user.getMsalOpenId()) && !user.getMsalOpenId().isBlank();
    }

    @Override
    public void process(User user) {
        user.setPassword("");
    }
}
