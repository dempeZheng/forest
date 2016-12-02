package com.dempe.forest.codec;

/**
 * 0-15	16-23	24-31	32-95	96-127
 * magic	version	extend flag	request id	body content length
 * 魔数	协议版本	24-28	29-30	31
 * 消息id
 * <p>
 * body包长
 * 保留	event( 可支持4种event，
 * 如normal, exception等)
 * <p>
 * User: Dempe
 * Date: 2016/11/25
 * Time: 16:23
 * To change this template use File | Settings | File Templates.
 */
public class Header implements Cloneable {

    private short magic;// 魔数
    private byte version; // 协议版本
    /**
     * 0 不压缩
     * 1 使用Snappy 1.0.5
     * 2 使用gzip
     */
    /**
     * 扩展字段[0-2=>序列化方式，3-4=>压缩方式，0 不压缩,1 gzip，2使用Snappy 1.0.5
     * 5-6=>(event( 可支持4种event， 如normal, exception等)),7=>]
     */
    private byte extend;
    private Long messageID;// 消息id
    private String uri;// 协议路由uri
    private Integer size;// 消息payload长度

    private transient long timeOut;

    public Header() {
    }
    public Header(short magic, byte version, byte extend, String uri, long timeOut) {
        this.magic = magic;
        this.version = version;
        this.extend = extend;
        this.uri = uri;
        this.timeOut = timeOut;
    }

    public Header(short magic, byte version, byte extend, Long messageID, String uri, Integer size) {
        this.magic = magic;
        this.version = version;
        this.extend = extend;
        this.messageID = messageID;
        this.uri = uri;
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

    public void setVersion(Byte version) {
        this.version = version;
    }

    public Byte getExtend() {
        return extend;
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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setExtend(byte extend) {
        this.extend = extend;
    }


    public long getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(long timeOut) {
        this.timeOut = timeOut;
    }

    public void setVersion(byte version) {
        this.version = version;
    }

    @Override
    public Header clone() throws CloneNotSupportedException {
        return (Header) super.clone();
    }
}

