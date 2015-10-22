package com.yy.ent.dao;

import com.yy.ent.model.User;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/22
 * Time: 10:46
 * To change this template use File | Settings | File Templates.
 */
public class UserDao {

    public User getUserByUid(String uid) {
        User user = new User();
        user.setName("demo");
        user.setUid(uid);
        return user;
    }
}
