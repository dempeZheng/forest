package com.zhizus.forest.common.util;


import com.google.common.base.Strings;
import com.zhizus.forest.common.CompressType;
import com.zhizus.forest.common.SerializeType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

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


    public static String getDefaultBeanName(Class<?> classType) {
        return StringUtils.uncapitalize(classType.getSimpleName());
    }

    public static String getBeanId(Element element, Class<?> classType) {
        String id = element.getAttribute("id");
        return Strings.isNullOrEmpty(id) ? StringUtils.uncapitalize(classType.getSimpleName()) : id;
    }


}
