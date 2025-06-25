package com.onixbyte.oauth.repository;

import com.mybatisflex.core.BaseMapper;
import com.onixbyte.oauth.model.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseMapper<User> {

    boolean canRegister(@Param("user") User user);

    @Select("""
            SELECT *
              FROM users
             WHERE msal_open_id = #{msalOpenId}
            """)
    Optional<User> getUserByMsalOpenId(@Param("msalOpenId") String msalOpenId);
}
