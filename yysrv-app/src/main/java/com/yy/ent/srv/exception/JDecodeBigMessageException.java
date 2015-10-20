/*
 * Copyright 2012 duowan.com.
 * All Rights Reserved.
 * This program is the confidential and proprietary information of 
 * duowan. ("Confidential Information").  You shall not disclose such
 * Confidential Information and shall use it only in accordance with
 * the terms of the license agreement you entered into with duowan.com.
 */
package com.yy.ent.srv.exception;

/**
 * 当服务器接收到的数据包长度大于8M的时候，会抛异常
 * 然后服务器会断开和对方服务器的连接
 *
 * @author sunxiaodi
 */

public class JDecodeBigMessageException extends JCodecException {

    private static final long serialVersionUID = 7574065487685548393L;

    public JDecodeBigMessageException(String message) {
        super(message);
    }

    public JDecodeBigMessageException(String message, Throwable cause) {
        super(message, cause);
    }

}
