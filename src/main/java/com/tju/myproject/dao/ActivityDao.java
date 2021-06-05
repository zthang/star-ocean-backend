package com.tju.myproject.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
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
    @Select("select * from activity limit #{index},#{size}")
    ArrayList<Map> getActivities(@Param("index")Integer index, @Param("size")Integer size);
    @Select("select * from location where id=#{id}")
    Map getLocationById(@Param("id")Integer id);
    @Select("select * from good where id=#{id}")
    Map getGoodById(@Param("id")Integer id);
    @Select("select * from university where id=#{id}")
    Map getUniversityById(@Param("id")Integer id);
    @Insert("insert into user_activity(activityID, userID, name, phone, urgentPhone, idCard, location, scheme, remark, shouldPay) VALUES (#{activityID}, #{userID}, #{name}, #{phone}, #{urgentPhone}, #{idCard}, #{location}, #{scheme}, #{remark}, #{shouldPay})")
    Integer activityEnrol(Map data);
    @Insert("insert into user_activity_good (activityID, userID, good, num) VALUES (#{activityID}, #{userID}, #{good}, #{num})")
    Integer addUserNeedGood(Map data);
    @Select("select * from user_activity where activityID=#{activityID} and userID=#{userID}")
    Map getUserActivity(@Param("activityID")Integer activityID, @Param("userID")Integer userID);
}
