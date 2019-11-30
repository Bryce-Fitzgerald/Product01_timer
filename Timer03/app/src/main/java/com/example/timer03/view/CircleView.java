package com.example.timer03.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.timer03.R;


//自定义计时圆圈视图，可参考https://www.cnblogs.com/yishujun/p/5559917.html
public class CircleView extends View {

    // Paint即画笔，在绘图过程中起到了极其重要的作用，画笔主要保存了颜色，样式等绘制信息，指定了如何绘制文本和图形，画笔对象有很多设置方法，大体上可以分为两类，一类与图形绘制相关，一类与文本绘制相关。
    private Paint mPaint;
    private int mCircleColor;//圆环经过的颜色
    private int mBackGroundColor;//圆环没有经过的背景颜色
    private int mPointColor;//那个小点的颜色
    private float mCenterX;//圆环的中心坐标X
    private float mCenterY;//圆环的中心坐标Y
    private float mRadius;//圆环的半径
    private float mCircleWidth;//圆环的环的宽度
    private float mPointRadius;//那个小点的半径

    //RectF类主要用于表示坐标系中的一块矩形区域，并可以对其做一些简单操作。这块矩形区域，需要用左上右下两个坐标点表示（left,top,right,bottom）,你也可以获取一个Rect实例的Width和Height
    //RectF 中的参数是单精度浮点型的
    private RectF mRectF;//花圆弧的四边形
    private float mAngle = 0;//圆弧的角度

    //Rect类主要用于表示坐标系中的一块矩形区域，并可以对其做一些简单操作。这块矩形区域，需要用左上右下两个坐标点表示（left,top,right,bottom）,你也可以获取一个Rect实例的Width和Height
    //Rect 中的参数是整形的
    private Rect mRect = new Rect();


    //以下是自定义View需要构造的三个构造函数
    public CircleView(Context context) {
        this(context, null);
    }
    public CircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCircleView(context, attrs);
    }

    //初始化圆圈视图
    public void initCircleView(Context context, AttributeSet attrs) {
        //创建新的画笔对象
        mPaint = new Paint();
        //设置去锯齿
        mPaint.setAntiAlias(true);

        //初始化所有的属性
        //创建包含各属性的队列
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleView);

        mCenterX = typedArray.getDimension(R.styleable.CircleView_centerX, 0.0f);
        mCenterY = typedArray.getDimension(R.styleable.CircleView_centerY, 0.0f);
        mCircleColor = typedArray.getColor(R.styleable.CircleView_circleColor, Color.RED);
        mBackGroundColor = typedArray.getColor(R.styleable.CircleView_backgroundColor, Color.GREEN);
        mCircleWidth = typedArray.getDimension(R.styleable.CircleView_circleWidth, 0.0f);
        mRadius = typedArray.getDimension(R.styleable.CircleView_radius, 0.0f);
        mPointColor = typedArray.getColor(R.styleable.CircleView_pointColor, Color.BLUE);
        mPointRadius = typedArray.getDimension(R.styleable.CircleView_pointRadius, 30);
        mAngle = typedArray.getFloat(R.styleable.CircleView_angle, 0);

        //创建新的RectF对象
        mRectF = new RectF();
        //设定创建的RectF对象的相关参数
        mRectF.set(mCenterX - mRadius, mCenterY - mRadius, mCenterX + mRadius, mCenterY + mRadius);
    }

    //在画布上进行绘画
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画背景圆环
        drawRing(canvas);
        //画有颜色的弧
        drawArc(canvas);
        //画弧上的小点
        drawPoint(canvas);
    }

    //改变圆弧的角度
    public void changeAngle(float angle) {
        mAngle = angle;
        invalidate();
    }
    // 按照进度来改变圆弧角度，调用changeAngle(angle)
    public void changeProgress(float progress) {
        //将一圈360°分为100格，每格3.6°，且每格要走0.6s，即600ms
        changeAngle(progress * 360.0f / 100.0f );
    }

    //画有颜色的弧
    private void drawArc(Canvas canvas) {
        Paint.Style style = mPaint.getStyle();
        int color = mPaint.getColor();
        float width = mPaint.getStrokeWidth();

        mPaint.setStrokeWidth(mCircleWidth);
        mPaint.setColor(mCircleColor);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(mRectF, 270, mAngle, false, mPaint);

        mPaint.setStyle(style);
        mPaint.setColor(color);
        mPaint.setStrokeWidth(width);
    }
    //画背景圆环
    private void drawRing(Canvas canvas) {
        int color = mPaint.getColor();
        Paint.Style style = mPaint.getStyle();
        float width = mPaint.getStrokeWidth();

        mPaint.setColor(mBackGroundColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mCircleWidth);

        canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);

        mPaint.setColor(color);
        mPaint.setStyle(style);
        mPaint.setStrokeWidth(width);
    }
    //画弧上小点
    private void drawPoint(Canvas canvas) {
        float x = XOfPoint(mAngle + 270, mCenterX, mRadius);
        float y = YOfPoint(mAngle + 270, mCenterY, mRadius);

        Paint.Style style = mPaint.getStyle();
        int color = mPaint.getColor();

        mPaint.setColor(mPointColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(x, y, mPointRadius, mPaint);

        mPaint.setColor(color);
        mPaint.setStyle(style);
    }

    //求弧上点的横坐标
    private float XOfPoint(float angle, float centerx, float r) {
        return centerx + r * (float) Math.cos(angle * Math.PI / 180);
    }
   //求弧上点的纵坐标
    private float YOfPoint(float angle, float centerx, float r) {
        return centerx + r * (float) Math.sin(angle * Math.PI / 180);
    }

}
