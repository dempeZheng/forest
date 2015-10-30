package com.yy.ent.protocol;


/**
 * Created with IntelliJ IDEA.
 * User: zhengdaxia
 * Date: 15/10/17
 * Time: 上午10:24
 * To change this template use File | Settings | File Templates.
 */
public class YYRequest {

    private Header header;

    private Body body;

    public YYRequest(Header header, Body body) {

        this.header = header;
        this.body = body;
    }


    public String uri() {
        return header.getUri();
    }


}
