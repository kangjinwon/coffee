package com.example.coffee;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MyCafeActivity extends Activity
{
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // �ؽ�Ʈ�並 �����Ѵ�.
        TextView tv = new TextView(this);
        // �ؽ�Ʈ�信 ���ڿ��� �����Ѵ�.
        tv.setText("This is MyCafe Tab");
        // ��Ƽ��Ƽ�� �ؽ�Ʈ�並 ��ġ�Ѵ�.
        setContentView(tv);
    }
}