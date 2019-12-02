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
 * 说明：漏斗View,可自定义宽度，颜色，高度，描述文字，线宽，线颜色等...
 * 详细操作请阅读README.md
 * github: https://github.com/Jay-huangjie/FunnelView
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
    private int mLineColor;
    private float mLineTextSpace;  //字与线的间距
    private float mLastLineOffset; //最底部从中心点向两边的偏移量
    private float mTotalHeight; //单个梯形的目标高度
    private int count; //漏斗的个数
    private float mPlotBottom; //底部坐标
    private boolean EXACTLY; //是否是精确高度
    private boolean hasLabel; //是否需要描述文字
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

    //最长的线的宽度的一半
    private float mTopMaxLineHalf;

    /*
     * 自定义绘制描述文字接口
     * */
    private CustomLabelCallback mCustomLabelCallback;

    /*
     * 自定义漏斗宽度变化策略
     * */
    private HalfWidthCallback mHalfWidthCallback;

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

    /*
     * 宽度策略数据容器
     * */
    private float[] halfArrays;

    /**
     * 描述文字辅助类
     */
    private LabelHelper mLabelHelper;

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
        mLabelHelper = new LabelHelper();
        if (attributeSet != null) {
            TypedArray ta = context.obtainStyledAttributes(attributeSet, R.styleable.FunnelView);
            mLineWidth = ta.getDimension(R.styleable.FunnelView_lineWidth, dip2px(context, 12));
            mLineColor = ta.getColor(R.styleable.FunnelView_lineColor, -1);
            mLineTextSpace = ta.getDimension(R.styleable.FunnelView_lineTextSpace, dip2px(context, 7));
            mLastLineOffset = ta.getDimension(R.styleable.FunnelView_lastLineOffset, dip2px(context, 20));
            mTotalHeight = ta.getDimension(R.styleable.FunnelView_totalHeight, dip2px(context, 30));
            mFunnelLineStoke = ta.getDimension(R.styleable.FunnelView_funnelLineStoke, 5);
            mFunnelLineColor = ta.getColor(R.styleable.FunnelView_funnelLineColor, Color.WHITE);
            mLineStoke = ta.getDimension(R.styleable.FunnelView_lineStoke, 3f);
            mLabelColor = ta.getColor(R.styleable.FunnelView_labelColor, Color.BLACK);
            mLabelSize = ta.getDimension(R.styleable.FunnelView_labelSize, sp2px(mContext, 12));
            hasLabel = ta.getBoolean(R.styleable.FunnelView_hasLabel,true);
            ta.recycle();
        }
        disableHardwareAccelerated(this);
        chartRender();
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
            calcPlotRange();
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
        float funnelHeight = EXACTLY ? mPlotHeight / count : mTotalHeight;
        float cx = mCenterX;
        renderPlotDesc(canvas, cx, funnelHeight);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec),
                measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int measureSpec) {
//        int result = 100;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

//        if (specMode == MeasureSpec.EXACTLY) { //fill_parent
//            result = specSize;
//        }
//        } else if (specMode == MeasureSpec.AT_MOST) { //wrap_content 暂时不处理,因为不能获取到具体需要的宽度
//            result = Math.min(result, specSize);
//        }
        return specSize;
    }

    private int measureHeight(int measureSpec) {
        int result = (int) (mTotalHeight * count + mFunnelLineStoke * (count - 1));
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) { //fill_parent
            result = specSize;
            EXACTLY = true;
        }
        return result;
    }


    /**
     * 计算一些关键数据
     */
    private void calcPlotRange() {
        mPlotBottom = mBottom - getPaddingBottom();
        float mPlotTop = getPaddingTop();
        float mPlotLeft = getPaddingLeft();
        float mPlotRight = mRight - getPaddingRight();
        mPlotWidth = Math.abs(mPlotRight - mPlotLeft);
        mPlotHeight = Math.abs(mPlotBottom - mPlotTop);
        mCenterX = (int) mTopMaxLineHalf + mPlotLeft;
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
        float lineY;
        Path path = new Path();
        mLabelHelper.setDrawBasicParameters(canvas,mPaintLabel);
        for (int i = 0; i < count; i++) {
            IFunnelData d = mDataSet.get(i);
            path.reset();
            if (i == 0) { //画底部的线，从左下角开始绘制
                path.moveTo(cx - mLastLineOffset, mPlotBottom);
                //底部线默认长度为80
                path.lineTo(cx + mLastLineOffset, mPlotBottom);
            } else {
                path.moveTo(pStart.x, pStart.y);
                path.lineTo(pStop.x, pStop.y);
            }
            //根据数量来调整倾斜角度,如果需要实现别的倾斜效果只需实现HalfWidthCallback接口
            if (mHalfWidthCallback == null) {
                halfWidth += getDefaultHalfWidthOffset();
            } else {
                halfWidth = halfArrays[i];
            }
            bottomY = sub(mPlotBottom, i * funnelHeight);
            lineY = bottomY - funnelHeight / 2;

            pStart.x = cx - mLastLineOffset - halfWidth;
            pStart.y = bottomY - funnelHeight;
            pStop.x = cx + mLastLineOffset + halfWidth;
            pStop.y = bottomY - funnelHeight;

            float lineX = pStop.x + mLineWidth;
            if (hasLabel) {
                //画线
                mPaintLabelLine.setColor(mLineColor == -1 ? d.getColor() : mLineColor);
                canvas.drawLine(cx, lineY, lineX, lineY, mPaintLabelLine);
            }
            path.lineTo(pStop.x, pStop.y); //画梯形右边的线
            path.lineTo(pStart.x, pStart.y); //画梯形左边的线
            mPaint.setColor(d.getColor());
            path.close();
            canvas.drawPath(path, mPaint);
            if (i != count - 1) { //绘制中间的线
                canvas.drawLine(pStart.x, pStart.y, pStop.x, pStop.y, mPaintFunnelLine);
            }

            if (hasLabel) {
                //绘制描述文字
                float labelX = lineX + mLineTextSpace;
                float labelY = lineY + getPaintFontHeight(mPaintLabel) / 3;
                if (mCustomLabelCallback == null) {
                    canvas.drawText(d.getLabel(), labelX, labelY, mPaintLabel);
                } else {
                    mLabelHelper.updateXY(labelX,labelY);
                    mCustomLabelCallback.drawText(mLabelHelper, i);
                }
            }
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
    public void addCustomLabelCallback(CustomLabelCallback iCustomLabelCallback) {
        this.mCustomLabelCallback = iCustomLabelCallback;
    }

    /**
     * 设置数据源
     *
     * @param chartData 数据源
     */
    public <T extends IFunnelData> void setChartData(@NonNull List<T> chartData) {
        setChartData(chartData, null);
    }

    public void setHasLabel(boolean hasLabel){
        this.hasLabel = hasLabel;
    }


    /**
     * 设置数据源及宽度策略
     *
     * @param chartData 数据源
     * @param callback  宽度策略回调
     */
    public <T extends IFunnelData> void setChartData(@NonNull List<T> chartData, HalfWidthCallback callback) {
        this.mDataSet = (List<IFunnelData>) chartData;
        this.mHalfWidthCallback = callback;
        count = mDataSet.size();
        if (callback == null) {
            mTopMaxLineHalf = mLastLineOffset + getDefaultHalfWidthOffset() * count;
        } else {
            halfArrays = new float[count];
            float max = 0;
            float halfWidth = 0;
            //将所有的宽度数据组装进数组中
            for (int i = 0; i < count; i++) {
                halfWidth = callback.getHalfStrategy(halfWidth, count, i);
                halfArrays[i] = halfWidth;
                if (max < halfWidth) {
                    max = halfWidth;  //找出其中的最大值,此值也就是漏斗的最大宽度(不包括线和描述文字)
                }
            }
            mTopMaxLineHalf = max + mLastLineOffset;
        }
        invalidate();
    }

    /*
     * 默认漏斗宽度变化策略
     * */
    private float getDefaultHalfWidthOffset() {
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

}
