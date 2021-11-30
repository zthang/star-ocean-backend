package com.tju.myproject.dao;

import com.tju.myproject.entity.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Mapper
@Repository
public interface UserDao
{
    int addUser(@Param("user") User user);
    User getUserByPhone(@Param("phone")String phone);
    @Select("select * from user where id=#{userID}")
    @ResultMap("userInfoMap")
    HashMap getUserByUserID(@Param("userID")Integer userID);
    @Update("update user set university=#{university}, student_id=#{studentID}, name=#{name}, phone=#{phone}, id_card=#{idCard}, role=#{role} where id=#{id}")
    Integer updateUserInfo(Map data);
    @Options(useGeneratedKeys =true, keyProperty = "id")
    @Insert("insert into user(university, student_id, name, phone, id_card, role, point, isAuth) values (#{university}, #{studentID}, #{name}, #{phone}, #{idCard}, #{role}, 0, -1)")
    Integer addUserInfo(Map data);
    @Insert("insert into user_club(userID, clubID) values (#{userID}, #{clubID})")
    Integer addUserClub(Map data);
    @Delete("delete from user_club where id=#{id}")
    Integer deleteUserClub(@Param("id")Integer id);
    @Delete("delete from user_club where userID=#{userID} and clubID=#{clubID}")
    Integer deleteUserClubByInfo(@Param("userID")Integer userID, @Param("clubID")Integer clubID);
    @Select("select * from user where student_id=#{studentID} and university=#{university} and name=#{name}")
    @Results(id = "userInfoMap", value = {
            @Result(property = "id", column = "id", javaType = Integer.class, jdbcType = JdbcType.INTEGER, id = true),
            @Result(property = "university", column = "university", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "studentID", column = "student_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "name", column = "name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "gender", column = "gender", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "faculty", column = "faculty", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "phone", column = "phone", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "idCard", column = "id_card", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "role", column = "role", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "joinDate", column = "joinDate", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "point", column = "point", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "isAuth", column = "isAuth", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(column = "id", property = "club",many = @Many(select = "com.tju.myproject.dao.UserDao.getUserClubs", fetchType = FetchType.EAGER))
    })
    Map getUserByInfo(Map data);
    @Select("select * from user_club as a left join club as b on a.clubID=b.id where userID=#{userID}")
    ArrayList<Map>getUserClubs(@Param("userID")Integer userID);
    @Select("select * from user_auth where isPass=-1")
    @Results(id = "authInfoMap", value = {
            @Result(property = "id", column = "id", javaType = Integer.class, jdbcType = JdbcType.INTEGER, id = true),
            @Result(property = "userID", column = "userID", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "name", column = "name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "university", column = "university", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "images", column = "images", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "isPass", column = "isPass", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "adminID", column = "adminID", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "remark", column = "remark", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "openid", column = "openid", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(column = "userID", property = "club",many = @Many(select = "com.tju.myproject.dao.UserDao.getUserClubs", fetchType = FetchType.EAGER))
    })
    ArrayList<Map> getUserAuthInfo();
    @Insert("insert into user_auth (userID, name, university, images, isPass, adminID, openid) VALUES (#{userID}, #{name}, #{university}, #{images}, -1, -1, #{openid})")
    Integer addUserAuth(Map data);
    @Update("update user_auth SET isPass = #{isPass}, adminID = #{adminID}, remark = #{remark} WHERE (id = #{authID})")
    Integer updateAuth(Map data);
    @Update("update user SET isAuth = #{isAuth} WHERE (id = #{userID})")
    Integer updateUserAuth(@Param("isAuth") Integer isAuth, @Param("userID") Integer userID);
    @Select("select * from user where name like CONCAT('%',#{name},'%')")
    @ResultMap("userInfoMap")
    ArrayList<Map>getUsersByName(@Param("name") String name);
    @Update("update user set role=1 where id=#{userID}")
    Integer makeUserVip(@Param("userID") Integer userID);
    @Update("update user set id_card=#{idCard} where id=#{userID}")
    Integer updateIdCard(@Param("idCard")String idCard, Integer userID);
    @Update("update user set phone=#{phone} where id=#{userID}")
    Integer updatePhone(@Param("phone")String phone, Integer userID);
}
