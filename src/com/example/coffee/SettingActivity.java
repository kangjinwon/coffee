package com.example.coffee;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class SettingActivity extends Activity
{
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // 텍스트뷰를 생성한다.
        TextView tv = new TextView(this);
        // 텍스트뷰에 문자열을 설정한다.
        tv.setText("This is Setting Tab");
        // 액티비티에 텍스트뷰를 배치한다.
        setContentView(tv);
    }
}