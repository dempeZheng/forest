package com.yy.ent.protocol;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yy.ent.pack.Pack;
import com.yy.ent.pack.Unpack;
import io.netty.buffer.ByteBuf;

import java.io.UnsupportedEncodingException;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/26
 * Time: 18:31
 * To change this template use File | Settings | File Templates.
 */
public class KettyRequest implements Request {


    private KettyHeader header;

    private JSONObject body;

    public JSONObject getBody() {
        return body;
    }

    public void setBody(JSONObject body) {
        this.body = body;
    }

    public void setMsgId(Integer msgId) {
        header.setMsgId(msgId);
    }

    public Integer getMsgId() {
        return header.getMsgId();
    }

    public String getUri() {
        return header.getUri();
    }

    public KettyRequest() {
        header = new KettyHeader();
        body = new JSONObject();
    }


    public void setUri(String uri) {
        header.setUri(uri);
    }

    public void setParameter(JSONObject param) {
        header.setParam(param);
    }


    public KettyHeader getHeader() {
        return header;
    }

    public void setHeader(KettyHeader header) {
        this.header = header;
    }

    public JSONObject getParameter() {
        return header.getParam();
    }


    public void encoder(ByteBuf byteBuf) throws UnsupportedEncodingException {
        Pack pack = new Pack();
        pack.putVarstr(getUri());
        pack.putInt(getMsgId());
        JSONObject params = getParameter();
        String str = "";
        if (params != null) {
            str = params.toJSONString();
        }
        pack.putVarstr(str);
        short headSize = (short) pack.size();
        pack.putVarstr(getBody().toJSONString());
        int size = pack.size();
        byte[] bytes = new byte[size];
        pack.getBuffer().get(bytes);

        // 消息长度
        byteBuf.writeShort(size + 2);

        byteBuf.writeShort(headSize);

        byteBuf.writeBytes(bytes);
    }

    public static KettyRequest decoder(ByteBuf byteBuf, int size) throws UnsupportedEncodingException {
        short headerSize = byteBuf.readShort();
        byte[] bytes = new byte[headerSize];
        byteBuf.readBytes(bytes);
        Unpack unpack = new Unpack(bytes);
        String uri = unpack.popVarstr();
        Integer msgId = unpack.popInt();
        String params = unpack.popVarstr();

        int bodySize = size - headerSize - 2;
        byte bodyBytes[] = new byte[bodySize];
        byteBuf.readBytes(bodyBytes);

        Unpack bodyPack = new Unpack(bodyBytes);
        String jsonBody = bodyPack.popVarstr();
        KettyRequest req = new KettyRequest();
        KettyHeader header = new KettyHeader();
        header.setUri(uri);
        header.setMsgId(msgId);
        header.setParam(JSON.parseObject(params));
        req.setBody(JSON.parseObject(jsonBody));
        req.setHeader(header);
        return req;
    }

}
