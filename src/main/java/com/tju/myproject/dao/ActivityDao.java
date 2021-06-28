package com.tju.myproject.dao;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Map;

@Mapper
@Repository
public interface ActivityDao
{
    @Select("select * from location")
    ArrayList<Map> getLocation();
    @Select("select * from good")
    ArrayList<Map> getGood();
    @Select("select * from university")
    ArrayList<Map> getUniversity();
    @Select("select * from club")
    ArrayList<Map> getClub();
    @Update("update location set name=#{name} where id=#{id}")
    Integer updateLocation(Map data);
    @Delete("delete from location where id=#{id}")
    Integer deleteLocation(@Param("id") Integer id);
    @Insert("insert into location (name) VALUES (#{name})")
    Integer addLocation(@Param("name") String name);
    @Update("update good set name=#{name}, price=#{price} where id=#{id}")
    Integer updateGood(Map data);
    @Delete("delete from good where id=#{id}")
    Integer deleteGood(@Param("id") Integer id);
    @Insert("insert into good (name, price) VALUES (#{name}, #{price})")
    Integer addGood(Map data);
    @Insert("insert into activity (activityName, activityDate, activityDDL, activityLocation, activityPrice, selectedLocation, selectedGood, selectedClub, scheme, delta, showImg, plainText) VALUES (#{activityName},#{activityDate},#{activityDDL},#{activityLocation},#{activityPrice},#{selectedLocation},#{selectedGood},#{selectedClub},#{scheme},#{delta},#{showImg},#{plainText})")
    Integer addActivity(Map m);
    @Update("update activity set activityName=#{activityName}, activityDate=#{activityDate}, activityDDL=#{activityDDL}, activityLocation=#{activityLocation}, activityPrice=#{activityPrice}, selectedLocation=#{selectedLocation}, selectedGood=#{selectedGood}, selectedClub=#{selectedClub}, scheme=#{scheme}, delta=#{delta}, showImg=#{showImg}, plainText=#{plainText} where id=#{activityID}")
    Integer updateActivity(Map m);
    @Select("select * from activity order by id desc limit #{index},#{size}")
    ArrayList<Map> getActivities(@Param("index")Integer index, @Param("size")Integer size);
    @Select("select * from activity order by id desc")
    ArrayList<Map> getAllActivities();
    @Select("select * from location where id=#{id}")
    Map getLocationById(@Param("id")Integer id);
    @Select("select * from good where id=#{id}")
    Map getGoodById(@Param("id")Integer id);
    @Select("select * from university where id=#{id}")
    Map getUniversityById(@Param("id")Integer id);
    @Insert("insert into user_activity(activityID, userID, name, phone, urgentPhone, idCard, location, scheme, remark, shouldPay,openid) VALUES (#{activityID}, #{userID}, #{name}, #{phone}, #{urgentPhone}, #{idCard}, #{location}, #{scheme}, #{remark}, #{shouldPay},#{openid})")
    Integer activityEnrol(Map data);
    @Update("update user_activity set userID=#{userID}, name=#{name}, phone=#{phone}, urgentPhone=#{urgentPhone},idCard=#{idCard},location=#{location}, scheme=#{scheme}, remark=#{remark}, shouldPay=#{shouldPay},openid=#{openid} where id=#{enrolID}")
    Integer updateActivityEnrol(Map data);
    @Insert("insert into user_activity_good (activityID, userID, good, num) VALUES (#{activityID}, #{userID}, #{good}, #{num})")
    Integer addUserNeedGood(Map data);
    @Delete("delete from user_activity_good where activityID=#{activityID} and userID=#{userID}")
    Integer deleteUserNeedGood(Map data);
    @Select("select * from user_activity where activityID=#{activityID} and userID=#{userID}")
    Map getUserActivity(@Param("activityID")Integer activityID, @Param("userID")Integer userID);

    @Select("select * from user_activity where activityID=#{activityID}")
    @Results(id = "activityUserInfoMap", value = {
            @Result(property = "id", column = "id", javaType = Integer.class, jdbcType = JdbcType.INTEGER, id = true),
            @Result(property = "activityID", column = "activityID", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "userID", column = "userID", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "name", column = "name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "phone", column = "phone", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "urgentPhone", column = "urgentPhone", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "idCard", column = "idCard", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "location", column = "location", javaType = Map.class, one=@One(select="com.tju.myproject.dao.ActivityDao.getLocationById",fetchType= FetchType.EAGER)),
            @Result(property = "scheme", column = "scheme", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "remark", column = "remark", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "shouldPay", column = "shouldPay", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "openid", column = "openid", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(column = "{activityID = activityID, userID = userID}", property = "goods",many = @Many(select = "com.tju.myproject.dao.ActivityDao.getActivityUserGoods", fetchType = FetchType.EAGER))
    })
    ArrayList<Map> getActivityUsersInfo(@Param("activityID") Integer activityID);

    @Select("select * from user_activity_good as a left join good as b on a.good=b.id where activityID=#{activityID} and userID=#{userID}")
    ArrayList<Map>getActivityUserGoods(@Param("activityID") Integer activityID, @Param("userID") Integer userID);
}

//many = @Many(select = "com.pjb.mapper.UserMapper.getRoleList", fetchType = FetchType.LAZY)