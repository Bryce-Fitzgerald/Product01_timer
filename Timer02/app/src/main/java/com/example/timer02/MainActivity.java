package com.example.timer02;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //创建计时成员变量
    private  TextView mTvTime;
    //创建线程运行对象
    private MyRunnable mRunnable;
    //创建线程处理对象,传入当前活动
    private  final MyHandler mHandler=new MyHandler(this);
    //创建分钟表示方法
    private int actualDurationFormatMinutes=0;
    //创建时钟表示方法
    private int actualDurationFormatHours=0;
    //创建秒钟表示方法（暂时）
    private int actualDurationFormatSeconds=-1;
    //创建用户持续使用时长（分）
    private int actualDurationMinutes=0;
    //创建用户设定使用时长（分）
    private int settedDurationMinutes=1;
//    //创建表示第一次调用线程标志
//    private  boolean flagRun=false;
    //创建表示线程是否将要被暂停的标志
    private boolean toPause=false;
    //创建表示线程是否已经被暂停的标志
    private boolean isPaused=false;
//    //创建表示实际时长是否已经超过设定时长
//    private boolean actualMoreThanSetted=false;
    //创建表示线程是否将要被唤醒的标志
    private  boolean toContinue=false;
    //用户设定开始时间
    private String actualTime="wtf";




    //创建线程构造方法，将线程运行对象加到消息队列中
    private class MyRunnable implements Runnable{

        //创建线程暂停和唤醒方法
        private void pauseOrContinue(){
            if(toPause){
                synchronized(this){
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                isPaused=true;
                toPause=false;
            }
            if(toContinue){
                synchronized (this){
                   this.notify();
                }
//                this.notify();
                isPaused=false;
                toContinue=false;
            }
        }

        @Override
        public void run() {

            Toast.makeText(MainActivity.this,actualTime,Toast.LENGTH_SHORT);

            //若刚刚才创建线程（第一次调用run()方法）
//            if(flagRun){
//
////                //线程运行时，为计时区赋予初始时间字符串
//////                startTime="13:00:00";
////                minutes=0;
////                hours=13;
//                flagRun=false;
//            }

//            actualDurationFormatMinutes++;
//                if(actualDurationFormatMinutes>=60){
//                    actualDurationFormatHours++;
//                    actualDurationFormatMinutes=0;
//                }
            actualDurationFormatSeconds++;
//            if(actualDurationFormatSeconds>=60) {
//                actualDurationFormatMinutes++;
//                actualDurationFormatSeconds=0;
//                if(actualDurationMinutes>=60){
//                    actualDurationFormatHours++;
//                    actualDurationFormatMinutes=0;
//                }
//            }

//            //线程运行时，为计时区赋予时间字符串
//            String startTime="13:00:00";
            //为计时变量赋予起始计时时间

            mTvTime.setText(String.format("%02d:%02d:%02d", actualDurationFormatHours, actualDurationFormatMinutes,actualDurationFormatSeconds));
            //记录用户实际持续时长（分）
//            actualDurationMinutes=actualDurationFormatHours*60+actualDurationFormatMinutes+actualDurationFormatSeconds/60;
//            if(toPause || toContinue){
//                this.pauseOrContinue();
//            }
//            if(actualDurationMinutes>=settedDurationMinutes){
//                toPause=true;
//                settedDurationMinutes=24*60;
//                this.pauseOrContinue();
//            }
            mHandler.postDelayed(this,1000);

//            if(!actualMoreThanSetted && !toPause ){
////                mHandler.postDelayed(this,1000*60);
////            }

//            if(actualMoreThanSetted){
//                actualMoreThanSetted=false;
//                toPause=true;
//                this.pauseOrContinue(true);
//            }
//            if(toPause){
//                this.pauseOrContinue(true);
//            }
//            if(!toPause)

//            //将起始时间转换为字符序列
//            CharSequence mTimeRecord=mTvTime.getText();
//            String mTimeRecordString;
//            //将字符序列转换为字符串
//            mTimeRecordString=mTimeRecord.toString();
//            Toast.makeText(MainActivity.this,mTimeRecordString,Toast.LENGTH_SHORT).show();
            //延后1秒将线程运行对象this加到消息队列中,此处作用是每隔一秒就调用线程处理对象更改计时区文字
//            else if (actualDurationMinutes<=settedDurationMinutes ){
//                isPaused=false;
//                this.pauseOrContinue();
//            }
//            else{
//                this.pauseOrContinue();
////                synchronized(this){
////                    try {
////                        this.wait();
////                    } catch (InterruptedException e) {
////                        e.printStackTrace();
////                    }
////                }
//            }

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



//    //获取当前时间方法
//    private  static String getTime(){
//        //格式化当前时间
//        SimpleDateFormat format=new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
//        //返回格式化的时间
//        return format.format(new Date());
//    }

    //自定义UI以及按钮监听器
    private void initUI() {
        //计时区文字
        mTvTime=(TextView)findViewById(R.id.tv_time);
        //开始按钮
        Button buStart=(Button)findViewById(R.id.bu_start);
        //停止按钮
        Button buStop=(Button)findViewById(R.id.bu_stop);
        //暂停按钮
        Button buWait=(Button)findViewById(R.id.bu_wait) ;

        //为按钮设置监听器
        buStart.setOnClickListener(this);
        buStop.setOnClickListener(this);
        buWait.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toast.makeText(this,actualTime,Toast.LENGTH_SHORT);
        MainActivity mainActivity=new MainActivity();
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
                //获取当前时间
//                SimpleDateFormat format=new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
//                this.actualTime=format.format(new Date());
                Toast.makeText(MainActivity.this,"wtf",Toast.LENGTH_SHORT);
                //若线程未运行……
                if(mRunnable==null) {
                    //……为其赋予新的运行对象
                    mRunnable=new MyRunnable();
//                    //将run()第一次运行标志设为true
//                    this.flagRun=true;
                    //无延时（立即）将运行对象放入消息队列，即调用线程处理对象
                    mHandler.postDelayed(mRunnable,0);
                    Toast.makeText(MainActivity.this,"wtf",Toast.LENGTH_SHORT);
                }
                break;

            //点击停止按钮
            case R.id.bu_stop:
                //将线程运行对象从消息队列移除，即取消计时
                mHandler.removeCallbacks(mRunnable);
                //无效化线程运行对象
                mRunnable=null;
                //将分钟表示和时钟表示重置
                this.actualDurationFormatHours=0;
                this.actualDurationFormatMinutes=0;
                this.actualDurationFormatSeconds=-1;
//                Toast.makeText(MainActivity.this,"wtf",Toast.LENGTH_LONG);
                break;

            //点击暂停按钮
            case R.id.bu_wait:
                if(!isPaused) {
                    this.toPause = true;
                }
                else{
                    this.toContinue=true;
                    mRunnable.pauseOrContinue();
                }


        }
    }
}
