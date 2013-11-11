package com.example.coffee;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;


public class MainActivity extends TabActivity
{
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // main.xml ���Ͽ� ������ ���ҽ��� �� ��Ƽ��Ƽ���� ����Ѵ�.
        setContentView(R.layout.activity_main);

        // �� �Ǿ�Ƽ��Ƽ���� ����� �� �ִ� TabHost ��ü�� ��´�. 
        TabHost tab_host = getTabHost();  
        // �� �ǿ� ����� TabSpec ��ü.
        // ��ȣ��Ʈ�� TabWidget �� FrameLayout �� ����� ������ �Ѱ��ִ� ������ �Ѵ�.
        TabHost.TabSpec spec;
        // �� ���� FrameLayout �� ����ϴ� ��Ƽ��Ƽ�� �����ϴ� ��ü
        Intent intent;

        // �ǿ��� ��Ƽ��Ƽ�� ����� �� �ֵ��� ����Ʈ�� �����Ѵ�.
        intent = new Intent().setClass(this, NewsFeedActivity.class);
        // "music" �̶�� �±� ���� ���� TabSpec ��ü�� �����Ѵ�.
        spec = tab_host.newTabSpec("newsfeed");
        // TabSpec ��ü�� TabWidget ��ü�� ����� ���� �̸��� �����Ѵ�.
        spec.setIndicator("�����ǵ�",getResources().getDrawable(R.drawable.newsfeed));
        // TabSpec ��ü�� FrameLayout �� ����� �������� �����Ѵ�.
        spec.setContent(intent);
        // ��ȣ��Ʈ�� �ش� ������ ���� ���� �߰��Ѵ�.
        tab_host.addTab(spec);

        
        //
        intent = new Intent().setClass(this, FindCafeActivity.class);
        spec = tab_host.newTabSpec("findCafe");
        spec.setIndicator("ī��ã��",getResources().getDrawable(R.drawable.findcafe));
        spec.setContent(intent);
        tab_host.addTab(spec);

        intent = new Intent().setClass(this, MyCafeActivity.class);
        spec = tab_host.newTabSpec("myCafe");
        spec.setIndicator("����ī��",getResources().getDrawable(R.drawable.mycafe));
        spec.setContent(intent);
        tab_host.addTab(spec);
        
        intent = new Intent().setClass(this, ExtraActivity.class);
        spec = tab_host.newTabSpec("extra");
        spec.setIndicator("�ΰ����",getResources().getDrawable(R.drawable.more));
        spec.setContent(intent);
        tab_host.addTab(spec);
        
        intent = new Intent().setClass(this, SettingActivity.class);
        spec = tab_host.newTabSpec("setting");
        spec.setIndicator("����",getResources().getDrawable(R.drawable.setting));
        spec.setContent(intent);
        tab_host.addTab(spec);
        

        // ù��° ���� ������ ���·� �����Ѵ�.
        tab_host.setCurrentTab(0);
    }
}