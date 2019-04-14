package cn.yunchuang.im.server.utils;

import java.util.ArrayList;

public class StaticDataUtils {

    //进入程序后是否已经定位成功，并上传位置成功过，
    //如果成功过，进入首页直接取sp里的值，查看附近的人时，可以直接向服务器发起请求
    //如果未成功过，必须再进行一次定位，定位成功并且上传位置成功后才能，向服务器发起附近的人或者计算距离的请求，否则服务器保留的是旧位置信息
    //计算距离也会计算出错误信息
    public static boolean hasLocated = false;

    //进入详情页，浏览图片时，临时的模糊照片的url存在这个变量里，进入浏览模式时取出来
    //静态变量，不要直接赋值，容易内存泄漏
    public static ArrayList<String> mBlurImgUrlList = new ArrayList<>();

    public static boolean isInBlurImgUrlList(String url) {
        if (mBlurImgUrlList.indexOf(url) >= 0) {
            return true;
        }
        return false;
    }

    //调用此函数对mBlurImgUrlList赋值，依次对其中的String复制一遍，防止内存泄漏
    public static void setBlurImgUrlList(ArrayList<String> blurImgUrlList) {
        mBlurImgUrlList.clear();
        for (String str : blurImgUrlList) {
            mBlurImgUrlList.add(str);
        }
    }

}
