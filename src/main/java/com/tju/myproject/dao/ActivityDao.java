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
    @Insert("insert into activity (activityName, activityDate, activityDDL, activityLocation, activityPrice, selectedLocation, selectedGood, selectedUniversity, scheme, delta, showImg, plainText) VALUES (#{activityName},#{activityDate},#{activityDDL},#{activityLocation},#{activityPrice},#{selectedLocation},#{selectedGood},#{selectedUniversity},#{scheme},#{delta},#{showImg},#{plainText})")
    Integer addActivity(Map m);
    @Update("update activity set activityName=#{activityName}, activityDate=#{activityDate}, activityDDL=#{activityDDL}, activityLocation=#{activityLocation}, activityPrice=#{activityPrice}, selectedLocation=#{selectedLocation}, selectedGood=#{selectedGood}, selectedUniversity=#{selectedUniversity}, scheme=#{scheme}, delta=#{delta}, showImg=#{showImg}, plainText=#{plainText} where id=#{activityID}")
    Integer updateActivity(Map m);
    @Select("select * from activity order by id desc limit #{index},#{size}")
    ArrayList<Map> getActivities(@Param("index")Integer index, @Param("size")Integer size);
    @Select("select * from location where id=#{id}")
    Map getLocationById(@Param("id")Integer id);
    @Select("select * from good where id=#{id}")
    Map getGoodById(@Param("id")Integer id);
    @Select("select * from university where id=#{id}")
    Map getUniversityById(@Param("id")Integer id);
    @Insert("insert into user_activity(activityID, userID, name, phone, urgentPhone, idCard, location, scheme, remark, shouldPay,openid) VALUES (#{activityID}, #{userID}, #{name}, #{phone}, #{urgentPhone}, #{idCard}, #{location}, #{scheme}, #{remark}, #{shouldPay},#{openid})")
    Integer activityEnrol(Map data);
    @Insert("insert into user_activity_good (activityID, userID, good, num) VALUES (#{activityID}, #{userID}, #{good}, #{num})")
    Integer addUserNeedGood(Map data);
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