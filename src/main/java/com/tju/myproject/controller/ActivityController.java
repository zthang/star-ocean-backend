package com.tju.myproject.controller;

import com.tju.myproject.entity.ResultEntity;
import com.tju.myproject.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ActivityController {
    @Autowired
    private ActivityService activityService;
    @GetMapping("/api/getLocation")
    public ResultEntity getLocation()
    {
        return activityService.getLocation();
    }
    @GetMapping("/api/getGood")
    public ResultEntity getGood()
    {
        return activityService.getGood();
    }
    @GetMapping("/getUniversity")
    public ResultEntity getUniversity()
    {
        return activityService.getUniversity();
    }
    @PostMapping("/api/addActivity")
    public ResultEntity addActivity(@RequestBody Map data)
    {
        return activityService.addAcivity(data);
    }
    @PostMapping("/api/updateActivity")
    public ResultEntity updateActivity(@RequestBody Map data)
    {
        return activityService.updateAcivity(data);
    }
    @GetMapping("/api/getActivity")
    public ResultEntity addActivity(Integer index,Integer size)
    {
        return activityService.getActivities(index,size);
    }
    @PostMapping("api/activityEnrol")
    public ResultEntity activityEnrol(@RequestBody Map data)
    {
        return activityService.activityEnrol(data);
    }
    @GetMapping("/api/getActivityUsersInfo")
    public ResultEntity getActivityUsersInfo(Integer activityID)
    {
        return activityService.getActivityUsersInfo(activityID);
    }
}
