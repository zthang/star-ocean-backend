package com.tju.myproject.service;

import com.tju.myproject.entity.ResultEntity;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface ActivityService {
    ResultEntity getLocation();
    ResultEntity getGood();
    ResultEntity getUniversity();
    ResultEntity getClub();
    ResultEntity addAcivity(Map data);
    ResultEntity updateAcivity(Map data);
    ResultEntity getActivities(Integer index,Integer size);
    ResultEntity activityEnrol(Map data);
    ResultEntity updateActivityEnrol(Map data);
    ResultEntity getActivityUsersInfo(Integer activityID);
    ResultEntity updateLocation(Map data);
    ResultEntity updateGood(Map data);
    ResultEntity getActivitiesByUser(Map data);
    ResultEntity checkIfEnrolled(Map data);
}
