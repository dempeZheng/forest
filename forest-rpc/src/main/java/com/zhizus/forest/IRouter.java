package com.zhizus.forest;

import com.zhizus.forest.core.ActionMethod;

/**
 * Created by Dempe on 2016/12/9.
 */
public interface IRouter {

    ActionMethod router(String uri);
}
