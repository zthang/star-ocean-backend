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
    @Select("select * from area")
    ArrayList<Map> getArea();
    @Select("select * from area_university")
    @Results(id = "areaMap", value = {
            @Result(property = "areaID", column = "area_id", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "universityID", column = "university_id", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "area", column = "area_id", javaType = Map.class, one=@One(select="com.tju.myproject.dao.ActivityDao.getAreaById",fetchType= FetchType.EAGER)),
            @Result(column = "university_id", property = "university", javaType = Map.class ,one = @One(select = "com.tju.myproject.dao.ActivityDao.getUniversityById", fetchType = FetchType.EAGER))
    })
    ArrayList<Map> getUniversityArea();
    @Select("select * from area where id=#{id}")
    Map getAreaById(@Param("id")Integer id);
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
    @Update("update university set name=#{name} where id=#{id}")
    Integer updateUniversity(Map data);
    @Delete("delete from university where id=#{id}")
    Integer deleteUniversity(@Param("id") Integer id);
    @Options(useGeneratedKeys =true, keyProperty = "id")
    @Insert("insert into university (name) VALUES (#{name})")
    Integer addUniversity(Map data);
    @Insert("insert into area_university(area_id, university_id) VALUES (#{areaID}, #{universityID})")
    Integer addAreaUniversity(@Param("areaID")Integer areaID, @Param("universityID")Integer universityID);
    @Insert("delete from area_university where id>0 and university_id = #{universityID}")
    Integer deleteAreaUniversity(@Param("universityID")Integer universityID);
    @Update("update club set name=#{name} where id=#{id}")
    Integer updateClub(Map data);
    @Delete("delete from club where id=#{id}")
    Integer deleteClub(@Param("id") Integer id);
    @Insert("insert into club (name) VALUES (#{name})")
    @Options(useGeneratedKeys = true,keyProperty = "new_id")
    Integer addClub(Map data);
    @Insert("insert into activity (adminID, activityName, activityDate, activityDDL, activityLocation, activityPrice, activityPriceVip, selectedLocation, selectedGood, selectedClub, scheme, delta, showImg, plainText, isIdNes, contact, limitNum) VALUES (#{adminID}, #{activityName},#{activityDate},#{activityDDL},#{activityLocation},#{activityPrice},#{activityPriceVip},#{selectedLocation},#{selectedGood},#{selectedClub},#{scheme},#{delta},#{showImg},#{plainText},#{isIdNes},#{contact}, #{limitNum})")
    Integer addActivity(Map m);
    @Update("update activity set activityName=#{activityName}, activityDate=#{activityDate}, activityDDL=#{activityDDL}, activityLocation=#{activityLocation}, activityPrice=#{activityPrice}, activityPriceVip=#{activityPriceVip}, selectedLocation=#{selectedLocation}, selectedGood=#{selectedGood}, selectedClub=#{selectedClub}, scheme=#{scheme}, delta=#{delta}, showImg=#{showImg}, plainText=#{plainText}, isIdNes=#{isIdNes}, contact=#{contact}, limitNum=#{limitNum} where id=#{activityID}")
    Integer updateActivity(Map m);
    @Select("select * from activity where isDeleted=0 order by id desc")
    ArrayList<Map> getActivities();
    @Select("select * from activity order by id desc")
    ArrayList<Map> getAllActivities();
    @Select("select * from location where id=#{id}")
    Map getLocationById(@Param("id")Integer id);
    @Select("select * from good where id=#{id}")
    Map getGoodById(@Param("id")Integer id);
    @Select("select * from university where id=#{id}")
    Map getUniversityById(@Param("id")Integer id);
    @Insert("insert into user_activity(activityID, userID, name, phone, urgentPhone, idCard, location, scheme, remark, shouldPay,openid, friend) VALUES (#{activityID}, #{userID}, #{name}, #{phone}, #{urgentPhone}, #{idCard}, #{location}, #{scheme}, #{remark}, #{shouldPay},#{openid}, #{friend})")
    Integer activityEnrol(Map data);
    @Update("update user_activity set userID=#{userID}, name=#{name}, phone=#{phone}, urgentPhone=#{urgentPhone},idCard=#{idCard},location=#{location}, scheme=#{scheme}, remark=#{remark}, shouldPay=#{shouldPay},openid=#{openid}, friend=#{friend} where id=#{enrolID}")
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
            @Result(property = "friend", column = "friend", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(column = "{activityID = activityID, userID = userID}", property = "goods",many = @Many(select = "com.tju.myproject.dao.ActivityDao.getActivityUserGoods", fetchType = FetchType.EAGER)),
            @Result(column = "userID", property = "club",many = @Many(select = "com.tju.myproject.dao.UserDao.getUserClubs", fetchType = FetchType.EAGER))
    })
    ArrayList<Map> getActivityUsersInfo(@Param("activityID") Integer activityID);

    @Select("select * from user_activity where userID=#{userID}")
    @Results(id = "userEnrolsMap", value = {
            @Result(property = "id", column = "id", javaType = Integer.class, jdbcType = JdbcType.INTEGER, id = true),
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
            @Result(column = "{activityID = activityID, userID = userID}", property = "goods",many = @Many(select = "com.tju.myproject.dao.ActivityDao.getActivityUserGoods", fetchType = FetchType.EAGER)),
            @Result(property = "activityInfo", column = "activityID", javaType = Map.class, one=@One(select="com.tju.myproject.dao.ActivityDao.getActivityInfoById",fetchType= FetchType.EAGER)),
    })
    ArrayList<Map> getUserActivitiesInfo(@Param("userID") Integer userID);

    @Select("select * from user_activity_good as a left join good as b on a.good=b.id where activityID=#{activityID} and userID=#{userID}")
    ArrayList<Map>getActivityUserGoods(@Param("activityID") Integer activityID, @Param("userID") Integer userID);
    @Select("select * from activity where id=#{activityID}")
    Map getActivityInfoById(@Param("activityID") Integer activityID);
    @Select("select * from swiper")
    ArrayList<Map>getSwiperPics();
    @Update("update activity set isDeleted=#{isDeleted}, delAdminID=#{adminID} where id=#{activityID}")
    Integer deleteActivity(@Param("activityID")Integer activityID,@Param("adminID")Integer adminID,@Param("isDeleted")Integer isDeleted);
    @Select("select count(*) from user_activity where activityID=#{activityID}")
    Integer countEnrollNum(@Param("activityID") Integer activityID);
}

//many = @Many(select = "com.pjb.mapper.UserMapper.getRoleList", fetchType = FetchType.LAZY)