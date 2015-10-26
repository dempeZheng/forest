package com.yy.ent.pack;

import com.yy.ent.commons.protopack.base.Marshallable;
import com.yy.ent.commons.protopack.exception.UnpackException;

import java.io.UnsupportedEncodingException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/26
 * Time: 18:17
 * To change this template use File | Settings | File Templates.
 */
public class Unpack {

    protected ByteBuffer buffer;


    public int size() {
        return buffer.limit() - buffer.position();
    }

    public Unpack(byte[] bytes, int offset, int length) {
        buffer = ByteBuffer.wrap(bytes, offset, length);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
    }

    public Unpack(byte[] bytes) {
        this(bytes, 0, bytes.length);
    }

    /**
     * buf [ position : limit] -> Unpack
     */
    public Unpack(ByteBuffer buf) {
        this(buf.array(), buf.position(), buf.limit() - buf.position());
    }

    public void setBuffer(ByteBuffer buf) {
        buffer = buf;
    }

    public void reset(byte[] bytes) {
        buffer.clear();
        buffer.put(bytes);
        buffer.flip();
    }

    /**
     * 该函数没有buffer，只是便于Class.newInstance进行统一初始化, 只有popObject能继续使用之
     */
    public Unpack() {
        buffer = null;
    }

    public ByteBuffer getBuffer() {
        return buffer.duplicate();
    }

    public ByteBuffer getOriBuffer() {
        return buffer;
    }

    public byte[] popFetch(int sz) {
        try {
            byte[] fetch = new byte[sz];
            buffer.get(fetch);
            return fetch;
        } catch (BufferUnderflowException bEx) {
            throw new UnpackException(bEx);
        }
    }

    public Object popObject(Object obj)
            throws Exception {
        if (obj instanceof Marshallable) {
            //return popMarshallable((Marshallable) obj);
            return null;
        } else if (obj instanceof String) {
            return popVarstr();
        } else if (obj instanceof Unpack) {
            ((Unpack) obj).buffer = ByteBuffer.allocate(buffer
                    .remaining());
            ((Unpack) obj).buffer.order(ByteOrder.LITTLE_ENDIAN);
            ((Unpack) obj).buffer.put(buffer);
            ((Unpack) obj).buffer.rewind();
            return obj;
        } else {
            throw new UnpackException("unknow object type");
        }
    }

    public Byte popByte() {
        try {
            return buffer.get();
        } catch (BufferUnderflowException bEx) {
            throw new UnpackException(bEx);
        }
    }

    // 16位的大小
    public byte[] popVarbin() {
        return popFetch(buffer.getShort());
    }

    // 32位的大小
    public byte[] popVarbin32() {
        return popFetch(buffer.getInt());
    }

    public String popVarbin(String encode) {
        try {
            byte[] bytes = popVarbin();
            return new String(bytes, encode);
        } catch (UnsupportedEncodingException codeEx) {
            throw new UnpackException("unsupported encoding", codeEx);
        }
    }

    public String popVarbin32(String encode) {
        try {
            byte[] bytes = popVarbin32();
            return new String(bytes, encode);
        } catch (UnsupportedEncodingException codeEx) {
            throw new UnpackException(codeEx);
        }
    }

    public String popVarstr() {
        return popVarbin("utf-8");
    }

    public String popVarstr32() {
        return popVarbin32("utf-8");
    }

    public String popVarstr(String encode) {
        return popVarbin(encode);
    }

    public Integer popInt() {
        try {
            return buffer.getInt();
        } catch (BufferUnderflowException bEx) {
            throw new UnpackException(bEx);
        }
    }

    public Long popLong() {
        try {
            return buffer.getLong();
        } catch (BufferUnderflowException bEx) {
            throw new UnpackException(bEx);
        }
    }

    public Short popShort() {
        try {
            return buffer.getShort();
        } catch (BufferUnderflowException bEx) {
            throw new UnpackException(bEx);
        }
    }


    public Boolean popBoolean() {
        if (popByte() > 0)
            return true;
        else
            return false;
    }

    @Override
    public String toString() {
        return buffer.toString();
    }

}
