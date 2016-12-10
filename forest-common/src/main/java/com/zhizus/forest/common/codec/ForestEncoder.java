package com.zhizus.forest.common.codec;

import com.zhizus.forest.common.CompressType;
import com.zhizus.forest.common.SerializeType;
import com.zhizus.forest.common.codec.compress.Compress;
import com.zhizus.forest.common.codec.serialize.Serialization;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by Dempe on 2016/12/7.
 */
public class ForestEncoder extends MessageToByteEncoder<Message> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf byteBuf) throws Exception {
        Header header = message.getHeader();
        byteBuf.writeShort(header.getMagic());
        byteBuf.writeByte(header.getVersion());
        byteBuf.writeByte(header.getExtend());
        byteBuf.writeLong(header.getMessageID());
        Serialization serialization = SerializeType.getSerializationByExtend(header.getExtend());
        Compress compress = CompressType.getCompressTypeByValueByExtend(header.getExtend());
        byte[] payload = compress.compress(serialization.serialize(message.getContent()));
        byteBuf.writeInt(payload.length);
        byteBuf.writeBytes(payload);
    }
}
