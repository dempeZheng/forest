package com.dempe.ketty.intercetptor;

import com.alibaba.fastjson.JSONObject;
import com.dempe.ketty.mvc.anno.Around;
import com.dempe.ketty.protocol.KettyRequest;
import com.dempe.ketty.srv.interceptor.BaseInterceptor;
import com.dempe.ketty.srv.ketty.KettyRequestContext;
import com.dempe.ketty.srv.ketty.KettyServerContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/11/4
 * Time: 15:21
 * To change this template use File | Settings | File Templates.
 */
@Around
public class PermissionInterceptor extends BaseInterceptor {

    private final static Logger LOGGER = LoggerFactory.getLogger(PermissionInterceptor.class);

    @Override
    public boolean before() {
        KettyRequestContext reqCxt = KettyServerContext.getReqCxt();
        LOGGER.info("==============PermissionInterceptor before=========context:{}", reqCxt);
        KettyRequest request = reqCxt.getRequest();
        JSONObject parameter = request.getParameter();
        // 参数为空直接过滤
        if (parameter == null) {
            return false;
        }
        String uid = parameter.getString("uid");
        if (StringUtils.isBlank(uid) || !StringUtils.equals(uid, "1234567")) {
            return false;
        }

        return true;
    }
}
