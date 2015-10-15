package com.yy.ent.srv.codec;

import com.yy.ent.commons.protopack.base.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.EncoderException;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/15
 * Time: 17:22
 * To change this template use File | Settings | File Templates.
 */
public class YYEncoder extends MessageToByteEncoder<Packet> {
    private static final Logger log = LoggerFactory.getLogger(YYEncoder.class);
    private ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;

    public void setByteOrder(ByteOrder byteOrder) {
        this.byteOrder = byteOrder;
    }

    public ByteOrder getByteOrder() {
        return this.byteOrder;
    }

    protected void encode(ChannelHandlerContext ctx, Packet pk, ByteBuf outBuf)
            throws Exception {
        try {
            pk.marshal();
            ByteBuffer data = pk.getPack().getBuffer();
            outBuf = outBuf.order(byteOrder);// 字节序转成YY协议的低端字节
            outBuf.writeBytes(getOutBytes(data));
        } catch (Throwable e) {
            log.error("throwable: " + e.getMessage(), e);
            throw new EncoderException(e);
        }

    }

    protected byte[] getOutBytes(ByteBuffer data) {
        ByteBuffer out = ByteBuffer
                .allocate(data.limit() - data.position() + 4);
        out.order(getByteOrder());
        int len = data.limit() - data.position() + 4;
        // 长度包含包长度int 4个字节
        out.putInt(len);
        out.put(data);
        out.flip();
        return out.array();
    }

}
