package cn.yunchuang.im.server.utils;

import java.util.ArrayList;

public class StaticDataUtils {

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
