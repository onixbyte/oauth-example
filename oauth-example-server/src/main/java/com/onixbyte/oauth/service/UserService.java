package com.onixbyte.oauth.service;

import com.onixbyte.guid.GuidCreator;
import com.onixbyte.oauth.data.persistent.User;
import com.onixbyte.oauth.exception.BizException;
import com.onixbyte.oauth.exception.PropertyMissingException;
import com.onixbyte.oauth.manager.PasswordProcessorManager;
import com.onixbyte.oauth.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final PasswordProcessorManager passwordProcessorManager;
    private final UserRepository userRepository;
    private final GuidCreator<Long> userGuidCreator;

    public UserService(
            PasswordProcessorManager passwordProcessorManager,
            UserRepository userRepository,
            GuidCreator<Long> userGuidCreator
    ) {
        this.passwordProcessorManager = passwordProcessorManager;
        this.userRepository = userRepository;
        this.userGuidCreator = userGuidCreator;
    }

    /**
     * User register.
     *
     * @param user the user to be registered
     */
    public User register(User user) {
        // check whether the user can be registered
        Optional.ofNullable(user)
                .map(User::getUsername)
                .orElseThrow(() -> new PropertyMissingException("username"));

        Optional.of(user)
                .map(User::getEmail)
                .orElseThrow(() -> new PropertyMissingException("email"));

        if (!userRepository.canRegister(user)) {
            throw new BizException(HttpStatus.CONFLICT, "User's username/email or Microsoft Entra ID open ID is duplicated.");
        }

        // edit user information
        passwordProcessorManager.process(user);
        user.setId(userGuidCreator.nextId()); // generate user ID

        // save user in database
        userRepository.insert(user);

        return user;
    }

    public Optional<User> getUserByMsalOpenId(String msalOpenId) {
        return Optional.ofNullable(userRepository.getUserByMsalOpenId(msalOpenId));
    }
}
