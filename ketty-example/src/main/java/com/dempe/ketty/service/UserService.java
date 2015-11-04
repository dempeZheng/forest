package com.dempe.ketty.service;

import com.dempe.ketty.dao.UserDao;
import com.dempe.ketty.model.User;
import com.dempe.ketty.mvc.anno.Inject;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/22
 * Time: 10:47
 * To change this template use File | Settings | File Templates.
 */
public class UserService {

    @Inject
    private UserDao userDao;

    public User getUserByUid(String uid) {
        return userDao.getUserByUid(uid);
    }
}
