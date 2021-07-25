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
    @GetMapping("/api/getClub")
    public ResultEntity getClub()
    {
        return activityService.getClub();
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
    public ResultEntity getActivity(Integer index,Integer size)
    {
        return activityService.getActivities(index,size);
    }
    @PostMapping("/api/getActivityByUser")
    public ResultEntity getActivityByUser(@RequestBody Map data)
    {
        return activityService.getActivitiesByUser(data);
    }
    @PostMapping("api/activityEnrol")
    public ResultEntity activityEnrol(@RequestBody Map data)
    {
        return activityService.activityEnrol(data);
    }
    @PostMapping("api/updateEnrol")
    public ResultEntity updateActivityEnrol(@RequestBody Map data)
    {
        return activityService.updateActivityEnrol(data);
    }
    @GetMapping("/api/getActivityUsersInfo")
    public ResultEntity getActivityUsersInfo(Integer activityID)
    {
        return activityService.getActivityUsersInfo(activityID);
    }
    @PostMapping("/api/updateLocation")
    public ResultEntity updateLocation(@RequestBody Map data)
    {
        return activityService.updateLocation(data);
    }
    @PostMapping("/api/updateGood")
    public ResultEntity updateGood(@RequestBody Map data)
    {
        return activityService.updateGood(data);
    }
    @PostMapping("/api/checkIfEnrolled")
    public ResultEntity checkIfEnrolled(@RequestBody Map data)
    {
        return activityService.checkIfEnrolled(data);
    }
}
