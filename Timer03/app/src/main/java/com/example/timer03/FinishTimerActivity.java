package com.example.timer03;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class FinishTimerActivity extends AppCompatActivity {

    private Button backMainActivityButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_timer);

        backMainActivityButton=(Button)findViewById(R.id.back_mainActivity);
        backMainActivityButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //点击back按钮回到主页面（计时页面已自动销毁）
                finish();
            }
        });
    }
}
