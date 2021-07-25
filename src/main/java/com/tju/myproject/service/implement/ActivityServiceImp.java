package com.tju.myproject.service.implement;

import com.alibaba.fastjson.JSON;
import com.tju.myproject.dao.ActivityDao;
import com.tju.myproject.entity.ResultEntity;
import com.tju.myproject.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.util.*;
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

    @Override
    public ResultEntity getClub()
    {
        ArrayList<Map> m=activityDao.getClub();
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
        if(builder.length()>1)
            builder.deleteCharAt(builder.length()-1);
        builder.append(']');
        return builder.toString();
    }
    @Override
    public ResultEntity addAcivity(Map data)
    {
        data.put("selectedLocation",getFormatStr((ArrayList<Object>) data.get("selectedLocation"),','));
        data.put("selectedGood",getFormatStr((ArrayList<Object>) data.get("selectedGood"),','));
        data.put("selectedClub",getFormatStr((ArrayList<Object>) data.get("selectedClub"),','));
        data.put("scheme",getFormatStr((ArrayList<Object>) data.get("scheme"),'@'));
        if(data.get("activityPrice")==null||data.get("activityPrice")=="")
            data.put("activityPrice",0);
        if(data.get("showImg")==null)
            data.put("showImg",defaultUrl);
        Integer success = activityDao.addActivity(data);
        return new ResultEntity(success==1?200:-1,success==1?"发布活动成功！":"发布活动失败！",null);
    }

    @Override
    public ResultEntity updateAcivity(Map data)
    {
        data.put("selectedLocation",getFormatStr((ArrayList<Object>) data.get("selectedLocation"),','));
        data.put("selectedGood",getFormatStr((ArrayList<Object>) data.get("selectedGood"),','));
        data.put("selectedClub",getFormatStr((ArrayList<Object>) data.get("selectedClub"),','));
        data.put("scheme",getFormatStr((ArrayList<Object>) data.get("scheme"),'@'));
        if(data.get("activityPrice")==null||data.get("activityPrice")=="")
            data.put("activityPrice",0);
        if(data.get("showImg")==null)
            data.put("showImg",defaultUrl);
        Integer success = activityDao.updateActivity(data);
        return new ResultEntity(success==1?200:-1,success==1?"修改活动成功！":"修改活动失败！",null);
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
            source=activityDao.getClub();
        for(String t:strArr)
        {
            if(t.length()>0)
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
            map.put("selectedLocation",handleJson(map.get("selectedLocation").toString(),",",1));
            map.put("selectedGood",handleJson(map.get("selectedGood").toString(),",",2));
            map.put("selectedClub",handleJson(map.get("selectedClub").toString(),",",3));
            String temp=(String)map.get("scheme");
            temp=temp.substring(1,temp.length()-1);
            List<Object>schemeList=new ArrayList<>(Arrays.asList(temp.split("@")));
            for(Integer i=0;i<schemeList.size();i++)
            {
                Map t=new HashMap();
                String price=schemeList.get(i).toString().split("=")[1];
                t.put("text",schemeList.get(i).toString().replace('=',',')+"元/人");
                t.put("price",price);
                schemeList.set(i,t);
            }
            map.put("scheme",schemeList);
        }
        return new ResultEntity(200,"",m);
    }

    @Override
    public ResultEntity getActivitiesByUser(Map data)
    {
        Integer index=Integer.parseInt(data.get("index").toString());
        Integer size=Integer.parseInt(data.get("size").toString());
        ArrayList<String>clubList=(ArrayList<String>) data.get("club");
        ArrayList<Map> m=activityDao.getAllActivities();
        ArrayList<Map> res=new ArrayList<>();
        for(Map map:m)
        {
            String temp=map.get("selectedClub").toString();
            String s=temp.substring(1,temp.length()-1);
            ArrayList<String> strArr=new ArrayList<>(Arrays.asList(s.split(",")));
            List<String> collect = strArr.stream().filter(num -> clubList.contains(num)).collect(Collectors.toList());
            if(collect.size()==0&&!clubList.get(0).equals("undefined"))
                continue;
            map.put("selectedLocation",handleJson((String)map.get("selectedLocation"),",",1));
            map.put("selectedGood",handleJson((String)map.get("selectedGood"),",",2));
            temp=map.get("scheme").toString();
            temp=temp.substring(1,temp.length()-1);
            List<Object>schemeList=new ArrayList<>(Arrays.asList(temp.split("@")));
            for(Integer i=0;i<schemeList.size();i++)
            {
                Map t=new HashMap();
                String price=schemeList.get(i).toString().split("=")[1];
                t.put("text",schemeList.get(i).toString().replace('=',',')+"元/人");
                t.put("price",price);
                schemeList.set(i,t);
            }
            map.put("scheme",schemeList);
            res.add(map);
        }
        return new ResultEntity(200,"",res.subList(index*size,Math.min(size*(index+1),res.size())));
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

    @Override
    @Transactional
    public ResultEntity updateActivityEnrol(Map data)
    {
        data.put("scheme", JSON.toJSONString(data.get("scheme")));
        Integer res1=activityDao.updateActivityEnrol(data);
        Integer res2=0;
        activityDao.deleteUserNeedGood(data);
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

    @Override
    public ResultEntity getActivityUsersInfo(Integer activityID)
    {
        ArrayList<Map>enrolInfo=activityDao.getActivityUsersInfo(activityID);
        Map resMap=new HashMap();
        Map<Integer,ArrayList<Map>>locationMap=new HashMap<>();
        Map<Integer,ArrayList<Map>> goodsMap=new HashMap();
        Map<String,ArrayList<Map>>schemeMap=new HashMap();
        resMap.put("allInfo", enrolInfo);
        for(Map m:enrolInfo)
        {
            m.put("scheme",JSON.parse((String)m.get("scheme")));
            locationMap.compute((Integer) ((HashMap)m.get("location")).get("id"),(k,v)->v==null?new ArrayList<>():v).add(m);
            for(Map good:(ArrayList<Map>)m.get("goods"))
            {
                Map mTemp=new HashMap();
                mTemp.putAll(m);
                mTemp.put("num",good.get("num"));
                goodsMap.compute((Integer) good.get("good"),(k,v)->v==null?new ArrayList<>():v).add(mTemp);
            }
            schemeMap.compute((String)((Map)m.get("scheme")).get("text"),(k,v)->v==null?new ArrayList<>():v).add(m);
        }
        resMap.put("locationInfo",locationMap);
        resMap.put("goodInfo",goodsMap);
        resMap.put("schemeInfo",schemeMap);

        return new ResultEntity(200,"",resMap);
    }

    @Override
    @Transactional
    public ResultEntity updateLocation(Map data)
    {
        ArrayList<Map>items=(ArrayList<Map>) data.get("items");
        Integer res=0;
        for(Map item:items)
        {
            if(!item.containsKey("state"))
                continue;
            else {
                res-=1;
                Integer state=Integer.parseInt(item.get("state").toString());
                if(state==1)
                    res+=activityDao.addLocation(item.get("name").toString());
                else if(state==2)
                    res+=activityDao.updateLocation(item);
                else if(state==3)
                    res+=activityDao.deleteLocation(Integer.parseInt(item.get("id").toString()));
            }
        }
        return new ResultEntity(res==0?200:-1,res==0?"":"更新地点信息失败",null);
    }
    @Override
    @Transactional
    public ResultEntity updateGood(Map data)
    {
        ArrayList<Map>items=(ArrayList<Map>) data.get("items");
        Integer res=0;
        for(Map item:items)
        {
            if(!item.containsKey("state"))
                continue;
            else {
                res-=1;
                Integer state=Integer.parseInt(item.get("state").toString());
                if(state==1)
                    res+=activityDao.addGood(item);
                else if(state==2)
                    res+=activityDao.updateGood(item);
                else if(state==3)
                    res+=activityDao.deleteGood(Integer.parseInt(item.get("id").toString()));
            }
        }
        return new ResultEntity(res==0?200:-1,res==0?"":"更新物品信息失败",null);
    }

    @Override
    public ResultEntity checkIfEnrolled(Map data)
    {
        Map temp=activityDao.getUserActivity((Integer) data.get("activityID"),(Integer)data.get("userID"));
        if(temp!=null)
            return new ResultEntity(200,"",1);
        else
            return new ResultEntity(200,"",0);
    }
}
