package jie.com.funnellib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.List;

import static jie.com.funnellib.Util.dip2px;
import static jie.com.funnellib.Util.disableHardwareAccelerated;
import static jie.com.funnellib.Util.getPaintFontHeight;
import static jie.com.funnellib.Util.sp2px;
import static jie.com.funnellib.Util.sub;

/**
 * Created by hj on 2018/11/23.
 * 说明：漏斗View
 */
public class FunnelView extends View {
    private static final String TAG = "FunnelChart";
    /*
     * 数据源
     * */
    private List<IFunnelData> mDataSet;
    /*
     * 梯形画笔
     * */
    private Paint mPaint = null;
    /*
     * 间隔线画笔
     * */
    private Paint mPaintFunnelLine = null;
    /*
     * 描述画笔
     * */
    private Paint mPaintLabel = null;
    /*
     * 线 画笔
     * */
    private Paint mPaintLabelLine = null;

    private float mRight = 0.0f;
    private float mBottom = 0.0f;
    private Context mContext;
    private float mLineWidth; //线的长度
    private float mLineTextSpace;  //字与线的间距
    private float mLastLineOffset; //最底部从中心点向两边的偏移量
    private float mTotalHeight; //单个梯形的目标高度
    private int count; //漏斗的个数
    private float mPlotRight;
    private float mPlotLeft;
    private float mPlotBottom;
    private float mPlotTop;

    /*
     * 中心点坐标，绘制是从下往上，这个坐标是最底部那跟线的中心点
     * 最后一根线的长度= mLastLineOffset*2
     * */
    private float mCenterX;

    /*
     * 图表总高度
     * */
    private float mPlotHeight;
    /*
     * 图表总宽度
     * */
    private float mPlotWidth;

    //最顶部的线的宽度
    private float mTopMaxLineWidth;

    /*
     * 自定义绘制描述文字接口
     * */
    private CustomLabel mCustomLabel;

    /*
     * 漏斗之间线的颜色
     * */
    private int mFunnelLineColor;
    /*
     * 漏斗之间线的粗细
     * */
    private float mFunnelLineStoke;

    /*
     * 漏斗与描述之间线的粗细
     * */
    private float mLineStoke;

    /*
     * 描述文字颜色
     * */
    private int mLabelColor;

    /*
     * 描述文字大小
     * */
    private float mLabelSize;

    public FunnelView(Context context) {
        super(context);
        initView(context, null);
    }

    public FunnelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public FunnelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attributeSet) {
        this.mContext = context;
        if (attributeSet != null) {
            TypedArray ta = context.obtainStyledAttributes(attributeSet, R.styleable.FunnelView);
            mLineWidth = ta.getDimension(R.styleable.FunnelView_lineWidth, dip2px(context, 12));
            mLineTextSpace = ta.getDimension(R.styleable.FunnelView_lineTextSpace, dip2px(context, 7));
            mLastLineOffset = ta.getDimension(R.styleable.FunnelView_lastLineOffset, dip2px(context, 20));
            mTotalHeight = ta.getDimension(R.styleable.FunnelView_totalHeight, dip2px(context, 30));
            mFunnelLineStoke = ta.getDimension(R.styleable.FunnelView_funnelLineStoke, 5);
            mFunnelLineColor = ta.getColor(R.styleable.FunnelView_funnelLineColor, Color.WHITE);
            mLineStoke = ta.getDimension(R.styleable.FunnelView_lineStoke, 3f);
            mLabelColor = ta.getColor(R.styleable.FunnelView_labelColor, Color.BLACK);
            mLabelSize = ta.getDimension(R.styleable.FunnelView_labelSize, sp2px(mContext, 12));
            ta.recycle();
        }
        disableHardwareAccelerated(this);
        chartRender();
    }


    public void setChartData(@NonNull List<IFunnelData> chartData) {
        this.mDataSet = chartData;
        count = mDataSet.size();
        mTopMaxLineWidth = mLastLineOffset + getHalfWidthOffset() * count;
        invalidate();
    }

    private void chartRender() {
        mPaintLabel = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintFunnelLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintFunnelLine.setStrokeWidth(mFunnelLineStoke);
        mPaintFunnelLine.setColor(mFunnelLineColor);
        mPaintLabelLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintLabel.setTextAlign(Paint.Align.LEFT);
        mPaintLabel.setColor(mLabelColor);
        mPaintLabel.setTextSize(mLabelSize);
        mPaintLabelLine.setStrokeWidth(mLineStoke);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //图所占范围大小
        mRight = w;
        mBottom = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        try {
            canvas.save();
            //计算主图表区范围
            calcPlotRange();
            //绘制图表
            renderPlot(canvas);
            canvas.restore();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void renderPlot(Canvas canvas) {
        if (null == mDataSet) {
            Log.e(TAG, "FunnelView=>未设置数据源!");
            return;
        }
        float funnelHeight = mPlotHeight / count;
        float cx = mCenterX;
        renderPlotDesc(canvas, cx, funnelHeight);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec),
                measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int measureSpec) {
        int result = 100;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) { //fill_parent
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) { //wrap_content
            result = Math.min(result, specSize);
        }
        return result;
    }

    private int measureHeight(int measureSpec) {
        int result = (int) mTotalHeight * count;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) { //fill_parent
            result = specSize;
        }
        return result;
    }


    /**
     * 计算图的显示范围,依屏幕px值来计算.
     */
    private void calcPlotRange() {
        mPlotBottom = mBottom - getPaddingBottom();
        mPlotTop = getPaddingTop();
        mPlotLeft = getPaddingLeft();
        mPlotRight = mRight - getPaddingRight();
        mPlotWidth = Math.abs(mPlotRight - mPlotLeft);
        mPlotHeight = Math.abs(mPlotBottom - mPlotTop);
        mCenterX = (int) mTopMaxLineWidth + mPlotLeft;
    }

    /**
     * 返回图的数据源
     *
     * @return 数据源
     */
    public List<IFunnelData> getDataSource() {
        return mDataSet;
    }


    /**
     * 绘制梯形
     *
     * @param canvas       画布
     * @param cx           梯形中心坐标
     * @param funnelHeight 梯形的高度
     */
    private void renderPlotDesc(Canvas canvas, float cx, float funnelHeight) {
        int count = mDataSet.size();
        float halfWidth = 0.f; //梯形的半径
        float bottomY;

        PointF pStart = new PointF();
        PointF pStop = new PointF();

        pStart.x = cx - mPlotWidth / 2;
        pStop.x = cx + mPlotWidth / 2;
        pStart.y = pStop.y = mPlotBottom;

        float labelY = 0.f;
        Path path = new Path();
        for (int i = 0; i < count; i++) {
            IFunnelData d = mDataSet.get(i);
            path.reset();
            if (i == 0) { //画底部的线，从左下角开始绘制
                path.moveTo(cx - mLastLineOffset, mPlotBottom);
                //底部线默认长度为100
                path.lineTo(cx + mLastLineOffset, mPlotBottom);
            } else {
                path.moveTo(pStart.x, pStart.y);
                path.lineTo(pStop.x, pStop.y);
            }
            //根据数量来调整倾斜角度,如果需要实现别的倾斜效果只需调整下面的算法
            if (count <= 4) {
                halfWidth += dip2px(mContext, 17);
            } else if (count <= 6) {
                halfWidth += dip2px(mContext, 13);
            } else if (count <= 8) {
                halfWidth += dip2px(mContext, 10);
            } else if (count <= 10) {
                halfWidth += dip2px(mContext, 7);
            } else {
                halfWidth += dip2px(mContext, 5);
            }
            bottomY = sub(mPlotBottom, i * funnelHeight);

            labelY = bottomY - funnelHeight / 2;

            pStart.x = cx - mLastLineOffset - halfWidth;
            pStart.y = bottomY - funnelHeight;

            pStop.x = cx + mLastLineOffset + halfWidth;
            Log.i("HJ", pStop.x + "--cx:" + mLastLineOffset + "--halfWidth:" + halfWidth);
            pStop.y = bottomY - funnelHeight;
            path.lineTo(pStop.x, pStop.y); //画右边的线
            path.lineTo(pStart.x, pStart.y); //画左边的线
            mPaint.setColor(d.getColor());
            path.close();
            canvas.drawPath(path, mPaint);
            if (i != count - 1) { //绘制中间的线
                canvas.drawLine(pStart.x, pStart.y, pStop.x, pStop.y, mPaintFunnelLine);
            }
            renderLabels(canvas, d, cx, labelY, d.getColor(), (int) (pStop.x - cx), i);
        }
    }


    private float getHalfWidthOffset() {
        if (count <= 4) {
            return dip2px(mContext, 17);
        } else if (count <= 6) {
            return dip2px(mContext, 13);
        } else if (count <= 8) {
            return dip2px(mContext, 10);
        } else if (count <= 10) {
            return dip2px(mContext, 7);
        } else {
            return dip2px(mContext, 5);
        }
    }

    /*
     * 设置线的宽度
     * */
    public void setLineWidth(float mLineWidth) {
        this.mLineWidth = mLineWidth;
    }

    /*
     *设置字与线的距离
     * */
    public void setLineTextSpace(float mLineTextSpace) {
        this.mLineTextSpace = mLineTextSpace;
    }

    /*
     * 自定义描述绘制
     * */
    public void addCustomDrawLabel(CustomLabel iCustomLabel) {
        this.mCustomLabel = iCustomLabel;
    }

    //画线和字
    private void renderLabels(Canvas canvas, IFunnelData data, float cx, float y, int color, int halfWidth, int i) {
        if (data == null) return;
        mPaintLabelLine.setColor(color);
        float lineX = cx + halfWidth + mLineWidth;
        canvas.drawLine(cx, y, lineX, y, mPaintLabelLine);
        float labelX = lineX + mLineTextSpace;
        float labelY = y + getPaintFontHeight(mPaintLabel) / 3;
        if (mCustomLabel == null) {
            canvas.drawText(data.getLabel(), labelX, labelY, mPaintLabel);
//            float labelWidth = DrawHelper.getInstance().getTextWidth(mPaintLabel, data.getLabel());
//            setNumUi();
//            canvas.drawText(data.getNumUnit(), labelX + labelWidth, labelY, mPaintLabel);
        } else {
            mCustomLabel.drawText(canvas, mPaintLabel, labelX, labelY, i);
        }
    }
}
