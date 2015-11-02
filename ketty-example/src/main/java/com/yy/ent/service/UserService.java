package com.yy.ent.service;

import com.yy.ent.dao.UserDao;
import com.yy.ent.mvc.anno.Inject;
import com.yy.ent.model.User;

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
