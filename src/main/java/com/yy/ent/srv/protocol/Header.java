/*
 * Copyright (c) 2011 duowan.com. 
 * All Rights Reserved.
 * This program is the confidential and proprietary information of 
 * duowan. ("Confidential Information").  You shall not disclose such
 * Confidential Information and shall use it only in accordance with
 * the terms of the license agreement you entered into with duowan.com.
 */
package com.yy.ent.srv.protocol;

/**
 * @author hongyuan
 */
public class Header implements YYProto {

    public static final int DATA_LENGTH = 4;

    public static final int URI_LENGTH = 4;

    public static final int RES_CODE_LENGTH = 2;

    public static final short DEFAULT_RES_CODE = 200;

    public static final int HEADER_LENGTH = DATA_LENGTH + URI_LENGTH + RES_CODE_LENGTH;

    private long size;

    private long uri;

    private int code;

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getUri() {
        return uri;
    }

    public void setUri(long uri) {
        this.uri = uri;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String toString() {
        return "Header [size=" + size + ", uri=" + uri + ", code=" + code + "]";
    }
}
