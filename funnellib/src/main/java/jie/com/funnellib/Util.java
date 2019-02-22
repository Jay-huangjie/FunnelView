package jie.com.funnellib;

import android.content.Context;
import android.graphics.Paint;
import android.util.TypedValue;
import android.view.View;

import java.math.BigDecimal;

/**
 * Created by hj on 2019/2/22.
 * 说明：
 */
class Util {
    /**
     * convert sp to its equivalent px
     * <p>
     * 将sp转换为px
     */
    static float sp2px(Context context, float spValue) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue,context.getResources().getDisplayMetrics());
    }

    /** dip转换px */
    static float dip2px(Context context,int dip) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip,context.getResources().getDisplayMetrics());
    }


    /*
     * 禁止硬件加速
     * */
    static void disableHardwareAccelerated(View view) {
        if (view == null) {
            return;
        }
        //是否开启了硬件加速,如开启将其禁掉
        if (!view.isHardwareAccelerated()) {
            view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }


    /**
     * 得到单个字的高度
     * @param paint 画笔
     * @return 高度
     */
    static float getPaintFontHeight(Paint paint)
    {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (float) Math.ceil(fm.descent - fm.ascent);
    }

    /**
     * 减法运算
     *
     * @param v1 参数1
     * @param v2 参数2
     * @return 运算结果
     */
    static float sub(float v1, float v2) {
        BigDecimal bgNum1 = new BigDecimal(Float.toString(v1));
        BigDecimal bgNum2 = new BigDecimal(Float.toString(v2));
        return bgNum1.subtract(bgNum2).floatValue();
    }

}
