package com.tju.myproject.service.implement;

import com.alibaba.fastjson.JSON;
import com.tju.myproject.dao.ActivityDao;
import com.tju.myproject.entity.ResultEntity;
import com.tju.myproject.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service(value = "activityService")
public class ActivityServiceImp implements ActivityService {
    @Autowired
    private ActivityDao activityDao;
    @Value("${default-img}")
    private String defaultUrl;
    @Override
    public ResultEntity getLocation()
    {
        ArrayList<Map> m=activityDao.getLocation();
        return new ResultEntity(m!=null?200:-1,"",m);
    }

    @Override
    public ResultEntity getGood()
    {
        ArrayList<Map> m=activityDao.getGood();
        return new ResultEntity(m!=null?200:-1,"",m);
    }

    @Override
    public ResultEntity getUniversity()
    {
        ArrayList<Map> m=activityDao.getUniversity();
        return new ResultEntity(m!=null?200:-1,"",m);
    }

    public String getFormatStr(ArrayList<Object> arr, char c)
    {
        StringBuilder builder=new StringBuilder();
        builder.append("[");
        for(Object i:arr)
        {
            builder.append(i);
            builder.append(c);
        }
        builder.deleteCharAt(builder.length()-1);
        builder.append(']');
        return builder.toString();
    }
    @Override
    public ResultEntity addAcivity(Map data)
    {
        data.put("selectedLocation",getFormatStr((ArrayList<Object>) data.get("selectedLocation"),','));
        data.put("selectedGood",getFormatStr((ArrayList<Object>) data.get("selectedGood"),','));
        data.put("selectedUniversity",getFormatStr((ArrayList<Object>) data.get("selectedUniversity"),','));
        data.put("scheme",getFormatStr((ArrayList<Object>) data.get("scheme"),'@'));
        if(data.get("activityPrice")==null||data.get("activityPrice")=="")
            data.put("activityPrice",0);
        if(data.get("showImg")==null)
            data.put("showImg",defaultUrl);
        Integer success = activityDao.addActivity(data);
        return new ResultEntity(success==1?200:-1,success==1?"发布活动成功！":"发布活动失败！",null);
    }
    public ArrayList<Map> handleJson(String str, String c, Integer mode)
    {
        String s=str.substring(1,str.length()-1);
        String[] strArr=s.split(c);
        ArrayList<Map>res=new ArrayList<>();
        ArrayList<Map>source=new ArrayList<>();
        if(mode==1)
            source=activityDao.getLocation();
        else if(mode==2)
            source=activityDao.getGood();
        else if(mode==3)
            source=activityDao.getUniversity();
        for(String t:strArr)
        {
            source.stream().filter(item->item.get("id").equals(Integer.parseInt(t))).findFirst().ifPresent(res::add);
        }
        return res;
    }
    @Override
    public ResultEntity getActivities(Integer index, Integer size)
    {
        ArrayList<Map> m=activityDao.getActivities(index*size,size);
        for(Map map:m)
        {
            map.put("selectedLocation",handleJson((String)map.get("selectedLocation"),",",1));
            map.put("selectedGood",handleJson((String)map.get("selectedGood"),",",2));
            map.put("selectedUniversity",handleJson((String)map.get("selectedUniversity"),",",3));
            String temp=(String)map.get("scheme");
            temp=temp.substring(1,temp.length()-1);
            map.put("scheme",temp.split("@"));
        }
        return new ResultEntity(200,"",m);
    }

    @Override
    @Transactional
    public ResultEntity activityEnrol(Map data)
    {
        Map temp=activityDao.getUserActivity((Integer) data.get("activityID"),(Integer)data.get("userID"));
        if(temp!=null)
            return new ResultEntity(-1,"您已经报名过了!",null);
        data.put("scheme", JSON.toJSONString(data.get("scheme")));
        Integer res1=activityDao.activityEnrol(data);
        Integer res2=0;
        for(Map m:(ArrayList<Map>)data.get("goods"))
        {
            if((Integer) m.get("num")!=0)
            {
                res2-=1;
                m.put("good",m.remove("id"));
                m.put("activityID",data.get("activityID"));
                m.put("userID",data.get("userID"));
                res2+=activityDao.addUserNeedGood(m);
            }
        }
        return new ResultEntity(res1==1&&res2==0?200:-1,"",null);
    }
}
