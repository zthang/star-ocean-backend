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
    public User getUserByUserID(Integer userID) {
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
}
