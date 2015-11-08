package com.example.lenovo.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.lenovo.lwmusic.R;

/**
 * Created by lenovo on 2015/11/8.
 * 引导页
 */
public class FirstActivity extends Activity {
    private Button btn_first;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.first_activity);
        btn_first= (Button) findViewById(R.id.btn_first);
        btn_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FirstActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        super.onCreate(savedInstanceState);
    }
}
