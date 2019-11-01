package com.example.timer02;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //创建计时成员变量
    private Timer mTimer;
    private TimerTask mTask;
    private  TextView mTvTime;
    //创建线程运行对象
    private MyRunnable mRunnable;
    //创建线程处理对象,传入当前活动
    private  final MyHandler mHandler=new MyHandler(this);


    //创建线程构造方法，将线程运行对象加到消息队列中
    private class MyRunnable implements Runnable{
        @Override
        public void run() {
            //线程运行时，为计时区赋予当前时间作为文字
            mTvTime.setText(getTime());
            //延后1秒将线程运行对象this加到消息队列中,此处作用是每隔一秒就调用线程处理对象更改计时区文字
            mHandler.postDelayed(this,1000);
        }
    }
    //创建线程处理方法，构造消息队列
    private static class MyHandler extends Handler{
        //使主活动的实例作为弱引用对象
        private final WeakReference<MainActivity> mActivity;
        //初始化弱引用对象
        private MyHandler (MainActivity activity){
            mActivity=new WeakReference<>(activity);
        }
    }

    //获取当前时间方法
    private  static String getTime(){
        //格式化当前时间
        SimpleDateFormat format=new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
        //返回格式化的时间
        return format.format(new Date());
    }

    //自定义UI以及按钮监听器
    private void initUI() {
        //计时区文字
        mTvTime=(TextView)findViewById(R.id.tv_time);
        //开始按钮
        Button buStart=(Button)findViewById(R.id.bu_start);
        //停止按钮
        Button buStop=(Button)findViewById(R.id.bu_stop);

        //为按钮设置监听器
        buStart.setOnClickListener(this);
        buStop.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //启动UI（同时在该方法中启动监听器）
        initUI();
    }

    //实现监听——按钮按动
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击开始按钮
            case R.id.bu_start:
                //若线程未运行……
                if(mRunnable==null) {
                    //……为其赋予新的运行对象
                    mRunnable=new MyRunnable();
                    //无延时（立即）将运行对象放入消息队列，即调用线程处理对象
                    mHandler.postDelayed(mRunnable,0);
                }
                break;

            //点击停止按钮
            case R.id.bu_stop:
                //将线程运行对象从消息队列移除，即取消计时
                mHandler.removeCallbacks(mRunnable);
                //无效化线程运行对象
                mRunnable=null;
                break;
        }
    }
}
