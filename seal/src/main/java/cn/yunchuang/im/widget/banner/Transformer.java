package cn.yunchuang.im.widget.banner;

import android.support.v4.view.ViewPager.PageTransformer;

import cn.yunchuang.im.widget.banner.transformer.AccordionTransformer;
import cn.yunchuang.im.widget.banner.transformer.BackgroundToForegroundTransformer;
import cn.yunchuang.im.widget.banner.transformer.CubeInTransformer;
import cn.yunchuang.im.widget.banner.transformer.CubeOutTransformer;
import cn.yunchuang.im.widget.banner.transformer.DefaultTransformer;
import cn.yunchuang.im.widget.banner.transformer.DepthPageTransformer;
import cn.yunchuang.im.widget.banner.transformer.FlipHorizontalTransformer;
import cn.yunchuang.im.widget.banner.transformer.FlipVerticalTransformer;
import cn.yunchuang.im.widget.banner.transformer.ForegroundToBackgroundTransformer;
import cn.yunchuang.im.widget.banner.transformer.RotateDownTransformer;
import cn.yunchuang.im.widget.banner.transformer.RotateUpTransformer;
import cn.yunchuang.im.widget.banner.transformer.ScaleInOutTransformer;
import cn.yunchuang.im.widget.banner.transformer.StackTransformer;
import cn.yunchuang.im.widget.banner.transformer.TabletTransformer;
import cn.yunchuang.im.widget.banner.transformer.ZoomInTransformer;
import cn.yunchuang.im.widget.banner.transformer.ZoomOutSlideTransformer;
import cn.yunchuang.im.widget.banner.transformer.ZoomOutTranformer;

public class Transformer {
    public static Class<? extends PageTransformer> Default = DefaultTransformer.class;
    public static Class<? extends PageTransformer> Accordion = AccordionTransformer.class;
    public static Class<? extends PageTransformer> BackgroundToForeground = BackgroundToForegroundTransformer.class;
    public static Class<? extends PageTransformer> ForegroundToBackground = ForegroundToBackgroundTransformer.class;
    public static Class<? extends PageTransformer> CubeIn = CubeInTransformer.class;
    public static Class<? extends PageTransformer> CubeOut = CubeOutTransformer.class;
    public static Class<? extends PageTransformer> DepthPage = DepthPageTransformer.class;
    public static Class<? extends PageTransformer> FlipHorizontal = FlipHorizontalTransformer.class;
    public static Class<? extends PageTransformer> FlipVertical = FlipVerticalTransformer.class;
    public static Class<? extends PageTransformer> RotateDown = RotateDownTransformer.class;
    public static Class<? extends PageTransformer> RotateUp = RotateUpTransformer.class;
    public static Class<? extends PageTransformer> ScaleInOut = ScaleInOutTransformer.class;
    public static Class<? extends PageTransformer> Stack = StackTransformer.class;
    public static Class<? extends PageTransformer> Tablet = TabletTransformer.class;
    public static Class<? extends PageTransformer> ZoomIn = ZoomInTransformer.class;
    public static Class<? extends PageTransformer> ZoomOut = ZoomOutTranformer.class;
    public static Class<? extends PageTransformer> ZoomOutSlide = ZoomOutSlideTransformer.class;
}
