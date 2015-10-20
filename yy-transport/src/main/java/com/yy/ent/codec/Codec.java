/*
 * Copyright 2012 duowan.com.
 * All Rights Reserved.
 * This program is the confidential and proprietary information of 
 * duowan. ("Confidential Information").  You shall not disclose such
 * Confidential Information and shall use it only in accordance with
 * the terms of the license agreement you entered into with duowan.com.
 */
package com.yy.ent.codec;


import com.yy.ent.srv.exception.JCodecException;

import java.nio.ByteBuffer;

/**
 * @author hongyuan
 */
public interface Codec {


    /**
     * @param buffer
     * @return
     * @throws JCodecException
     */
    public Object decode(ByteBuffer buffer) throws JCodecException;

    /**
     * @param msg
     * @return
     * @throws JCodecException
     */
    public ByteBuffer encode(Object msg) throws JCodecException;
}
