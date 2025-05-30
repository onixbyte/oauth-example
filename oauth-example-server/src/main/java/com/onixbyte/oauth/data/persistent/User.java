package com.onixbyte.oauth.data.persistent;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;

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
}
