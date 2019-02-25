package jie.com.funnel;

import android.util.TypedValue;

import jie.com.funnellib.FunnelView;
import jie.com.funnellib.HalfWidthCallback;

/**
 * Created by hj on 2019/2/25.
 * 说明：自定义宽度策略
 */
public class CustomHalfWidthActivity extends BaseFunnelActivity {

    @Override
    void initEvent(FunnelView funnelView) {
        /**
         * 自定义宽度策略，也就是漏斗每一层宽度增加多少，这都是可以自定义的，这样有利于适配的灵活性,也可以自定义出
         * 很多的效果出来
         * 注意事项：绘制是从下往上绘制的，halfWidth返回的当前的下面那个漏斗的宽度，需要注意一下
         *
         */
        funnelView.setChartData(FunnelData.getTenCountData(), new HalfWidthCallback() {
            @Override
            public float getHalfStrategy(float halfWidth, int count, int i) {
                /**
                 * 这里定义的策略是前4个宽度不变，后面的逐渐增加10dp,所以呈现了一个真正的漏斗形状
                 */
                if (i <= 3) {
                    halfWidth = dp2px(5);
                    return halfWidth;
                } else {
                    halfWidth += dp2px(10);
                    return halfWidth;
                }
            }
        });
    }

    private float dp2px(int dip) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, this.getResources().getDisplayMetrics());
    }
}
