package com.onixbyte.oauth.repository;

import com.mybatisflex.core.BaseMapper;
import com.onixbyte.oauth.data.persistent.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseMapper<User> {

    boolean canRegister(@Param("user") User user);

    User getUserByMsalOpenId(@Param("msalOpenId") String msalOpenId);
}
