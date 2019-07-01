package jie.com.funnel;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.List;

import jie.com.funnellib.CustomLabelCallback;
import jie.com.funnellib.FunnelView;
import jie.com.funnellib.Util;

/**
 * Created by hj on 2019/2/25.
 * 说明：此Activity演示自定义描述文字
 */
public class CustomLabelActivity extends BaseFunnelActivity {

    @Override
    void initEvent(FunnelView funnelView) {
        final List<FunnelData> data = FunnelData.getFourCountData();

        funnelView.addCustomLabelCallback(new CustomLabelCallback() {
            @Override
            public void drawText(Canvas canvas, Paint mPaintLabel, float labelX, float labelY, int index) {
                FunnelData funnelData = data.get(index);
                //先画前面的文字
                mPaintLabel.setColor(funnelData.color);
                mPaintLabel.setFakeBoldText(false);
                canvas.drawText(funnelData.getLabel()+":", labelX, labelY, mPaintLabel);
                //计算前面文字的长度
                float labelWidth = Util.getTextWidth(mPaintLabel,funnelData.getLabel()+":");
                mPaintLabel.setColor(Color.parseColor("#333333"));
                mPaintLabel.setFakeBoldText(true);
                //画后面的文字
                canvas.drawText(funnelData.num+"个", labelX + labelWidth, labelY, mPaintLabel);
            }
        });
        funnelView.setHasLabel(false);
        funnelView.setChartData(data);

    }
}
