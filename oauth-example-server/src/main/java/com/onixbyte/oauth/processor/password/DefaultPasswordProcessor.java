package com.onixbyte.oauth.processor.password;

import com.onixbyte.oauth.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Order()
public class DefaultPasswordProcessor implements PasswordProcessor {

    private static final Logger log = LoggerFactory.getLogger(DefaultPasswordProcessor.class);
    private final PasswordEncoder passwordEncoder;

    public DefaultPasswordProcessor(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean supports(User user) {
        log.info("Testing user with DefaultPasswordProcessor");
        var password = user.getPassword();
        return Objects.nonNull(password) && !password.isBlank();
    }

    @Override
    public void process(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }
}
