package com.dempe.ketty.srv.uitl;

import com.alibaba.fastjson.JSON;
import com.dempe.ketty.srv.exception.ModelConvertJsonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/11/3
 * Time: 13:22
 * To change this template use File | Settings | File Templates.
 */
public class ResultConcert {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResultConcert.class);

    /**
     * 将对象转换成JSONString
     *
     * @param result
     * @return
     * @throws com.dempe.ketty.srv.exception.ModelConvertJsonException
     */
    public static String toJSONString(Object result) throws ModelConvertJsonException {
        if (result instanceof String) {
            return result.toString();
        }
        String data = null;
        try {
            data = JSON.toJSONString(result);
        } catch (Exception e) {
            LOGGER.error("model convert 2 json err:{} parse json error", result);
            throw new ModelConvertJsonException("model convert 2 json err");
        }
        return data;
    }
}
