package com.onixbyte.oauth.processor.password;

import com.onixbyte.oauth.model.User;

/**
 * Defines a contract for processing a user's password according to a specific authentication method or condition.
 * Implementations determine whether they support processing a given user and perform the necessary password handling.
 *
 * @author zihluwang
 */
public interface PasswordProcessor {

    /**
     * Determines whether this processor supports handling the password of the specified user.
     *
     * @param user the user whose password needs processing
     * @return {@code true} if this processor supports the given user; {@code false} otherwise
     */
    boolean supports(User user);

    /**
     * Processes the password of the specified user.
     * The processing may include encoding, clearing, or other transformations based on implementation.
     *
     * @param user the user whose password is to be processed
     */
    void process(User user);
}
