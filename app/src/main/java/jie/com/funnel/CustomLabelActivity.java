package jie.com.funnel;

import android.graphics.Color;

import java.util.List;

import jie.com.funnellib.CustomLabelCallback;
import jie.com.funnellib.FunnelView;
import jie.com.funnellib.LabelHelper;

/**
 * Created by hj on 2019/2/25.
 * 说明：此Activity演示自定义描述文字
 */
public class CustomLabelActivity extends BaseFunnelActivity {

    @Override
    void initEvent(FunnelView funnelView) {
        final List<FunnelData> data = FunnelData.getFourCountData();
        //第一种方式，获取画笔，画布，实现逻辑自由，缺点是需要熟悉自定义View的绘制Api
//        funnelView.addCustomLabelCallback(new CustomLabelCallback() {
//            @Override
//            public void drawText(LabelHelper mHelper, int index) {
//                FunnelData mData = data.get(index);
//                //获取控件
//                Canvas canvas = mHelper.getCanvas();
//                float mLabelX = mHelper.getLabelX();
//                float mLabelY = mHelper.getLabelY();
//                Paint mPaintLabel = mHelper.getPaint();
//                mPaintLabel.setColor(mData.color);
//                mPaintLabel.setFakeBoldText(false);
//                //先画前面的文字
//                canvas.drawText(mData.getLabel() + ":", mLabelX, mLabelY, mPaintLabel);
//                //计算前面文字的长度
//                float labelWidth = Util.getTextWidth(mPaintLabel, mData.getLabel() + ":");
//                mPaintLabel.setColor(Color.parseColor("#333333"));
//                mPaintLabel.setFakeBoldText(true);
//                //画后面的文字
//                canvas.drawText(mData.num + "个", mLabelX + labelWidth, mLabelY, mPaintLabel);
//            }
//        });

        //第二种方式，使用封装的Api,能覆盖大部分场景，但缺少定制化的自由
        funnelView.addCustomLabelCallback(new CustomLabelCallback() {
            @Override
            public void drawText(LabelHelper mHelper, int index) {
                FunnelData mData = data.get(index);
                mHelper.build(
                        mHelper.getBuild()
                        .setFirstHalfText(mData.label+":")
                        .setFirstHalfTextStyle(mData.color)
                        .setCenterHalfText(mData.num)
                        .setCenterHalfTextStyle(Color.parseColor("#333333"),true)
                        .setFooterHalfText("个")
                        .setFooterTextStyle(Color.parseColor("#333333"))
                );
            }
        });
        funnelView.setChartData(data);
    }
}
