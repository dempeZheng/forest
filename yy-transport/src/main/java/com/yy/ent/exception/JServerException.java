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
 * @author hongyuan
 */
public class JServerException extends Exception {

    private static final long serialVersionUID = -9095702616213992961L;

    public JServerException() {
        super();
    }

    public JServerException(String message) {
        super(message);
    }

    public JServerException(Throwable cause) {
        super(cause);
    }

    public JServerException(String message, Throwable cause) {
        super(message, cause);
    }

}
