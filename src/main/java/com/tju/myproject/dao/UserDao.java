package com.tju.myproject.dao;

import com.tju.myproject.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao
{
    int addUser(@Param("user") User user);
    User getUserByUsername(@Param("username")String username);
    User getUserByUserID(@Param("userID")Integer userID);
}
