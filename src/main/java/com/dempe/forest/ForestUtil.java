package com.dempe.forest;


import com.dempe.forest.core.CompressType;
import com.dempe.forest.core.SerializeType;
import com.dempe.forest.core.annotation.Action;
import com.dempe.forest.core.annotation.Export;
import com.dempe.forest.core.exception.ForestErrorMsgConstant;
import com.dempe.forest.core.exception.ForestFrameworkException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/30
 * Time: 11:58
 * To change this template use File | Settings | File Templates.
 */
public class ForestUtil {

    private final static Logger LOGGER = LoggerFactory.getLogger(ForestUtil.class);


    public static byte getExtend(SerializeType serializeType, CompressType invokeType) {
        return (byte) (serializeType.getValue() | invokeType.getValue());
    }

    public static String buildUri(String actionBeanName, String uri) {
        return "/" + actionBeanName + "/" + uri;
    }

    public static String buildUri(Object target, Method method) {
        Action action = target.getClass().getAnnotation(Action.class);
        String actionValue = action.value();
        Export export = method.getAnnotation(Export.class);
        String uri = export.uri();
        return ForestUtil.buildUri(actionValue, uri);
    }

    public static String getGroup(Method method) {
        Export export = method.getAnnotation(Export.class);
        return export.group();
    }


    public static String getMethodSign(String methodName, String paramtersDesc) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(methodName).append(paramtersDesc);
            String surfix = md5LowerCase(sb.toString()).substring(8, 20); // 取32位md5的8-20位。
            int endIndex = methodName.length() > 4 ? 4 : methodName.length();
            String prefix = methodName.substring(0, endIndex);
            return prefix + surfix;
        } catch (Exception e) {
            throw new ForestFrameworkException("gen method sign error! ", ForestErrorMsgConstant.FRAMEWORK_DECODE_ERROR);
        }

    }

    /*
   * 全小写32位MD5
   */
    public static String md5LowerCase(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuilder buf = new StringBuilder("");
            for (byte element : b) {
                i = element;
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("md5 digest error!", e);
        }
        return null;
    }
}
