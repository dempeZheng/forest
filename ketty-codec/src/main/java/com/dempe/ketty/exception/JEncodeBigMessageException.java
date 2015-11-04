/*
 * Copyright 2012 duowan.com.
 * All Rights Reserved.
 * This program is the confidential and proprietary information of 
 * duowan. ("Confidential Information").  You shall not disclose such
 * Confidential Information and shall use it only in accordance with
 * the terms of the license agreement you entered into with duowan.com.
 */
package com.dempe.ketty.exception;

/**
 * 发送包的长度超过8M的时候，会抛异常，服务器会丢弃该包
 *
 * @author sunxiaodi
 */
public class JEncodeBigMessageException extends JCodecException {

    private static final long serialVersionUID = -7958925995971271608L;

    public JEncodeBigMessageException(String message) {
        super(message);
    }

    public JEncodeBigMessageException(String message, Throwable cause) {
        super(message, cause);
    }
}
