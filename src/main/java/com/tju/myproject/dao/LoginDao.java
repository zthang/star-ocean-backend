package com.tju.myproject.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Mapper
@Repository
public interface LoginDao {
    @Select("select * from openID where openid=#{openid}")
    Map getSessionByOpenid(@Param("openid")String openid);
    @Insert("insert into openID (openid, session_key) values (#{openid},#{session_key})")
    Integer insertInfo(Map map);
    @Update("update openID set session_key=#{session_key} where openid=#{openid}")
    Integer updateInfo(Map map);
}
