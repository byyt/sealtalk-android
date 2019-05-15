package cn.yunchuang.im.notify;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.TypedValue;

import cn.yunchuang.im.App;


/**
 * Created by liumingkong on 15/1/4.
 */
public class NotifyImageUtils {

    public static Bitmap createFramedPhoto(Bitmap b) {
        int notifyWidth = App.getAppContext().getResources()
                .getDimensionPixelSize(android.R.dimen.notification_large_icon_width);
        //切正方形
        int bW = b.getWidth();
        int bH = b.getHeight();
        Bitmap b1;
        if (bH != bW) {
            int w = Math.min(bW, bH);
            int x = (bW - w) / 2;
            int y = (bH - w) / 2;
            b1 = Bitmap.createBitmap(b, x, y, w, w);
        } else {
            b1 = b;
        }
        //等比缩放
        Bitmap b2 = Bitmap.createScaledBitmap(b1, notifyWidth, notifyWidth, true);
        //输出
        Bitmap out = Bitmap.createBitmap(notifyWidth, notifyWidth, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(out);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        final int radius = notifyWidth / 2;
        canvas.drawCircle(radius, radius, radius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //绘制图片的时候，给画笔加一个灰色的蒙层
        paint.setColorFilter(getColorFilter());
        canvas.drawBitmap(b2, 0, 0, paint);
        return out;
    }

    private static ColorMatrixColorFilter getColorFilter() {
        int brightness = 5;
        ColorMatrix cMatrix = new ColorMatrix();
        cMatrix.set(new float[]{1, 0, 0, 0, brightness, 0, 1, 0, 0, brightness,// 改变亮度
                0, 0, 1, 0, brightness, 0, 0, 0, 1, 0});
        ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(cMatrix);
        return colorFilter;
    }

    // 按照官方推荐，48*48 md存放
    // 因为官方说明，大图最大的大小是64*64
    public static Bitmap decodeResource(Resources resources, int id) {
        try {
            TypedValue value = new TypedValue();
            resources.openRawResource(id, value);
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inTargetDensity = value.density;
            opts.inScaled = true;
            return BitmapFactory.decodeResource(resources, id, opts);
        } catch (Exception e) {
//            Ln.e(e);
        } catch (OutOfMemoryError error) {
//            Ln.e(error);
        }
        return null;
    }
}
