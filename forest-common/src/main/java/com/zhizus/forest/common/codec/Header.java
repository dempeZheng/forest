package com.zhizus.forest.common.codec;


import com.zhizus.forest.common.Constants;
import com.zhizus.forest.common.ProtoVersion;
import com.zhizus.forest.common.config.MethodConfig;
import com.zhizus.forest.common.util.ForestUtil;

/**
 * 0-15	16-23	24-31	32-95	96-127
 * magic	version	extend flag	request id	body content length
 * 魔数	协议版本	24-28	29-30	31
 * 消息id
 * <p/>
 * body包长
 * 保留	event( 可支持4种event，
 * 如normal, exception等)
 * <p/>
 * User: Dempe
 * Date: 2016/11/25
 * Time: 16:23
 * To change this template use File | Settings | File Templates.
 */
public class Header {

    private short magic;// 魔数
    private byte version; // 协议版本
    /**
     * 0 不压缩
     * 1 使用Snappy 1.0.5
     * 2 使用gzip
     */
    /**
     * 扩展字段[0-2=>序列化方式，3-4=>压缩方式，0 不压缩,1 GZIP，2使用Snappy 1.0.5
     * 5-6=>(event( 可支持4种event， 如normal,heartbeat, exception等)),7=>0:request,1:response]
     */
    private byte extend;
    private Long messageID;// 消息id
    private Integer size;// 消息payload长度


    public Header() {
    }

    public Header(short magic, byte version) {
        this.magic = magic;
        this.version = version;
    }

    public Header(short magic, byte version, byte extend) {
        this.magic = magic;
        this.version = version;
        this.extend = extend;
    }

    public Header(short magic, byte version, byte extend, Long messageID, Integer size) {
        this.magic = magic;
        this.version = version;
        this.extend = extend;
        this.messageID = messageID;
        this.size = size;
    }

    public short getMagic() {
        return magic;
    }

    public void setMagic(short magic) {
        this.magic = magic;
    }

    public Byte getVersion() {
        return version;
    }

    public void setVersion(byte version) {
        this.version = version;
    }

    public void setVersion(Byte version) {
        this.version = version;
    }

    public Byte getExtend() {
        return extend;
    }

    public void setExtend(byte extend) {
        this.extend = extend;
    }

    public void setExtend(Byte extend) {
        this.extend = extend;
    }

    public Long getMessageID() {
        return messageID;
    }

    public void setMessageID(Long messageID) {
        this.messageID = messageID;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public static class HeaderMaker {
        HeaderMaker() {
        }

        Header header;

        public static HeaderMaker newMaker() {
            HeaderMaker maker = new HeaderMaker();
            maker.header = new Header(Constants.MAGIC, ProtoVersion.VERSION_1.getVersion());
            return maker;
        }

        public Header make() {
            return header;
        }

        public HeaderMaker loadWithMethodConfig(MethodConfig config) {
            header.setExtend(ForestUtil.getExtend(config.getSerializeType(), config.getCompressType()));
            return this;
        }

        public HeaderMaker withMessageId(long messageID) {
            header.setMessageID(messageID);
            return this;
        }
    }


}

