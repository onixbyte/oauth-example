package com.onixbyte.oauth.manager;

import com.onixbyte.oauth.data.persistent.User;
import com.onixbyte.oauth.processor.PasswordProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PasswordProcessorManager {

    private final List<PasswordProcessor> passwordProcessors;

    @Autowired
    public PasswordProcessorManager(List<PasswordProcessor> passwordProcessors) {
        this.passwordProcessors = passwordProcessors;
    }

    public void process(User user) {
        for (var passwordProcessor : passwordProcessors) {
            if (passwordProcessor.supports(user)) {
                passwordProcessor.process(user);
                return;
            }
        }
    }

}
