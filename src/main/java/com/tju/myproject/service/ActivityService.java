package com.tju.myproject.service;

import com.tju.myproject.entity.ResultEntity;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface ActivityService {
    ResultEntity getLocation();
    ResultEntity getGood();
    ResultEntity getUniversity();
    ResultEntity getClub();
    ResultEntity addAcivity(Map data);
    ResultEntity updateAcivity(Map data);
    ResultEntity getActivities(Integer index,Integer size,Integer adminID,Integer role);
    ResultEntity activityEnrol(Map data);
    ResultEntity updateActivityEnrol(Map data);
    ResultEntity getActivityUsersInfo(Integer activityID, Integer index, Integer size, Integer mode, String key);
    ResultEntity updateLocation(Map data);
    ResultEntity updateUniversity(Map data);
    ResultEntity updateClub(Map data);
    ResultEntity updateGood(Map data);
    ResultEntity getActivitiesByUser(Map data);
    ResultEntity checkIfEnrolled(Map data);
    ResultEntity getALlSwiperPics();
    ResultEntity getUserActivitiesInfo(Integer userID);
    ResultEntity deleteActivity(Integer activityID,Integer adminID, Integer isDeleted);
    ResultEntity exportExcel(Integer activityID);
    ResultEntity countEnrollNum(Integer activityID);
    ResultEntity getUniversityArea();
    ResultEntity getArea();
}
