package com.tju.myproject.service;

import com.tju.myproject.entity.ResultEntity;

import java.util.Map;

public interface ActivityService {
    ResultEntity getLocation();
    ResultEntity getGood();
    ResultEntity getUniversity();
    ResultEntity addAcivity(Map data);
    ResultEntity updateAcivity(Map data);
    ResultEntity getActivities(Integer index,Integer size);
    ResultEntity activityEnrol(Map data);
    ResultEntity getActivityUsersInfo(Integer activityID);
}
