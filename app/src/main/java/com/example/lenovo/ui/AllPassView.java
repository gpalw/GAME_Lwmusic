package com.example.lenovo.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.example.lenovo.lwmusic.R;
/**
 * 通关画面
 * Created by lenovo on 2015/8/10.
 */
public class AllPassView extends Activity {
    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.all_pass_view);

        //隐藏右上角的金币按钮
        FrameLayout view = (FrameLayout) findViewById(R.id.layout_bar_coin);
        view.setVisibility(View.INVISIBLE);

        ImageButton button= (ImageButton) findViewById(R.id.all_pass_weixin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AllPassView.this,"微信端还没有处理好~~",Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton button1= (ImageButton) findViewById(R.id.all_pass_add);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AllPassView.this,"弹出加好友的也是没有处理好~~",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
