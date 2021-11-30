package com.tju.myproject.service.implement;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tju.myproject.dao.ActivityDao;
import com.tju.myproject.dao.UserDao;
import com.tju.myproject.entity.ResultEntity;
import com.tju.myproject.service.ActivityService;
import com.tju.myproject.utils.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service(value = "activityService")
public class ActivityServiceImp implements ActivityService {
    @Autowired
    private ActivityDao activityDao;
    @Autowired
    private UserDao userDao;
    @Value("${default-img}")
    private String defaultUrl;
    @Value("${excel-export-path}")
    private String excelSavePath;
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
        if(data.get("activityPriceVip")==null||data.get("activityPriceVip")=="")
            data.put("activityPriceVip",0);
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
        if(data.get("activityPriceVip")==null||data.get("activityPriceVip")=="")
            data.put("activityPriceVip",0);
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
    public ResultEntity getActivities(Integer index, Integer size, Integer adminID, Integer role)
    {
        ArrayList<Map> m=activityDao.getAllActivities();
        ArrayList<Map> resList=new ArrayList<>();
        for(Map map:m)
        {
            Integer tempAdminID=(Integer)map.get("adminID");
            if(!tempAdminID.equals(adminID)&&role!=3)
                continue;
            map.put("selectedLocation",handleJson(map.get("selectedLocation").toString(),",",1));
            map.put("selectedGood",handleJson(map.get("selectedGood").toString(),",",2));
            map.put("selectedClub",handleJson(map.get("selectedClub").toString(),",",3));
            String temp=(String)map.get("scheme");
            temp=temp.substring(1,temp.length()-1);
            List<Object>schemeList=!temp.equals("")?new ArrayList<>(Arrays.asList(temp.split("@"))):new ArrayList<>();
            for(Integer i=0;i<schemeList.size();i++)
            {
                Map t=new HashMap();
                String price=schemeList.get(i).toString().split("=")[1];
                t.put("text",schemeList.get(i).toString().replace('=',',')+"元/人");
                t.put("price",price);
                schemeList.set(i,t);
            }
            map.put("scheme",schemeList);
            resList.add(map);
        }
        Integer to=Math.min(size*(index+1),resList.size());
        Integer from=Math.min(index*size, to);
        return new ResultEntity(200,"",resList.subList(from,to));
    }

    @Override
    public ResultEntity getActivitiesByUser(Map data)
    {
        Integer index=Integer.parseInt(data.get("index").toString());
        Integer size=Integer.parseInt(data.get("size").toString());
        ArrayList<String>clubList=(ArrayList<String>) data.get("club");
        ArrayList<Map> m=activityDao.getActivities();
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
            List<Object>schemeList=!temp.equals("")?new ArrayList<>(Arrays.asList(temp.split("@"))):new ArrayList<>();
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
        Integer to=Math.min(size*(index+1),res.size());
        Integer from=Math.min(index*size, to);
        return new ResultEntity(200,"",res.subList(from,to));
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
        String phone = data.get("phone").toString();
        String idCard = data.get("idCard").toString();
        Integer userID = Integer.parseInt(data.get("userID").toString());
        if(phone.length()>8)
        {
            userDao.updatePhone(phone, userID);
        }
        if(idCard.length()>8)
        {
            userDao.updateIdCard(idCard, userID);
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
    public ResultEntity getActivityUsersInfo(Integer activityID, Integer index, Integer size, Integer mode, String key)
    {
        ArrayList<Map>enrolInfo=activityDao.getActivityUsersInfo(activityID);
        ArrayList<Map>locationList=new ArrayList<>();
        ArrayList<Map> goodsList=new ArrayList<>();
        ArrayList<Map> schemeList=new ArrayList<>();
        ArrayList<Map>resList=null;
        Collections.reverse(enrolInfo);
        for(Map m:enrolInfo)
        {
            m.put("tempText","");
            m.put("selected",false);
        }
        if(mode==0)
        {
            Integer to=Math.min(size*(index+1),enrolInfo.size());
            Integer from=Math.min(index*size, to);
            return new ResultEntity(200,"",enrolInfo.subList(from, to));
        }
        for(Map m:enrolInfo)
        {
            if(mode==1)
            {
                String locationID=((HashMap)m.get("location")).get("id").toString();
                if(locationID.equals(key))
                {
                    locationList.add(m);
                }
            }
            else if(mode==2)
            {
                for(Map good:(ArrayList<Map>)m.get("goods"))
                {
                    String goodID=good.get("good").toString();
                    if(!goodID.equals(key))
                        continue;
                    Map mTemp=new HashMap();
                    mTemp.putAll(m);
                    mTemp.put("num",good.get("num"));
                    goodsList.add(mTemp);
                }
            }
            else if(mode==3)
            {
                if(m.get("scheme")!=null&&!m.get("scheme").toString().equals("")&&!m.get("scheme").toString().equals("null"))
                {
                    m.put("scheme",JSON.parse((String)m.get("scheme")));
                    String schemeID=((Map)m.get("scheme")).get("text").toString();
                    if(schemeID.equals(key))
                    {
                        schemeList.add(m);
                    }
                }
            }
        }
        if(mode==1)
        {
            resList=locationList;
        }
        else if(mode==2)
        {
            resList=goodsList;
            for(Map tempMap:resList)
            {
                tempMap.put("tempText","个数:" + tempMap.get("num"));
            }
        }
        else if(mode==3)
        {
            resList=schemeList;
        }
        Integer to=Math.min(size*(index+1),resList.size());
        Integer from=Math.min(index*size, to);
        return new ResultEntity(200,"",resList.subList(from, to));
    }

    @Override
    public ResultEntity exportExcel(Integer activityID) {
        ArrayList<Map>enrolInfo=activityDao.getActivityUsersInfo(activityID);
        List<String> columnList = new ArrayList<>();
        columnList.add("姓名");
        columnList.add("电话");
        columnList.add("紧急联系人电话");
        columnList.add("身份证");
        columnList.add("社团");
        columnList.add("备注");
        columnList.add("上车地点");
        columnList.add("物资信息");
        columnList.add("住宿信息");
        columnList.add("配对人员");
        List<List<String>> dataList = new ArrayList<>();
        ArrayList<Map>goods=activityDao.getGood();
        Map<Integer, String>goodMap=new HashMap<>();
        for(Map m:goods)
        {
            goodMap.put((Integer) m.get("id"), m.get("name").toString());
        }
        for(Map m:enrolInfo)
        {
            List<String> rowList = new ArrayList<>();
            rowList.add(m.get("name").toString());
            rowList.add(m.get("phone").toString());
            rowList.add(m.get("urgentPhone").toString());
            rowList.add(m.get("idCard").toString());
            rowList.add(((Map)((ArrayList)m.get("club")).get(0)).get("name").toString());
            rowList.add(m.get("remark").toString());
            rowList.add(((Map)m.get("location")).get("name").toString());
            String goodStr="";
            for(Map good:(ArrayList<Map>)m.get("goods"))
            {
                goodStr+=goodMap.get((Integer) good.get("good"));
                goodStr+=good.get("num").toString();
                goodStr+="个 ";
            }
            rowList.add(goodStr);
            JSONObject jsonObj = JSON.parseObject(m.get("scheme").toString());
            rowList.add(jsonObj.get("text").toString());
            rowList.add(m.containsKey("friend")?m.get("friend").toString():"无");
            dataList.add(rowList);
        }
        String fileName="activity"+activityID.toString()+".xls";
        ExcelUtil.uploadExcelAboutUser(excelSavePath+fileName,columnList,dataList);
        return new ResultEntity(200, "", "https://xzxjlljh.xyz:8787/excel/"+fileName);
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
    public ResultEntity updateUniversity(Map data)
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
                {
                    res -= 1;
                    res += activityDao.addUniversity(item);
                    res += activityDao.addAreaUniversity(Integer.parseInt(data.get("area").toString()), Integer.parseInt(item.get("id").toString()));
                }
                else if(state==2)
                    res+=activityDao.updateUniversity(item);
                else if(state==3)
                {
                    res -= 1;
                    res += activityDao.deleteUniversity(Integer.parseInt(item.get("id").toString()));
                    res += activityDao.deleteAreaUniversity(Integer.parseInt(item.get("id").toString()));
                }
            }
        }
        return new ResultEntity(res==0?200:-1,res==0?"":"更新学校信息失败",null);
    }
    @Override
    @Transactional
    public ResultEntity updateClub(Map data)
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
                if(state==1) {
                    res += activityDao.addClub(item);
                    res-=3;
                    for(Integer i=2557;i<2560;i++)
                    {
                        Map<String, Integer> tempMap=new HashMap<>();
                        tempMap.put("userID", i);
                        tempMap.put("clubID", Integer.parseInt(item.get("new_id").toString()));
                        res+=userDao.addUserClub(tempMap);
                    }
                }
                else if(state==2)
                    res+=activityDao.updateClub(item);
                else if(state==3) {
                    res += activityDao.deleteClub(Integer.parseInt(item.get("id").toString()));
                    res -= 3;
                    for(Integer i=2557;i<2560;i++)
                    {
                        res+=userDao.deleteUserClubByInfo(i, Integer.parseInt(item.get("id").toString()));
                    }
                }
            }
        }
        return new ResultEntity(res==0?200:-1,res==0?"":"更新社团信息失败",null);
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

    @Override
    public ResultEntity getALlSwiperPics() {
        return new ResultEntity(200, "", activityDao.getSwiperPics());
    }

    @Override
    public ResultEntity getUserActivitiesInfo(Integer userID) {
        return new ResultEntity(200,"",activityDao.getUserActivitiesInfo(userID));
    }

    @Override
    public ResultEntity deleteActivity(Integer activityID, Integer adminID, Integer isDeleted)
    {
        isDeleted=isDeleted==0?1:0;
        Integer success=activityDao.deleteActivity(activityID,adminID,isDeleted);
        return new ResultEntity(success==1?200:-1,success==1?"操作成功":"操作失败",null);
    }

    @Override
    public ResultEntity countEnrollNum(Integer activityID)
    {
        return new ResultEntity(200, "", activityDao.countEnrollNum(activityID));
    }

    @Override
    public ResultEntity getUniversityArea() {
        ArrayList<Map> m = activityDao.getUniversityArea();
        Map<String, ArrayList<Map>> resMap = new HashMap<>();
        for(Map item:m)
        {
            String key = ((Map)item.get("area")).get("name").toString();
            if(!resMap.containsKey(key))
                resMap.put(key, new ArrayList<>());
            resMap.get(key).add(((Map)item.get("university")));
        }
        return new ResultEntity(200, "", resMap);
    }
    @Override
    public ResultEntity getArea()
    {
        return new ResultEntity(200, "", activityDao.getArea());
    }
}
