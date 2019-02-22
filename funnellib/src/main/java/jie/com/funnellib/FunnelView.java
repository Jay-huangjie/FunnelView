package jie.com.funnellib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


import java.math.BigDecimal;
import java.util.List;

/**
 * Created by hj on 2018/11/23.
 * 说明：漏斗View
 */
public class FunnelView extends View {
    private static final String TAG = "FunnelChart";
    private List<IFunnelData> mDataSet;
    private Paint mPaint = null;
    private Paint mPaintFunnelLine = null;

    private Paint mPaintLabel = null;
    private Paint mPaintLabelLine = null;
    private float mRight = 0.0f;
    private float mBottom = 0.0f;
    private Context mContext;
    private float mLineWidth; //线的长度
    private float mLineTextSpace;  //字与线的间距
    private float mLastLineOffset;

    private ICustomText mCustomText;

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
            mLineWidth = ta.getDimension(R.styleable.FunnelView_lineWidth, Util.dip2px(context, 12));
            mLineTextSpace = ta.getDimension(R.styleable.FunnelView_lineTextSpace, Util.dip2px(context, 7));
            mLastLineOffset = ta.getDimension(R.styleable.FunnelView_lastLineOffset, Util.dip2px(context, 20));
            ta.recycle();
        }
        Util.disableHardwareAccelerated(this);
        chartRender();
    }


    public void setChartData(List<IFunnelData> chartData) {
        this.mDataSet = chartData;
        invalidate();
    }

    public void setLeftPadding(float leftPadding) {
        setCenterX(leftPadding);
    }

    private void chartRender() {
        mPaintLabel = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintFunnelLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintFunnelLine.setStrokeWidth(5);
        mPaintFunnelLine.setColor(Color.WHITE);
        mPaintLabelLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintLabel.setTextAlign(Paint.Align.LEFT);
        try {
            mPaintLabel.setColor(Color.parseColor("#666666"));
            mPaintLabel.setTextSize(Util.sp2px(mContext, 12));
            mPaintLabelLine.setStrokeWidth(3f);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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

    private float mPlotRight;
    private float mPlotLeft;
    private float mPlotBottom;
    private float mPlotTop;
    private float mCenterX;

    private float mPlotHeight;
    private float mPlotWidth;

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

        mCenterX = Util.dip2px(mContext, 92 + 45);
    }

    public void setCenterX(float centerX) {
        this.mCenterX = centerX;
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
                halfWidth += Util.dip2px(mContext,17);
            } else if (count <= 6) {
                halfWidth += Util.dip2px(mContext,13);
            } else if (count <= 8) {
                halfWidth += Util.dip2px(mContext,10);
            } else if (count <= 10) {
                halfWidth += Util.dip2px(mContext,7);
            } else {
                halfWidth += Util.dip2px(mContext,5);
            }
            bottomY = sub(mPlotBottom, i * funnelHeight);

            labelY = bottomY - funnelHeight / 2;

            pStart.x = cx - mLastLineOffset - halfWidth;
            pStart.y = bottomY - funnelHeight;

            pStop.x = cx + mLastLineOffset + halfWidth;
            pStop.y = bottomY - funnelHeight;
            path.lineTo(pStop.x, pStop.y); //画右边的线
            path.lineTo(pStart.x, pStart.y); //画左边的线
            mPaint.setColor(d.getColor());
            path.close();
            canvas.drawPath(path, mPaint);
            if (i != count - 1) { //绘制中间的线
                canvas.drawLine(pStart.x, pStart.y, pStop.x, pStop.y, mPaintFunnelLine);
            }
            renderLabels(canvas, d, cx, labelY, d.getColor(), (int) (pStop.x - cx));
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
    public void addCustomDrawText(ICustomText iCustomText) {
        this.mCustomText = iCustomText;
    }

    //画线和字
    private void renderLabels(Canvas canvas, IFunnelData data, float cx, float y, int color, int width/*每一个四边形的宽度的一半*/) {
        if (data == null) return;
        mPaintLabelLine.setColor(color);
        float lineX = cx + width + mLineWidth;
        canvas.drawLine(cx, y, lineX, y, mPaintLabelLine);
        float labelX = lineX + mLineTextSpace;
        float labelY = y + (DrawHelper.getInstance().getPaintFontHeight(mPaintLabel) / 3);
        if (mCustomText == null) {
            setLableUi();
            canvas.drawText(data.getLabel(), labelX, labelY, mPaintLabel);
//            float labelWidth = DrawHelper.getInstance().getTextWidth(mPaintLabel, data.getLabel());
//            setNumUi();
//            canvas.drawText(data.getNumUnit(), labelX + labelWidth, labelY, mPaintLabel);
        } else {
            mCustomText.drawText(canvas, labelX, labelY);
        }
    }


    /**
     * 设置客户字体
     */
    private void setLableUi() {
        mPaintLabel.setColor(Color.parseColor("#666666"));
        mPaintLabel.setFakeBoldText(false);
    }

    /**
     * 设置数量字体
     */
    private void setNumUi() {
        mPaintLabel.setColor(Color.parseColor("#333333"));
        mPaintLabel.setFakeBoldText(true);
    }

    private void renderPlot(Canvas canvas) {
        if (null == mDataSet) {
            Log.e(TAG, "数据源为空!");
            return;
        }
        int count = mDataSet.size();
        float funnelHeight = mPlotHeight / count;
        float cx = mCenterX;
        renderPlotDesc(canvas, cx, funnelHeight);
    }

    /**
     * 减法运算
     *
     * @param v1 参数1
     * @param v2 参数2
     * @return 运算结果
     */
    private float sub(float v1, float v2) {
        BigDecimal bgNum1 = new BigDecimal(Float.toString(v1));
        BigDecimal bgNum2 = new BigDecimal(Float.toString(v2));
        return bgNum1.subtract(bgNum2).floatValue();
    }
}
