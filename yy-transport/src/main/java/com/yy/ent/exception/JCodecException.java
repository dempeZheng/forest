/*
 * Copyright (c) 2011 duowan.com. 
 * All Rights Reserved.
 * This program is the confidential and proprietary information of 
 * duowan. ("Confidential Information").  You shall not disclose such
 * Confidential Information and shall use it only in accordance with
 * the terms of the license agreement you entered into with duowan.com.
 */
package com.yy.ent.exception;

/**
 * @author hefeng
 * @author hongyuan
 */
public class JCodecException extends JServerException {

    private static final long serialVersionUID = -835016280310725427L;

    public JCodecException(Throwable cause) {
        super(cause);
    }

    public JCodecException(String message) {
        super(message);
    }

    public JCodecException(String message, Throwable cause) {
        super(message, cause);
    }
}
