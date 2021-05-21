package com.tju.myproject.service.implement;

import com.tju.myproject.dao.UserDao;
import com.tju.myproject.entity.User;
import com.tju.myproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service(value = "userService")
public class UserServiceImp implements UserService
{
    @Autowired
    private UserDao userDao;
    @Override
    public int addUser(User user)
    {
        return userDao.addUser(user);
    }

    @Override
    public User getUserByUsername(String username)
    {
        return userDao.getUserByUsername(username);
    }

    @Override
    public User getUserByUserID(Integer userID) {
        return userDao.getUserByUserID(userID);
    }
}
