package com.onixbyte.oauth.data.persistent;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import com.onixbyte.common.util.BoolUtil;
import com.onixbyte.oauth.data.persistent.table.UserTableDef;
import com.onixbyte.oauth.data.response.UserResponse;

import java.util.Objects;

@Table("users")
public class User {

    /**
     * User ID.
     */
    @Id
    private Long id;

    /**
     * Username.
     */
    private String username;

    /**
     * Password.
     */
    private String password;

    /**
     * Email address.
     */
    private String email;

    /**
     * Open ID provided by Microsoft Entra ID.
     */
    private String msalOpenId;

    /**
     * Base32 encoded secret key used for TOTP generation.
     */
    private String totpSecret;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMsalOpenId() {
        return msalOpenId;
    }

    public void setMsalOpenId(String msalOpenId) {
        this.msalOpenId = msalOpenId;
    }

    public String getTotpSecret() {
        return totpSecret;
    }

    public void setTotpSecret(String totpSecret) {
        this.totpSecret = totpSecret;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        var user = (User) object;
        return BoolUtil.and(
                Objects.equals(getId(), user.getId()),
                Objects.equals(getUsername(), user.getUsername()),
                Objects.equals(getPassword(), user.getPassword()),
                Objects.equals(getEmail(), user.getEmail()),
                Objects.equals(getMsalOpenId(), user.getMsalOpenId())
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUsername(), getPassword(), getEmail(), getMsalOpenId());
    }

    public User() {
    }

    public User(
            Long id,
            String username,
            String password,
            String email,
            String msalOpenId,
            String totpSecret
    ) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.msalOpenId = msalOpenId;
        this.totpSecret = totpSecret;
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static class UserBuilder {
        private Long id;
        private String username;
        private String password;
        private String email;
        private String msalOpenId;
        private String totpSecret;

        private UserBuilder() {
        }

        public UserBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public UserBuilder withUsername(String username) {
            this.username = username;
            return this;
        }

        public UserBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder withMsalOpenId(String msalOpenId) {
            this.msalOpenId = msalOpenId;
            return this;
        }

        public UserBuilder withTotpSecret(String totpSecret) {
            this.totpSecret = totpSecret;
            return this;
        }

        public User build() {
            return new User(id, username, password, email, msalOpenId, totpSecret);
        }
    }

    public UserResponse asResponse() {
        return new UserResponse(String.valueOf(id), username, email);
    }

    public static UserTableDef USER = new UserTableDef();

    public boolean totpEnabled() {
        return Objects.nonNull(totpSecret) && !totpSecret.isBlank();
    }
}
