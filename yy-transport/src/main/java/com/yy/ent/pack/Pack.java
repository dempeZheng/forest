package com.yy.ent.pack;

import java.io.UnsupportedEncodingException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/26
 * Time: 18:16
 * To change this template use File | Settings | File Templates.
 */
public class Pack {
    private ByteBuffer buffer;

    private int m_maxCapacity = 2 * 1024 * 1024;

    public Pack() {
        buffer = ByteBuffer.allocate(512);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
    }

    public Pack(int size) {
        buffer = ByteBuffer.allocate(size);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
    }

    protected Object attachment;

    public void setAttachment(Object obj) {
        this.attachment = obj;
    }

    public Object getAttachment() {
        return this.attachment;
    }

    public int size() {
        return buffer.position();
    }

    public void clear() {
        buffer.clear();
    }

    public void mark() {
        buffer.mark();
    }

    public void reset() {
        buffer.reset();
    }

    /**
     * Get buffer of Pack with flip()
     */
    public ByteBuffer getBuffer() {
        ByteBuffer dup = buffer.duplicate();
        dup.flip();
        return dup;
    }

    public ByteBuffer getOriBuffer() {
        return buffer;
    }

    public Pack putFetch(byte[] bytes) {
        ensureCapacity(bytes.length);
        buffer.put(bytes);
        return this;
    }

    public Pack putByte(byte bt) {
        ensureCapacity(1);
        buffer.put(bt);
        return this;

    }


    public Pack putInt(int val) {
        ensureCapacity(4);
        buffer.putInt(val);
        return this;
    }


    public Pack putBoolean(boolean val) {
        ensureCapacity(1);
        buffer.put((byte) (val ? 1 : 0));
        return this;

    }

    public Pack putLong(long val) {
        ensureCapacity(8);
        buffer.putLong(val);
        return this;

    }

    public Pack putShort(short val) {
        ensureCapacity(2);
        buffer.putShort(val);
        return this;

    }

    // 16位的
    public Pack putVarbin(byte[] bytes) {
        ensureCapacity(2 + bytes.length);
        buffer.putShort((short) bytes.length);
        buffer.put(bytes);
        return this;

    }

    // 32位的
    public Pack putVarbin32(byte[] bytes) {
        ensureCapacity(4 + bytes.length);
        buffer.putInt(bytes.length);
        buffer.put(bytes);
        return this;

    }

    public Pack putVarstr(String str) throws UnsupportedEncodingException {
        if (str == null)
            str = "";
        return putVarbin(str.getBytes("utf-8"));

    }

    /**
     * 打包String
     *
     * @param str
     * @return
     */
    public Pack putVarstr32(String str) throws UnsupportedEncodingException {
        if (str == null) {
            str = "";
        }
        return putVarbin32(str.getBytes("utf-8"));

    }

    public Pack putBuffer(ByteBuffer buf) {
        ensureCapacity(buf.remaining());
        buffer.put(buf);
        return this;

    }

    public void replaceShort(int off, short val) {
        int pos = buffer.position();
        buffer.position(off);
        buffer.putShort(val).position(pos);

    }

    public void replaceInt(int off, int val) {
        int pos = buffer.position();
        buffer.position(off);
        buffer.putInt(val).position(pos);

    }

    /**
     * Ensures that a the buffer can hold up the new increment
     *
     * @throws Exception
     */
    public void ensureCapacity(int increament)
            throws BufferOverflowException {
        if (buffer.remaining() >= increament) {
            return;
        }

        int requiredCapacity = buffer.capacity() + increament
                - buffer.remaining();

        if (requiredCapacity > m_maxCapacity) {
            throw new BufferOverflowException();
        }

        int tmp = Math.max(requiredCapacity, buffer.capacity() * 2);
        int newCapacity = Math.min(tmp, m_maxCapacity);

        ByteBuffer newBuffer = ByteBuffer.allocate(newCapacity);
        newBuffer.order(buffer.order());
        buffer.flip();
        newBuffer.put(buffer);
        buffer = newBuffer;
    }


}
