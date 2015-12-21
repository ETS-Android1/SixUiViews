package cn.six.open.others.colormask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;

/**
 * Created by songzhw on 2015/12/21
 */
public class ColorMaskUtil {
    private static int iconSize = 64;
    private static int baseColor = Color.BLACK;
    private static int errorColor = Color.parseColor("#e7492E");
    private static int primaryColor = baseColor;

    public static Bitmap[] generateIconBitmaps(Context ctx, int origin) {
        if (origin == -1) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(ctx.getResources(), origin, options);
        int size = Math.max(options.outWidth, options.outHeight);
        options.inSampleSize = size > iconSize ? size / iconSize : 1;
        options.inJustDecodeBounds = false;
        return generateIconBitmaps(BitmapFactory.decodeResource(ctx.getResources(), origin, options));
    }

    public static Bitmap[] generateIconBitmaps(Bitmap origin) {
        if (origin == null) {
            return null;
        }
        Bitmap[] iconBitmaps = new Bitmap[4];
        origin = scaleIcon(origin);
        iconBitmaps[0] = origin.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(iconBitmaps[0]);
        canvas.drawColor(baseColor & 0x00ffffff | (ColorsUtil.isLight(baseColor) ? 0xff000000 : 0x8a000000), PorterDuff.Mode.SRC_IN);
        iconBitmaps[1] = origin.copy(Bitmap.Config.ARGB_8888, true);
        canvas = new Canvas(iconBitmaps[1]);
        canvas.drawColor(primaryColor, PorterDuff.Mode.SRC_IN);
        iconBitmaps[2] = origin.copy(Bitmap.Config.ARGB_8888, true);
        canvas = new Canvas(iconBitmaps[2]);
        canvas.drawColor(baseColor & 0x00ffffff | (ColorsUtil.isLight(baseColor) ? 0x4c000000 : 0x42000000), PorterDuff.Mode.SRC_IN);
        iconBitmaps[3] = origin.copy(Bitmap.Config.ARGB_8888, true);
        canvas = new Canvas(iconBitmaps[3]);
        canvas.drawColor(errorColor, PorterDuff.Mode.SRC_IN);
        return iconBitmaps;
    }

    private static Bitmap scaleIcon(Bitmap origin) {
        int width = origin.getWidth();
        int height = origin.getHeight();
        int size = Math.max(width, height);
        if (size == iconSize) {
            return origin;
        } else if (size > iconSize) {
            int scaledWidth;
            int scaledHeight;
            if (width > iconSize) {
                scaledWidth = iconSize;
                scaledHeight = (int) (iconSize * ((float) height / width));
            } else {
                scaledHeight = iconSize;
                scaledWidth = (int) (iconSize * ((float) width / height));
            }
            return Bitmap.createScaledBitmap(origin, scaledWidth, scaledHeight, false);
        } else {
            return origin;
        }
    }

}