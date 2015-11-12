package com.dempe.ketty.protocol;

import com.alibaba.fastjson.JSONObject;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessagePacker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: zhengdaxia
 * Date: 15/10/17
 * Time: 上午10:25
 * To change this template use File | Settings | File Templates.
 */
public class KettyHeader {


    private String uri;

    // 消息id，用于异步消息返回的标识
    private Integer msgId;

    private JSONObject param;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public JSONObject getParam() {
        return param;
    }

    public void setParam(JSONObject param) {
        this.param = param;
    }


    public Integer getMsgId() {
        return msgId;
    }

    public void setMsgId(Integer msgId) {
        this.msgId = msgId;
    }


    public byte[] encode() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        MessagePacker packer = MessagePack.newDefaultPacker(out);
        packer.packInt(1).packString(uri)
                .packInt(msgId)
                .packString(param.toJSONString());
        packer.close();
        return out.toByteArray();


    }
}
