package com.dempe.ketty.common.utils;

import java.io.*;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2014/12/25
 * Time: 15:42
 * To change this template use File | Settings | File Templates.
 */
public class MD5 {

    private static final char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static Appendable append(Appendable a, short in) {
        return append(a, in, 4);
    }

    public static Appendable append(Appendable a, short in, int length) {
        return append(a, in, length);
    }

    public static Appendable append(Appendable a, int in) {
        return append(a, in, 8);
    }

    public static Appendable append(Appendable a, int in, int length) {
        return append(a, in, length);
    }


    public static String hash(byte[] bytes) {
        byte[] encode = digest(bytes);
        return encode(encode);
    }

    public static String hash(String message) {
        return hash(message.getBytes());
    }

    public static String hash(String message, Charset charset) {
        return hash(message.getBytes(charset));
    }

    public static String hash(File file) throws IOException {
        byte[] encode = digest(file);
        return encode(encode);
    }

    public static byte[] digest(byte[] data) {
        return getMessageDigest().digest(data);
    }

    public static byte[] digest(File file) throws IOException {
        InputStream fis = null;
        MessageDigest digest = getMessageDigest();
        try {
            fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int numRead = 0;
            while ((numRead = fis.read(buffer)) > 0) {
                digest.update(buffer, 0, numRead);
            }
            return digest.digest();
        } catch (IOException e) {
            throw e;
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }

    static MessageDigest getMessageDigest() {
        try {
            return MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    public static String encode(byte[] data) {
        char[] charArray = encodeAsChars(data);
        return new String(charArray);
    }

    public static String encode(byte[] data, int index, int len) {
        char[] charArray = encodeAsChars(data, index, len);
        return new String(charArray);
    }

    public static char[] encodeAsChars(byte[] data) {
        int l = data.length;

        char[] out = new char[l << 1];

        int i = 0;
        for (int j = 0; i < l; i++) {
            out[(j++)] = DIGITS[((0xF0 & data[i]) >>> 4)];
            out[(j++)] = DIGITS[(0xF & data[i])];
        }

        return out;
    }

    public static char[] encodeAsChars(byte[] data, int index, int len) {
        if (data.length < (index + len)) {
            len = data.length - index;
        }

        char[] out = new char[len << 1];

        int i = index;
        for (int j = 0; i < index + len; i++) {
            out[(j++)] = DIGITS[((0xF0 & data[i]) >>> 4)];
            out[(j++)] = DIGITS[(0xF & data[i])];
        }

        return out;
    }

    public static byte[] decode(String hexString) {
        return decode(hexString.toCharArray());
    }

    public static byte[] decode(char[] hexChars) {
        int len = hexChars.length;

        if ((len & 0x1) != 0) {
            throw new RuntimeException("Odd number of characters.");
        }

        byte[] out = new byte[len >> 1];

        int i = 0;
        for (int j = 0; j < len; i++) {
            int f = toDigit(hexChars[j], j) << 4;
            j++;
            f |= toDigit(hexChars[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }

        return out;
    }

    protected static int toDigit(char ch, int index) {
        int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new RuntimeException("Illegal hexadecimal charcter " + ch + " at index " + index);
        }
        return digit;
    }

    public static String getMD5Str(String str) throws NoSuchAlgorithmException,
            UnsupportedEncodingException {
        MessageDigest messageDigest = null;

        messageDigest = MessageDigest.getInstance("MD5");

        messageDigest.reset();

        messageDigest.update(str.getBytes("UTF-8"));

        byte[] byteArray = messageDigest.digest();

        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(
                        Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }

        return md5StrBuff.toString();
    }


}