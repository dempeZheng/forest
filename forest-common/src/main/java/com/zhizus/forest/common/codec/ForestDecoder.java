package com.zhizus.forest.common.codec;

import com.zhizus.forest.common.CompressType;
import com.zhizus.forest.common.Constants;
import com.zhizus.forest.common.MessageType;
import com.zhizus.forest.common.SerializeType;
import com.zhizus.forest.common.codec.compress.Compress;
import com.zhizus.forest.common.codec.serialize.Serialization;
import com.zhizus.forest.common.exception.ForestFrameworkException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Created by Dempe on 2016/12/7.
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
        int size = byteBuf.readInt();
        if (byteBuf.readableBytes() < size) {
            byteBuf.resetReaderIndex();
            return;
        }
        // TODO 限制最大包长
        byte[] payload = new byte[size];
        byteBuf.readBytes(payload);

        Serialization serialization = SerializeType.getSerializationByExtend(extend);
        Compress compress = CompressType.getCompressTypeByValueByExtend(extend);
        Object req =  serialization.deserialize(compress.unCompress(payload), MessageType.getMessageTypeByExtend(extend));
        Header header = new Header(magic, version, extend, messageID, size);
        Message message = new Message(header, req);
        list.add(message);
    }
}
