package jie.com.funnellib;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.text.TextUtils;

/**
 * Created by huangjie on 2019/12/2.
 * 说明：描述文字辅助类
 */
public final class LabelHelper {
    private float mLabelX;
    private float mLabelY;
    private Canvas mCanvas;
    private Paint mPaint;
    private Builder mBuilder;

    protected void setDrawBasicParameters(Canvas mCanvas, Paint mPaint) {
        this.mCanvas = mCanvas;
        this.mPaint = mPaint;
    }

    protected void updateXY(float mLabelX, float mLabelY) {
        this.mLabelX = mLabelX;
        this.mLabelY = mLabelY;
    }

    public float getLabelX() {
        return mLabelX;
    }

    public float getLabelY() {
        return mLabelY;
    }

    public Canvas getCanvas() {
        return mCanvas;
    }

    public Paint getPaint() {
        return mPaint;
    }

    public void build(@NonNull Builder builder) {
        String mFirstHalfText = builder.mFirstHalfText;
        float mFirstHalfX = 0;
        if (!TextUtils.isEmpty(mFirstHalfText)) {
            mPaint.setColor(builder.mFirstHalfTextColor);
            mPaint.setFakeBoldText(builder.mFirstHalfFakeBoldText);
            mCanvas.drawText(mFirstHalfText, mLabelX, mLabelY, mPaint);
            mFirstHalfX = Util.getTextWidth(mPaint, mFirstHalfText);
        }
        String mCenterHalfText = builder.mCenterHalfText;
        float mCenterHalfX = 0;
        if (!TextUtils.isEmpty(mCenterHalfText)) {
            mPaint.setColor(builder.mCenterHalfTextColor);
            mPaint.setFakeBoldText(builder.mCenterHalfFakeBoldText);
            mCanvas.drawText(mCenterHalfText, mLabelX + mFirstHalfX, mLabelY, mPaint);
            mCenterHalfX = Util.getTextWidth(mPaint, mCenterHalfText);
        }
        String mFooterHalfText = builder.mFooterHalfText;
        if (!TextUtils.isEmpty(mFooterHalfText)) {
            mPaint.setColor(builder.mFooterTextColor);
            mPaint.setFakeBoldText(builder.mFooterHalfFakeBoldText);
            mCanvas.drawText(mFooterHalfText, mLabelX + mCenterHalfX + mFirstHalfX, mLabelY, mPaint);
        }
    }

    public Builder getBuild() {
        if (mBuilder == null) {
            mBuilder = new Builder();
        }
        return mBuilder;
    }

    public final static class Builder {
        //内容
        private String mFirstHalfText;
        private String mCenterHalfText;
        private String mFooterHalfText;
        //字体颜色
        private int mFirstHalfTextColor;
        private int mCenterHalfTextColor;
        private int mFooterTextColor;
        //是否加粗
        private boolean mFirstHalfFakeBoldText;
        private boolean mCenterHalfFakeBoldText;
        private boolean mFooterHalfFakeBoldText;

        public Builder setFirstHalfText(String mFirstHalfText) {
            this.mFirstHalfText = mFirstHalfText;
            return this;
        }

        public Builder setCenterHalfText(String mCenterHalfText) {
            this.mCenterHalfText = mCenterHalfText;
            return this;
        }

        public Builder setFooterHalfText(String mFooterHalfText) {
            this.mFooterHalfText = mFooterHalfText;
            return this;
        }

        public Builder setFirstHalfTextStyle(int mFirstHalfTextColor) {
            return setFirstHalfTextStyle(mFirstHalfTextColor,false);
        }

        public Builder setFirstHalfTextStyle(int mFirstHalfTextColor,boolean mFirstHalfFakeBoldText) {
            this.mFirstHalfTextColor = mFirstHalfTextColor;
            this.mFirstHalfFakeBoldText = mFirstHalfFakeBoldText;
            return this;
        }

        public Builder setCenterHalfTextStyle(int mCenterHalfTextColor) {
            return setCenterHalfTextStyle(mCenterHalfTextColor,false);
        }

        public Builder setCenterHalfTextStyle(int mCenterHalfTextColor,boolean mCenterHalfFakeBoldText) {
            this.mCenterHalfTextColor = mCenterHalfTextColor;
            this.mCenterHalfFakeBoldText = mCenterHalfFakeBoldText;
            return this;
        }

        public Builder setFooterTextStyle(int mFooterTextColor) {
            return setFooterTextStyle(mFooterTextColor,false);
        }

        public Builder setFooterTextStyle(int mFooterTextColor,boolean mFooterHalfFakeBoldText) {
            this.mFooterTextColor = mFooterTextColor;
            this.mFooterHalfFakeBoldText = mFooterHalfFakeBoldText;
            return this;
        }

    }

}
