package com.yy.ent.codec;

import com.yy.ent.protocol.JettyResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/26
 * Time: 20:08
 * To change this template use File | Settings | File Templates.
 */
public class JettyRespDecoder extends ByteToMessageDecoder {

    private final static Logger LOGGER = LoggerFactory.getLogger(JettyRespDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int length = byteBuf.readableBytes();
        if (length < 2) {
            return;
        }
        byteBuf.markReaderIndex();
        short size = byteBuf.readShort();
        if (length - 2 < size) {
            byteBuf.resetReaderIndex();
            LOGGER.warn("");
            return;
        }
        JettyResponse resp = JettyResponse.decode(byteBuf, size);
        LOGGER.debug("resp:{}", resp.toString());
        list.add(resp);

    }


}
