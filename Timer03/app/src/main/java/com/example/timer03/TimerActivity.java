package com.example.timer03;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timer03.common.TwoTuple;
import com.example.timer03.functions.TimerFunctions;
import com.example.timer03.view.CircleView;
import com.example.timer03.view.TimeView;

import java.util.List;

public class TimerActivity extends AppCompatActivity implements View.OnClickListener{

   // private static final String TAG = "TimerActivity";
    //声明转圈计时显示（暂时）
    private CircleView mCircleView;
    //声明圈内时间显示
    private TimeView mTimeView;
    //声明暂停或继续按钮
    private Button buttonPauseOrContinue;
    //声明计圈或退出按钮
    private Button buttonCountRoundOrStop;
    //声明线程消息传递器对象
    private MyHandler mHandler;
//    //声明RecyclerView视图适配器对象
//    private MyRecyclerViewAdapter recyclerViewAdapter;
//    //声明各圈时间显示对象
//    private TextView countRoundRecordsView;
    //声明RecyclerView视图对象
    private RecyclerView mRecyclerView;
    //声明计圈具体信息列表
    private List<String> countRoundRecords;
    //声明计时方法类对象
    private TimerFunctions timerFunctions;

    PowerManager.WakeLock wakeLock=null;
    private boolean wakeLocked=false;

    //假设声明用户预期设定计时时长
    private long settedDurationMinutes=10000;

    //判断是否正在计时
    public boolean isRunning() {
        return timerFunctions.isRunning();
    }

    //获得方法类、视图类对象并设定按钮监听器
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        //获得计时方法对象
        timerFunctions = new TimerFunctions();
        //获得计圈具体信息列表对象
        countRoundRecords = timerFunctions.getCountRoundRecordsList();
        //获得圆圈视图对象
        mCircleView = (CircleView) findViewById(R.id.circle_view);
        //获得时间文字视图对象
        mTimeView = (TimeView) findViewById(R.id.time_view);
        //获得暂停或继续按钮对象
        buttonPauseOrContinue = (Button) findViewById(R.id.pauseOrContinue);
        //获得计圈或停止按钮对象
        buttonCountRoundOrStop = (Button) findViewById(R.id.countRoundOrStop);
        //获得线程信息传递器对象
        mHandler = new MyHandler(TimerActivity.this);

//        //获得循环视图（计圈列表）对象
//        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(TimerActivity.this));
//        //获得循环视图适配器对象
//        recyclerViewAdapter = new MyRecyclerViewAdapter(TimerActivity.this, countRoundRecords);
//        //将适配器对象分配给循环视图
//        mRecyclerView.setAdapter(recyclerViewAdapter);

//        //获得各圈时间显示对象
//        countRoundRecordsView = (TextView) findViewById(R.id.countRoundRecords_text_view);

        //为两个按钮设定监听器
        buttonPauseOrContinue.setOnClickListener(this);
        buttonCountRoundOrStop.setOnClickListener(this);

        //自动开始计时
        startOrContinueTimer();

//        //开始计时
//        timerFunctions.startTimer();
//        //从消息库中获取一条新消息
//        Message message = mHandler.obtainMessage();
//        //延迟10毫秒向线程的消息队列中发送新消息
//        mHandler.sendMessageDelayed(message, 10);
//        //将暂停或继续按钮文字开始换为暂停
//        buttonPauseOrContinue.setText("暂停");
//        //将计圈或停止按钮文字换为计圈
//        buttonCountRoundOrStop.setText("计圈");
    }

    //判断当前活动是否处于后台（即生命周期的onStop）
    @Override
    public void onStop() {
        timerFunctions.pauseTimer();
        buttonPauseOrContinue.setText("继续");

        super.onStop();
        acquireWakeLock();
        Log.d("isWakeLocked",wakeLocked+"");

    }

    //在锁屏状态下保持运行（WakeLock权限机制，可参考https://blog.csdn.net/qq_35136577/article/details/79742818 ）
    public void acquireWakeLock(){
        wakeLocked=true;
        if (null==wakeLock) {
            PowerManager powerManager = (PowerManager) this.getSystemService(this.POWER_SERVICE);
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK|PowerManager.ON_AFTER_RELEASE, "Timer03:TimerActivity");
            if (null != wakeLock) {
                wakeLock.acquire();
            } else {
                Log.d("isWakeLockNull", "yes");
            }
        }
    }


    //更新时间方法（在正在运行的情况下计算从重新继续的时刻到现在的时长（某段时长））
    public void updateTime() {



        //更新时间方法（在正在运行的情况下计算从重新继续的时刻到现在的时长（某段时长））
        timerFunctions.updateTimer();
        //获取当前时间二元组（当前计圈时长，当前总时长）
        TwoTuple<Long, Long> nowTimeTwoTuple = timerFunctions.getNowTimeTwoTuple();
        //获得一条计圈具体信息（当前计圈时长，当前总时长）
//        countRoundRecordsView.setText(timerFunctions.getOneItemAsCountRoundRecord(nowTimeTwoTuple._1, nowTimeTwoTuple._2));

//        //获得一条计圈具体信息并写入一个字符串以便输出
//        String oneItemAsCountRoundRecord=timerFunctions.getOneItemAsCountRoundRecord(nowTimeTwoTuple._1, nowTimeTwoTuple._2);

        //调整弧点的当前角度
        mCircleView.changeProgress((float) nowTimeTwoTuple._1 / 600f);
        //调整显示的时间文字为当前总时长
        mTimeView.setTime(nowTimeTwoTuple._2);

        //判断当前总时长是否超过用户设定预期持续时长
        if(nowTimeTwoTuple._2>=settedDurationMinutes){
            timerFunctions.pauseTimer();
            settedDurationMinutes=(long)Float.POSITIVE_INFINITY;
            buttonPauseOrContinue.setText("继续");
        }

    }

    //开始或继续计时方法
    public void startOrContinueTimer(){
        //开始计时
        timerFunctions.startTimer();
        //从消息库中获取一条新消息
        Message message = mHandler.obtainMessage();
        //延迟10毫秒向线程的消息队列中发送新消息
        mHandler.sendMessageDelayed(message, 10);
        //将暂停或继续按钮文字开始换为暂停
        buttonPauseOrContinue.setText("暂停");
//        //将计圈或停止按钮文字换为计圈
//        buttonCountRoundOrStop.setText("计圈");
    }

//    //将暂停或继续按钮文字设为继续方法
//    public void changeTextPauseToContinue() {
//        buttonPauseOrContinue.setText("继续");
//    }
//
//    //将暂停或继续按钮文字设为暂停方法
//    public void changeTextContinueToPause() {
//        buttonPauseOrContinue.setText("暂停");
//    }



    //点击两个按钮
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //暂停或继续按钮
            case R.id.pauseOrContinue:
                //如果未计时
                if (!isRunning()) {
//                    //开始计时
//                    timerFunctions.startTimer();
//                    //从消息库中获取一条新消息
//                    Message message = mHandler.obtainMessage();
//                    //延迟10毫秒向线程的消息队列中发送新消息
//                    mHandler.sendMessageDelayed(message, 10);
//                    //将暂停或继续按钮文字开始换为暂停
//                    buttonPauseOrContinue.setText("暂停");
//                    //将计圈或停止按钮文字换为计圈
//                    buttonCountRoundOrStop.setText("计圈");
                    //继续计时
                    startOrContinueTimer();
                }
                //如果正在计时（此时按钮上的文字是“暂停”）
                else {

//                    //将计圈或停止按钮文字计圈换为停止
//                    buttonCountRoundOrStop.setText("停止");
                    //暂停计时
                    timerFunctions.pauseTimer();
                    //将暂停或继续按钮文字暂停换为继续
                    buttonPauseOrContinue.setText("继续");

                }
                break;

            //停止按钮
            case R.id.countRoundOrStop:
                //先暂停计时
                timerFunctions.pauseTimer();
                //然后获得用户持续计时时长（用于输出！！！！！！！）
                Long actualDuration=timerFunctions.getNowTime();

                Log.d("actualDuration",actualDuration+"");

                //再停止计时
                timerFunctions.stopTimer();
                //最后转入完成界面
                Intent intent=new Intent(TimerActivity.this,FinishTimerActivity.class);
                startActivity(intent);
                //同时自动销毁本页面
                finish();



//                //如果正在计时
//                if (isRunning()) {
//                    //计圈
//                    timerFunctions.roundCount();
//                    recyclerViewAdapter.notifyDataSetChanged();
//                    //更新时间
//                    updateTime();
////                    Log.e("theSize", countRoundRecords.size()+"");
//                }
//                //如果未计时（则停止）！！！！！！！！！！需修改！
//                else {
//                    //停止计时（退出）
//                    timerFunctions.stopTimer();
//                    //计圈记录清空
//                    countRoundRecordsView.setText("");
//                    // 将暂停或继续按钮文字继续换为开始
//                    buttonPauseOrContinue.setText("开始");
//                    //更新时间
//                    updateTime();
//                }

        }
    }

    //创建消息传递器类，继承消息传递器Handler
    //此为内部类，内部类相关知识可参考https://www.runoob.com/w3cnote/java-inner-class-intro.html
    //又创建为静态类，故只需加载一次
    //总的来说，此为静态内部类，要调用外部非静态方法，必须创建外部类实例来调用
    public static class MyHandler extends Handler {

        //创建外部类实例以便调用外部类非静态的updateTime()方法
        private TimerActivity activity;

        //构造方法
        public MyHandler(TimerActivity activity) {
            this.activity = activity;
        }

        //重写消息传递方法（每过10毫秒自动调用）
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //每过10毫秒自动更新时间
            activity.updateTime();
            if (activity.isRunning()) {
                Message message = obtainMessage();
                sendMessageDelayed(message, 10);
            }
        }
    }

}

