package com.tju.myproject.dao;

import com.tju.myproject.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao
{
    int addUser(@Param("user") User user);
    User getUserByPhone(@Param("phone")String phone);
    User getUserByUserID(@Param("userID")Integer userID);
}
