package jie.com.funnellib;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;

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

}
