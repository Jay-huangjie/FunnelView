package jie.com.funnellib;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by hj on 2019/2/22.
 * 说明：开放自定义描述绘制画笔
 */
public interface CustomLabelCallback {
    /**
     * 循环绘制线后面的文字
     * @param canvas 画布
     * @param mPaintLabel 画笔
     * @param labelX 文字开始X坐标
     * @param labelY 文字开始Y坐标
     * @param index 绘制下标
     * 注意：绘制顺序是从下往上绘制!!!
     */
    void drawText(Canvas canvas, Paint mPaintLabel,float labelX, float labelY,int index);
}
