package com.tju.myproject.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Map;

@Mapper
@Repository
public interface PaymentDao {
    @Insert("insert into user_activity_order (out_trade_no, userID, activityID, amount) VALUES (#{out_trade_no}, #{userID}, #{activityID}, #{amount})")
    Integer addUserPayment(Map data);
    @Insert("insert into user_vip_order (out_trade_no, userID, amount) VALUES (#{out_trade_no}, #{userID}, #{amount})")
    Integer addUserVipPayment(Map data);
    @Update("update user_activity_order set trade_no=#{trade_no}, in_trade_no=#{in_trade_no}, create_time=#{create_time}, pay_time=#{pay_time} where out_trade_no=#{out_trade_no}")
    Integer updatePayment(Map data);
    @Update("update user_vip_order set trade_no=#{trade_no}, in_trade_no=#{in_trade_no}, create_time=#{create_time}, pay_time=#{pay_time} where out_trade_no=#{out_trade_no}")
    Integer updateVipPayment(Map data);
    @Select("select in_trade_no, amount from user_activity_order where userID=#{userID} and activityID=#{activityID}")
    ArrayList<Map> getTradeNumber(Map data);
    @Select("select in_trade_no, amount from user_vip_order where userID=#{userID}")
    ArrayList<Map> getVipTradeNumber(@Param("userID")Integer userID);
}
