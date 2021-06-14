package com.tju.myproject.dao;

import com.tju.myproject.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Map;

@Repository
public interface UserDao
{
    int addUser(@Param("user") User user);
    User getUserByPhone(@Param("phone")String phone);
    User getUserByUserID(@Param("userID")Integer userID);

    @Select("select * from user where student_id=#{studentID} and university=#{university} and name=#{name}")
    Map getUserByInfo(Map data);
    @Select("select * from user_auth where isPass=-1 limit #{index},#{size}")
    ArrayList<Map> getUserAuthInfo(@Param("index")Integer index, @Param("size")Integer size);
    @Insert("insert into user_auth (userID, name, university, images, isPass, adminID, openid) VALUES (#{userID}, #{name}, #{university}, #{images}, -1, -1, #{openid})")
    Integer addUserAuth(Map data);
    @Update("update user_auth SET isPass = #{isPass}, adminID = #{adminID}, remark = #{remark} WHERE (id = #{authID})")
    Integer updateAuth(Map data);
    @Update("update user SET isAuth = #{isAuth} WHERE (id = #{userID})")
    Integer updateUserAuth(@Param("isAuth") Integer isAuth, @Param("userID") Integer userID);
}
