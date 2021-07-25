package com.tju.myproject.service.implement;

import com.tju.myproject.dao.UserDao;
import com.tju.myproject.entity.ResultEntity;
import com.tju.myproject.entity.User;
import com.tju.myproject.service.LoginService;
import com.tju.myproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service(value = "userService")
public class UserServiceImp implements UserService
{
    @Autowired
    private UserDao userDao;
    @Autowired
    private LoginService loginService;
    @Override
    public int addUser(User user)
    {
        return userDao.addUser(user);
    }

    @Override
    public User getUserByPhone(String phone)
    {
        return userDao.getUserByPhone(phone);
    }

    @Override
    public HashMap getUserByUserID(Integer userID) {
        return userDao.getUserByUserID(userID);
    }

    @Override
    public ResultEntity getUserAuthInfo(Integer index, Integer size)
    {
        ArrayList<Map> m=userDao.getUserAuthInfo(index*size,size);
        return new ResultEntity(m!=null?200:-1,m!=null?"":"查询出错，请重试！",m);
    }

    @Override
    @Transactional
    public ResultEntity addUserAuth(Map m)
    {
        Integer res1=userDao.addUserAuth(m);
        Integer res2=userDao.updateUserAuth(0,(Integer) m.get("userID"));
        return new ResultEntity(res1==1&&res2==1?200:-1,"",null);
    }

    @Override
    @Transactional
    public ResultEntity passAuth(Map m)
    {
        Integer res1=userDao.updateAuth(m);
        Integer res2=userDao.updateUserAuth(1,(Integer)m.get("userID"));
        loginService.messagePush(m);
        return new ResultEntity(res1==1&&res2==1?200:-1,"",null);
    }
    @Override
    @Transactional
    public ResultEntity notPassAuth(Map m)
    {
        Integer res1=userDao.updateAuth(m);
        Integer res2=userDao.updateUserAuth(-1,(Integer)m.get("userID"));
        loginService.messagePush(m);
        return new ResultEntity(res1==1&&res2==1?200:-1,"",null);
    }

    @Override
    public ResultEntity getUsersByName(String name)
    {
        return new ResultEntity(200,"",userDao.getUsersByName(name));
    }

    @Override
    @Transactional
    public ResultEntity addUserInfo(Map data)
    {
        Map u=userDao.getUserByInfo(data);
        if(u==null)
        {
            Integer res=-1;
            res+=userDao.addUserInfo(data);
            for(Map club:(ArrayList<Map>)data.get("club"))
            {
                res-=1;
                club.put("userID",Integer.parseInt(data.get("id").toString()));
                res+=userDao.addUserClub(club);
            }
            return new ResultEntity(res==0?200:-1,res==0?"":"添加用户失败!",null);
        }
        else
        {
            return new ResultEntity(-1,"用户已存在!",null);
        }
    }

    @Override
    @Transactional
    public ResultEntity updateUserInfo(Map data)
    {
        ArrayList<Map>items=(ArrayList<Map>) data.get("items");
        Integer res=0;
        for(Map item:items)
        {
            if(!item.containsKey("state"))
                continue;
            else {
                res-=1;
                Map u=userDao.getUserByInfo(item);
                if(u==null||u.get("id").toString().equals(item.get("id").toString()))
                {
                    res+=userDao.updateUserInfo(item);
                    ArrayList<Map>clubs=(ArrayList<Map>)item.get("club");
                    for(Map club:clubs)
                    {
                        if(!club.containsKey("state"))
                            continue;
                        else {
                            res-=1;
                            Integer state=Integer.parseInt(club.get("state").toString());
                            if(state==1)
                                res+=userDao.addUserClub(club);
                            else if(state==3)
                                res+=userDao.deleteUserClub(Integer.parseInt(club.get("id").toString()));
                        }
                    }
                }
                else {
                    return new ResultEntity(-1,"更新失败,用户已存在!",null);
                }
            }
        }
        return new ResultEntity(res==0?200:-1,res==0?"":"更新用户信息失败!",null);
    }
}
