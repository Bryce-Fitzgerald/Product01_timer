package com.example.timer03.functions;

import com.example.timer03.TimerActivity;
import com.example.timer03.common.TwoTuple;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//计时方法类
public class TimerFunctions {

    //某段计时时长
    private long someTime;
    //计时实际开始时间，也是重新继续的时刻
    private long actualTime;
    //计时实际总时长
    private long sumTime;
    //计圈时从点击该次计圈开始到上次暂停的时长
    private long countRoundMomentToLastPause;
    //计圈圈数
    private long countRound;
    //判断是否正在计时的标志
    public boolean isRunning;
    //计圈的具体信息列表，包括当前计圈数、该圈时长（某段时长，即从该次继续到该次暂停或至今之间的时间）、该次暂停前或至今的总时长
    private List<String> countRoundRecords;
    //日期格式对象
    private SimpleDateFormat simpleDateFormat;

    //声明计时活动对象
    private TimerActivity timerActivity;

    //构造方法，一旦创建计时方法类对象，即自定义时间格式并且初始设定计时相关参数值
    public TimerFunctions() {
        //创建分：秒.毫秒的时间格式
        simpleDateFormat = new SimpleDateFormat("mm:ss.SS");
        //设定初始参数值
        init();
    }

    //开始计时方法，并默认开始计圈（第0圈）
    public void startTimer() {
        //设定标志为正在计时
        isRunning = true;
        //取得实际开始时间，即为自从 January 1, 1970, 00:00:00 GMT以来的毫秒数（long）
        actualTime = new Date().getTime();
    }

    //从暂停到继续的继续计时方法
    public void continueTimer() {
        //如果未计时
        if (!isRunning()) {
            //状态换为正在计时
            isRunning = true;
            //再次获得当前实际开始时间
            actualTime = new Date().getTime();
        }
    }

    //暂停计时方法
    public void pauseTimer() {
        //状态换为未计时
        isRunning = false;
        //获得从最初开始至今的总持续时长=上次暂停前已记录时长+从重新继续的时刻到现在的某段时长
        sumTime = sumTime + lastContinueToCurrentTime();
        //从点击该次计圈开始到上次暂停的时长更新，加上上次暂停段时长
        countRoundMomentToLastPause += someTime;
        //将从重新继续的时刻到现在的某段时长重置为0
        someTime = 0;
//        //将计时活动暂停按钮文字设为继续
//        timerActivity.changeTextPauseToContinue();
    }

    //停止计时方法（即退出）
    //!!!!!!!!!!!!!!此时必须在暂停情况下才能停止，所以之后需要改为随时退出：思路为点击停止，调用暂停方法，然后重置（再以后重置应该换为跳转到完成界面）并且返回实际持续时长
    public void stopTimer() {
        //如果未计时
        if (!isRunning()) {
            //重置，调用设定初始参数方法
            init();
        }
    }

    //计算从重新继续的时刻到现在的时长（返回某段时长）
    private long lastContinueToCurrentTime() {
        //获得现在时间
        long now = new Date().getTime();
        //获得从继续时刻到现在的时长
        someTime = now - actualTime;
        //返回从继续时刻到现在的时长
        return someTime;
    }

    //计圈方法
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!之后删去此方法
    public void roundCount() {
        //如果正在计时
        if (isRunning()) {
            //设t为从重新继续的时刻到现在的时长（某段时长）
            long t = lastContinueToCurrentTime();
            //设countRoundT为当前计圈时长=从点击该次计圈开始到上次暂停的时长+从重新继续的时刻到现在的时长（某段时长）
            long countRoundT = countRoundMomentToLastPause + t;
            //设allT为当前实际总时长=上次暂停前总时长+从重新继续的时刻到现在的时长（某段时长）
            long allT = sumTime + t;
            //将该圈具体信息放入计圈记录序列，调用时间转换格式方法将毫秒数转换为自定义时间格式
            countRoundRecords.add("No." + countRound + "    " + changeTimeFormat(countRoundT) +"    " + changeTimeFormat(allT));
            //圈数+1
            countRound++;
            //从点击该次计圈开始到上次暂停的时长重置为0
            countRoundMomentToLastPause = 0;
            //点击计圈后记录实际总时长=上次暂停前总时长+t调用lastContinueToCurrentTime()方法后得知的到目前计圈为止的某段时长
            sumTime += someTime;
            //将从上次暂停到目前计圈为止的某段时长重置为0
            someTime = 0;
            //更新当前实际时间
            actualTime = new Date().getTime();
        }
    }

    //获得计圈具体信息列表
    public List<String> getCountRoundRecordsList() {
        return countRoundRecords;
    }

    //获得一条计圈具体信息（当前计圈时长，当前总时长）
    public String getOneItemAsCountRoundRecord(long roundTime, long allTime) {
        return "No." + countRound + "    " + changeTimeFormat(roundTime) +"    " + changeTimeFormat(allTime);
    }

    //获取当前时间二元组（当前计圈时长，当前总时长）
    public TwoTuple<Long, Long> getNowTimeTwoTuple() {
        //调用更新时间方法，计算从重新继续的时刻到现在的时长（某段时长）
        updateTimer();
        //设t等于更新后返回的某段时长
        long t = someTime;
        //创建包括当前计圈时长和当前总时长的二元组
        return new TwoTuple<Long,Long>(countRoundMomentToLastPause + t, sumTime + t);
    }

    //获取当前总时长
    public Long getNowTime() {
        return sumTime;
    }

    //更新时间方法（在正在运行的情况下计算从重新继续的时刻到现在的时长（某段时长））
    public void updateTimer() {
        //如果正在计时
        if (isRunning()) {
            //计算从重新继续的时刻到现在的时长（返回某段时长）
            lastContinueToCurrentTime();
        }
    }

    //判断是否正在计时方法
    public boolean isRunning() {
        //返回是否正在计时标志
        return isRunning;
    }

    //设定初始参数值
    public void init() {
        //初始尚未计时
        isRunning = false;
        someTime = 0;
        actualTime = 0;
        sumTime = 0;
        //?
        countRoundMomentToLastPause = 0;
        countRound = 0;
        //?
        if (null == countRoundRecords) {
            countRoundRecords = new ArrayList<>();
        } else {
            countRoundRecords.clear();
        }
    }

    //转换时间格式，将long类型（毫秒数）转换为特定的时间格式（"mm:ss.SS"）
    public String changeTimeFormat(long t) {
        return simpleDateFormat.format(new Date(t));
    }

}
