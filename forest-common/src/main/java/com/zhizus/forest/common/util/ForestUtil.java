package com.zhizus.forest.common.util;


import com.zhizus.forest.common.CompressType;
import com.zhizus.forest.common.SerializeType;
import com.zhizus.forest.common.annotation.MethodExport;
import com.zhizus.forest.common.exception.ForestErrorMsgConstant;
import com.zhizus.forest.common.exception.ForestFrameworkException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Dempe on 2016/12/7.
 */
public class ForestUtil {

    private final static Logger LOGGER = LoggerFactory.getLogger(ForestUtil.class);


    public static byte getExtend(SerializeType serializeType, CompressType invokeType) {
        return (byte) (serializeType.getValue() | invokeType.getValue());
    }

    public static String buildUri(String actionBeanName, String uri) {
        return "/" + actionBeanName + "/" + uri;
    }


    public static String getGroup(Method method) {
        MethodExport methodExport = method.getAnnotation(MethodExport.class);
        return methodExport.group();
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

    public static Method findMethodByInterfaceMethod(Method interfaceMethod, Class<?> targetClass) {
        for (Method method : targetClass.getMethods()) {
            if (StringUtils.equals(method.getName(), interfaceMethod.getName())) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                Class<?>[] interfaceMethodParameterTypes = interfaceMethod.getParameterTypes();
                if (parameterTypes.length != interfaceMethodParameterTypes.length) {
                    continue;
                }
                for (int i = 0; i < parameterTypes.length; i++) {
                    if (!StringUtils.equals(parameterTypes[i].getName(), interfaceMethodParameterTypes[i].getName())) {
                        break;
                    }
                    return method;
                }
            }
        }
        return null;

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
