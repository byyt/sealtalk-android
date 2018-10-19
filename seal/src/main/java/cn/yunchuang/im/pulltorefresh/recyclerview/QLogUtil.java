package cn.yunchuang.im.pulltorefresh.recyclerview;


import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;

/**
 * 日志工具类
 *
 * @author mulinrui
 */
public class QLogUtil {

    /**
     * Android's max limit for a log entry is ~4076 bytes,
     * so 4000 bytes is used as chunk size since default charset
     * is UTF-8
     */
    private static final int CHUNK_SIZE = 2000;

    private static String sTag = "hudongzuoye";
    private static boolean sDebuggable = false;

    public static void setTag(String tag) {
        sTag = tag;
    }

    public static void i(String msg) {
        log(Log.INFO, msg);
    }

    public static void v(String msg) {
        log(Log.VERBOSE, msg);
    }

    public static void d(String msg) {
        log(Log.DEBUG, msg);
    }

    public static void w(String msg) {
        log(Log.WARN, msg);
    }

    public static void w(Throwable tr) {
        log(Log.WARN, getStackTraceString(tr));
    }

    public static void w(String msg, Throwable tr) {
        log(Log.WARN, msg + '\n' + getStackTraceString(tr));
    }

    public static void e(String msg) {
        log(Log.ERROR, msg);
    }

    public static void e(Throwable tr) {
        getStackTraceString(tr);
        log(Log.ERROR, getStackTraceString(tr));
    }

    public static void e(String msg, Throwable tr) {
        getStackTraceString(tr);
        log(Log.ERROR, msg + '\n' + getStackTraceString(tr));
    }


    /**
     * Handy function to get a loggable stack trace from a Throwable
     *
     * @param tr An exception to log
     */
    public static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }

        // This is to reduce the amount of log spew that apps do in the non-error
        // condition of the network being unavailable.
        Throwable t = tr;
        while (t != null) {
            if (t instanceof UnknownHostException) {
                return "";
            }
            t = t.getCause();
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

    public static boolean isDebugable() {
        return sDebuggable;
    }

    public static void setDebugable(boolean debugable) {
        sDebuggable = debugable;
    }


    private static void log(int priority, String msg) {
        if (sDebuggable) {
            if (null != msg && msg.length() > 0) {
                //get bytes of message with system's default charset (which is UTF-8 for Android)
                byte[] bytes = msg.getBytes();
                int length = bytes.length;
                if (length <= CHUNK_SIZE) {
                    Log.println(priority, sTag, msg);
                    return;
                }

                for (int i = 0; i < length; i += CHUNK_SIZE) {
                    int count = Math.min(length - i, CHUNK_SIZE);
                    //create a new String with system's default charset (which is UTF-8 for Android)
                    Log.println(priority, sTag, new String(bytes, i, count));
                }
            }
        }
    }


    //==========================================================================
    // Inner/Nested Classes
    //==========================================================================


    public static final String NATIVE_TAG = "Native页面";
    public static final String REQUEST_TAG = "接口请求";
    public static final String H5_TAG = "H5页面";
    public static final String GAME_TAG = "游戏页面";

    private static final String ALL_TAG = "timeLog:";

    /**
     * 类名
     */
    static String className;
    /**
     * 方法名
     */
    static String methodName;
    /**
     * 行数
     */
    static int lineNumber;


    public static void endTime(String tag, String msg) {
        getMethodNames(new Throwable().getStackTrace());
        QLogUtil.i(ALL_TAG + tag + "  className:" + className + "  methodName:" + methodName + "  lineNumber:" + lineNumber + "  msg:" + msg);
    }

    private static void getMethodNames(StackTraceElement[] sElements) {
        className = sElements[1].getFileName();
        methodName = sElements[1].getMethodName();
        lineNumber = sElements[1].getLineNumber();
    }


    static long startAllTime;
    static long endAllTime;

    static long startTime;
    static long endTime;

    public static void setStartTime() {
        startAllTime = System.currentTimeMillis();
        endTime = startAllTime;
    }

    public static void setEndTime() {
        endAllTime = System.currentTimeMillis();
        QLogUtil.e(ALL_TAG + NATIVE_TAG + "  总耗时时间:" + (endAllTime - startAllTime));
    }

    public static void printTime(String msg) {
        startTime = endTime;
        endTime = System.currentTimeMillis();
        getMethodNames(new Throwable().getStackTrace());
        QLogUtil.i(ALL_TAG + NATIVE_TAG + "  className:" + className + "  methodName:" + methodName + "  lineNumber:" + lineNumber + "  msg:" + "  " + msg + ":" + (endTime - startTime));
    }

    public static void printTimeLog(String msg) {
        startTime = endTime;
        endTime = System.currentTimeMillis();
        getMethodNames(new Throwable().getStackTrace());
        QLogUtil.i(ALL_TAG + NATIVE_TAG + "  和上一次打印时间差：" + (endTime - startTime) + "--->msg:" + "  " + msg);
    }

}
