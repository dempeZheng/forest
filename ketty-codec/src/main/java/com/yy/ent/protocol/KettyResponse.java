package com.yy.ent.protocol;

import com.yy.ent.pack.Pack;
import com.yy.ent.pack.Unpack;
import io.netty.buffer.ByteBuf;

import java.io.UnsupportedEncodingException;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/26
 * Time: 19:53
 * To change this template use File | Settings | File Templates.
 */
public class KettyResponse implements Response {

    private Integer msgId;
    private short resCode;
    private String jsonString;

    public KettyResponse(int msgId, short resCode, String jsonString) {
        this.msgId = msgId;
        this.jsonString = jsonString;
        this.resCode = resCode;
    }

    public KettyResponse(int msgId, String jsonString) {
        this.msgId = msgId;
        this.jsonString = jsonString;
    }

    public Integer getMsgId() {
        return msgId;
    }

    public void setMsgId(Integer msgId) {
        this.msgId = msgId;
    }

    public String getJsonString() {
        return jsonString;
    }

    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
    }

    public short getResCode() {
        return resCode;
    }

    public void setResCode(short resCode) {
        this.resCode = resCode;
    }

    public void encode(ByteBuf byteBuf) throws UnsupportedEncodingException {
        Pack pack = new Pack();
        pack.putInt(getMsgId());
        pack.putInt(getResCode());
        int headSize = pack.size();
        pack.putVarstr(getJsonString());
        int size = pack.size();
        byte[] bytes = new byte[size];
        pack.getBuffer().get(bytes);
        byteBuf.writeShort(size + 2);
        byteBuf.writeShort(headSize);
        byteBuf.writeBytes(bytes);

    }

    public static KettyResponse decode(ByteBuf byteBuf, int size) throws UnsupportedEncodingException {
        short headerSize = byteBuf.readShort();
        byte[] headerByte = new byte[headerSize];
        byteBuf.readBytes(headerByte);
        Unpack unpack = new Unpack(headerByte);
        int id = unpack.popInt();
        short resCode = unpack.popShort();

        int bodySize = size - headerSize - 2;
        byte[] bodyByte = new byte[bodySize];
        byteBuf.readBytes(bodyByte);
        Unpack bodyPack = new Unpack(bodyByte);
        String data = bodyPack.popVarstr();
        return new KettyResponse(id, resCode, data);
    }
}
