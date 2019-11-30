package com.example.timer03.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

//自定义计时圈内文字视图，可参考https://www.cnblogs.com/yishujun/p/5559917.html
public class TimeView extends View {

    private int mHH = 0;//时
    private int mMM =0;//分
    private int mSS = 0;//秒
    private int mMMSS = 0;//毫秒
    private int mColor = Color.BLUE;
    private int mCenterX = 0;
    private int mCenterY = 0;
    private long mTime;//单位毫秒

    // Paint即画笔，在绘图过程中起到了极其重要的作用，画笔主要保存了颜色，样式等绘制信息，指定了如何绘制文本和图形，画笔对象有很多设置方法，大体上可以分为两类，一类与图形绘制相关，一类与文本绘制相关。
    private Paint mPaint;

    //RectF类主要用于表示坐标系中的一块矩形区域，并可以对其做一些简单操作。这块矩形区域，需要用左上右下两个坐标点表示（left,top,right,bottom）,你也可以获取一个Rect实例的Width和Height
    //RectF 中的参数是单精度浮点型的
    Rect mRect;

    //以下是自定义View需要构造的三个构造函数
    public TimeView(Context context) {
        this(context, null);
    }
    public TimeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public TimeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTimeView(context, attrs);
    }

    //初始化时间文字视图
    public void initTimeView(Context context, AttributeSet attributeSet) {
        //创建画笔对象
        mPaint = new Paint();
        //反锯齿
        mPaint.setAntiAlias(true);
        //创建矩形区域对象
        mRect = new Rect();
        //?
        setTime(0);
    }

    //?
    public void setTime(long time) {
        mTime = time;
        mMM = (int) mTime / 1000 / 60;
        mSS = ((int) mTime / 1000) % 60;
        mMMSS = ((int) mTime % 1000) / 10;
        invalidate();
    }

    //?
    public long getTime() {
        return mTime;
    }

    //在画布上进行绘画
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawMM(canvas);
        drawSS(canvas);
        drawMMSS(canvas);
    }

    private void drawMM(Canvas canvas) {
        String text = String.valueOf(mMM);
        mPaint.setColor(mColor);
        mPaint.setTextSize(getWidth() * 2 / 9);
        mPaint.getTextBounds(text, 0, text.length(), mRect);
        int width = mRect.width();
        int height = mRect.height();
        canvas.drawText(text, getWidth()/2 - width /2 - getWidth() * 2 / 7, getHeight()/2 + height/2, mPaint);
    }
    private void drawSS(Canvas canvas) {
        String text = String.valueOf(mSS);
        if (mSS < 10) {
            text = "0" + text;
        }
        mPaint.setColor(mColor);
        mPaint.setTextSize(getWidth() * 2 /9);
        mPaint.getTextBounds(text, 0, text.length(), mRect);
        int width = mRect.width();
        int height = mRect.height();
        canvas.drawText(text, getWidth()/2 - width /2, getHeight()/2 + height/2, mPaint);
    }
    private void drawMMSS(Canvas canvas) {
        String text = String.valueOf(mMMSS);
        if (mMMSS < 10) {
            text = "0" + text;
        }
        mPaint.setColor(mColor);
        mPaint.setTextSize(getWidth()/9);
        mPaint.getTextBounds(text, 0, text.length(), mRect);
        int width = mRect.width();
        int height = mRect.height();
        canvas.drawText(text, getWidth()/2 + width /2 + getWidth()/9, getHeight()/2 + height, mPaint);
    }


}
