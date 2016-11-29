package com.dempe.forest.codec;

import com.dempe.forest.Constants;
import com.dempe.forest.core.exception.ForestFrameworkException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.charset.Charset;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/25
 * Time: 16:34
 * To change this template use File | Settings | File Templates.
 */
public class ForestDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {

        if (byteBuf.readableBytes() < Constants.HEADER_SIZE) {
            return;
        }
        byteBuf.markReaderIndex();

        short magic = byteBuf.readShort();

        if (magic != Constants.MAGIC) {
            byteBuf.resetReaderIndex();
            throw new ForestFrameworkException("ForestDecoder transport header not support, type: " + magic);
        }

        byte version = byteBuf.readByte();
        byte extend = byteBuf.readByte();
        long messageID = byteBuf.readLong();
        short uriLen = byteBuf.readShort();
        byte[] uriArr = new byte[uriLen];
        byteBuf.readBytes(uriArr);
        String uri = new String(uriArr);

        int size = byteBuf.readInt();

        if (byteBuf.readableBytes() < size) {
            byteBuf.resetReaderIndex();
            return;
        }
        // TODO 限制最大包长
        byte[] payload = new byte[size];
        byteBuf.readBytes(payload);

        Header header = new Header();
        header.setMagic(magic);
        header.setVersion(version);
        header.setExtend(extend);
        header.setMessageID(messageID);
        header.setUri(uri);
        header.setSize(size);
        Message message = new Message();
        message.setHeader(header);
        message.setPayload(payload);
        list.add(message);


    }
}
