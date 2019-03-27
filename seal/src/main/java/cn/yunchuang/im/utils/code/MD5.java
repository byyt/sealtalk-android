package cn.yunchuang.im.utils.code;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

import cn.yunchuang.im.zmico.utils.Utils;


/**
 * MD5 加密�?2010-11-25
 *
 * @author zhiyong.jing
 */
public class MD5 {

    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final char[] HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static final ThreadLocal<MessageDigest> digest = new ThreadLocal<MessageDigest>() {
        @Override
        public MessageDigest get() {
            MessageDigest messageDigest = null;
            try {
                messageDigest = MessageDigest.getInstance("MD5");
            } catch (Throwable e) {
//                Ln.e(e);
            }
            return messageDigest;
        }
    };

    public static String getMD5(String instr) {
        String s = null;
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest md = MessageDigest
                    .getInstance("MD5");
            md.update(instr.getBytes());
            byte tmp[] = md.digest();
            char str[] = new char[16 * 2];
            int k = 0;
            for (int i = 0; i < 16; i++) {

                byte byte0 = tmp[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            s = new String(str);

        } catch (Exception e) {
//            Ln.e(e);
        }
        return s;
    }

    private static String toHexString(byte[] b) {
        int length = b == null ? 0 : b.length;
        StringBuilder sb = new StringBuilder(length * 2);
        for (int i = 0; i < length; i++) {
            sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    public static String md5sumFile(File file) {
        InputStream fis = null;
        String md5result = null;
        try {
            byte[] buffer = new byte[1024];
            int numRead;
            fis = new FileInputStream(file);
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            while ((numRead = fis.read(buffer)) > 0) {
                md5.update(buffer, 0, numRead);
            }
            md5result = toHexString(md5.digest());
        } catch (Exception e) {
//            Ln.e(e);
        } finally {
            if (!Utils.isNull(fis)) {
                try {
                    fis.close();
                } catch (IOException e) {
//                    Ln.e(e);
                }
            }
        }
        return md5result;
    }

    /**
     * 计算文件的MD5
     *
     * @param fileName 文件的绝对路径
     * @return
     * @throws IOException
     */
    public static String md5(String fileName) {
        File f = new File(fileName);
        return md5(f);
    }

    /**
     * 计算文件的MD5，重载方法
     *
     * @param file 文件对象
     * @return
     * @throws IOException
     */
    public static String md5(File file) {
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            FileChannel ch = in.getChannel();
            MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest messageDigest = digest.get();
            messageDigest.update(byteBuffer);
            return byte2hex(messageDigest.digest());
        } catch (Throwable e) {
//            Ln.e(e);
            return null;
        } finally {
            try {
                if (in != null) in.close();
            } catch (IOException e) {
//                Ln.e(e);
            }
        }
    }

    /**
     * 计算文件的MD5，重载方法
     *
     * @param bytes 文件对象
     * @return
     * @throws IOException
     */
    public static String md5(byte[] bytes) {
        if (Utils.isEmptyByte(bytes)) return null;

        MessageDigest messageDigest = digest.get();
        if (Utils.isNull(messageDigest)) return null;

        ByteBuffer bb = ByteBuffer.wrap(bytes);
        messageDigest.update(bb);
        return byte2hex(messageDigest.digest());
    }

    private static String byte2hex(byte bytes[]) {
        return byte2hex(bytes, 0, bytes.length);
    }

    private static String byte2hex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = HEX[(bt & 0xf0) >> 4];
        char c1 = HEX[bt & 0xf];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }
}
