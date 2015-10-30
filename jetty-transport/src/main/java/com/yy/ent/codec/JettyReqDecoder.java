package com.yy.ent.codec;

import com.yy.ent.pack.Unpack;
import com.yy.ent.protocol.JettyReq;
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
public class JettyReqDecoder extends ByteToMessageDecoder {

    private final static Logger LOGGER = LoggerFactory.getLogger(JettyReqDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {

        // TODO decoder
        int length = byteBuf.readableBytes();
        if (length < 2) {
            return;
        }
        byteBuf.markReaderIndex();
        short size = byteBuf.readShort();
        if (length - 2 < size) {
            byteBuf.resetReaderIndex();
            LOGGER.warn("");
        }
        byte[] bytes = new byte[size];
        byteBuf.readBytes(bytes);
        Unpack unpack = new Unpack(bytes);
        JettyReq req = new JettyReq();
        req.setId(unpack.popLong());
        req.setUri(unpack.popVarstr());
        LOGGER.info("req:{}", req.toString());
        list.add(req);

    }


}
