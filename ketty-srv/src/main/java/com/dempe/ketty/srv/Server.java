/*
 * Copyright (c) 2011 duowan.com. 
 * All Rights Reserved.
 * This program is the confidential and proprietary information of 
 * duowan. ("Confidential Information").  You shall not disclose such
 * Confidential Information and shall use it only in accordance with
 * the terms of the license agreement you entered into with duowan.com.
 */
package com.dempe.ketty.srv;


import com.dempe.ketty.srv.exception.JServerException;

/*

 */
public interface Server {

    /**
     * 启动服务器
     *
     * @throws JServerException
     */
    public void start() throws JServerException;

    /**
     * 停止服务器
     *
     * @throws JServerException
     */
    public void stop() throws JServerException;
}
