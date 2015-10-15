package com.yy.ent.srv.codec;

import com.yy.ent.commons.protopack.base.Unpack;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderException;
import io.netty.util.internal.RecyclableArrayList;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteOrder;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/15
 * Time: 17:40
 * To change this template use File | Settings | File Templates.
 */
public class YYDecoder extends ChannelHandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(YYDecoder.class);

    ByteBuf cumulation;
    private boolean singleDecode;
    private boolean decodeWasNull;
    private boolean first;
    private ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;


    public void setByteOrder(ByteOrder byteOrder) {
        this.byteOrder = byteOrder;
    }

    public ByteOrder getByteOrder() {
        return this.byteOrder;
    }

    /**
     * If set then only one message is decoded on each
     * {@link #channelRead(ChannelHandlerContext, Object)} call. This may be
     * useful if you need to do some protocol upgrade and want to make sure
     * nothing is mixed up.
     * <p/>
     * Default is {@code false} as this has performance impacts.
     */
    public void setSingleDecode(boolean singleDecode) {
        this.singleDecode = singleDecode;
    }

    /**
     * If {@code true} then only one message is decoded on each
     * {@link #channelRead(ChannelHandlerContext, Object)} call.
     * <p/>
     * Default is {@code false} as this has performance impacts.
     */
    public boolean isSingleDecode() {
        return singleDecode;
    }

    /**
     * Returns the actual number of readable bytes in the internal cumulative
     * buffer of this decoder. You usually do not need to rely on this value to
     * write a decoder. Use it only when you must use it at your own risk. This
     * method is a shortcut to {@link #internalBuffer()
     * internalBuffer().readableBytes()}.
     */
    protected int actualReadableBytes() {
        return internalBuffer().readableBytes();
    }

    /**
     * Returns the internal cumulative buffer of this decoder. You usually do
     * not need to access the internal buffer directly to write a decoder. Use
     * it only when you must use it at your own risk.
     */
    protected ByteBuf internalBuffer() {
        if (cumulation != null) {
            return cumulation;
        } else {
            return Unpooled.EMPTY_BUFFER;
        }
    }

    @Override
    public final void handlerRemoved(ChannelHandlerContext ctx)
            throws Exception {
        ByteBuf buf = internalBuffer();
        int readable = buf.readableBytes();
        if (buf.isReadable()) {
            ByteBuf bytes = buf.readBytes(readable);
            buf.release();
            ctx.fireChannelRead(bytes);
        }
        cumulation = null;
        ctx.fireChannelReadComplete();
        handlerRemoved0(ctx);
    }

    /**
     * Gets called after the {@link io.netty.handler.codec.ByteToMessageDecoder} was removed from the
     * actual context and it doesn't handle events anymore.
     */
    protected void handlerRemoved0(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        if (msg instanceof ByteBuf) {
            RecyclableArrayList out = RecyclableArrayList.newInstance();
            try {
                ByteBuf data = ((ByteBuf) msg).order(byteOrder);
                first = cumulation == null;
                if (first) {
                    cumulation = data;
                } else {
                    if (cumulation.writerIndex() > cumulation.maxCapacity()
                            - data.readableBytes()) {
                        expandCumulation(ctx, data.readableBytes());
                    }
                    cumulation.writeBytes(data);
                    data.release();
                }
                callDecode(ctx, cumulation, out);
            } catch (DecoderException e) {
                throw e;
            } catch (Throwable t) {
                throw new DecoderException(t);
            } finally {
                if (cumulation != null && !cumulation.isReadable()) {
                    cumulation.release();
                    cumulation = null;
                }
                int size = out.size();
                decodeWasNull = size == 0;

                for (int i = 0; i < size; i++) {
                    ctx.fireChannelRead(out.get(i));
                }
                out.recycle();
            }
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    private void expandCumulation(ChannelHandlerContext ctx, int readable) {
        ByteBuf oldCumulation = cumulation;
        cumulation = ctx.alloc().buffer(
                oldCumulation.readableBytes() + readable).order(byteOrder);
        cumulation.writeBytes(oldCumulation);
        oldCumulation.release();
    }

    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        if (cumulation != null && !first) {
            // discard some bytes if possible to make more room in the
            // buffer
            cumulation.discardSomeReadBytes();
        }
        if (decodeWasNull) {
            decodeWasNull = false;
            if (!ctx.channel().config().isAutoRead()) {
                ctx.read();
            }
        }
        ctx.fireChannelReadComplete();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.error("decoder channelInactive!ctx:" + ctx.name());
        RecyclableArrayList out = RecyclableArrayList.newInstance();
        try {
            if (cumulation != null) {
                callDecode(ctx, cumulation, out);
                decodeLast(ctx, cumulation, out);
            } else {
                decodeLast(ctx, Unpooled.EMPTY_BUFFER, out);
            }
        } catch (DecoderException e) {
            throw e;
        } catch (Exception e) {
            throw new DecoderException(e);
        } finally {
            if (cumulation != null) {
                cumulation.release();
                cumulation = null;
            }
            int size = out.size();
            for (int i = 0; i < size; i++) {
                ctx.fireChannelRead(out.get(i));
            }
            ctx.fireChannelInactive();
            out.recycle();
        }
    }

    /**
     * Called once data should be decoded from the given {@link ByteBuf}. This
     * method will call {@link #decode(ChannelHandlerContext, ByteBuf, java.util.List)} as
     * long as decoding should take place.
     *
     * @param ctx the {@link ChannelHandlerContext} which this
     *            {@link io.netty.handler.codec.ByteToMessageDecoder} belongs to
     * @param in  the {@link ByteBuf} from which to read data
     * @param out the {@link java.util.List} to which decoded messages should be added
     */
    protected void callDecode(ChannelHandlerContext ctx, ByteBuf in,
                              List<Object> out) {
        try {
            while (in.isReadable()) {
                int outSize = out.size();
                int oldInputLength = in.readableBytes();
                decode(ctx, in, out);

                // Check if this handler was removed before continuing the loop.
                // If it was removed, it is not safe to continue to operate on
                // the buffer.
                //
                // See https://github.com/netty/netty/issues/1664
                if (ctx.isRemoved()) {
                    break;
                }

                if (outSize == out.size()) {
                    if (oldInputLength == in.readableBytes()) {
                        break;
                    } else {
                        continue;
                    }
                }

                if (oldInputLength == in.readableBytes()) {
                    throw new DecoderException(
                            StringUtil.simpleClassName(getClass())
                                    + ".decode() did not read anything but decoded a message.");
                }

                if (isSingleDecode()) {
                    break;
                }
            }
        } catch (DecoderException e) {
            throw e;
        } catch (Throwable cause) {
            throw new DecoderException(cause);
        }
    }

    /**
     * Decode the from one {@link ByteBuf} to an other. This method will be
     * called till either the input {@link ByteBuf} has nothing to read anymore,
     * till nothing was read from the input {@link ByteBuf} or till this method
     * returns {@code null}.
     *
     * @param ctx the {@link ChannelHandlerContext} which this
     *            {@link io.netty.handler.codec.ByteToMessageDecoder} belongs to
     * @param in  the {@link ByteBuf} from which to read data
     * @param out the {@link List} to which decoded messages should be added
     * @throws Exception is thrown if an error accour
     */
    protected void decode(ChannelHandlerContext ctx, ByteBuf in,
                          List<Object> out) throws Exception {
        Unpack decoded = decode(in);
        if (decoded != null) {
            out.add(decoded);
        }
    }

    /**
     * Is called one last time when the {@link ChannelHandlerContext} goes
     * in-active. Which means the
     * {@link #channelInactive(ChannelHandlerContext)} was triggered.
     * <p/>
     * By default this will just call
     * {@link #decode(ChannelHandlerContext, ByteBuf, List)} but sub-classes may
     * override this for some special cleanup operation.
     */
    protected void decodeLast(ChannelHandlerContext ctx, ByteBuf in,
                              List<Object> out) throws Exception {
        decode(ctx, in, out);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        log.error("decoder exceptionCaught!", cause);
    }


    /**
     * YYP解码
     *
     * @param buf
     * @return
     */
    protected Unpack decode(ByteBuf buf) {
        int length = buf.readableBytes();
        if (length < 4) {
            log.warn("length not enough, need 4");
            return null;
        }
        buf.markReaderIndex();
        int firstIntValue = buf.readInt();
        int[] protoValue = ProtocolValue.parse(firstIntValue);
        int curPacketSize = protoValue[1];
        int dataSize = curPacketSize - 4;
        length = buf.readableBytes();
        if (length < dataSize) {
            log.warn("packet size not enough, need " + curPacketSize
                    + " now:" + length);
            buf.resetReaderIndex();
            return null;
        }
        byte[] bytes = new byte[dataSize];
        buf.readBytes(bytes, 0, bytes.length);

        Unpack unpack = new Unpack(bytes);

        return unpack;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("channelActive");
        super.channelActive(ctx);
    }
}
