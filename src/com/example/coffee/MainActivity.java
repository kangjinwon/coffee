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
        // main.xml 파일에 구성한 리소스를 이 액티비티에서 사용한다.
        setContentView(R.layout.activity_main);

        // 이 탭액티비티에서 사용할 수 있는 TabHost 객체를 얻는다. 
        TabHost tab_host = getTabHost();  
        // 각 탭에 사용할 TabSpec 객체.
        // 탭호스트에 TabWidget 과 FrameLayout 이 사용할 정보를 넘겨주는 역할을 한다.
        TabHost.TabSpec spec;
        // 각 탭의 FrameLayout 이 사용하는 액티비티를 구성하는 객체
        Intent intent;

        // 탭에서 액티비티를 사용할 수 있도록 인텐트를 생성한다.
        intent = new Intent().setClass(this, NewsFeedActivity.class);
        // "music" 이라는 태그 값을 가진 TabSpec 객체를 생성한다.
        spec = tab_host.newTabSpec("newsfeed");
        // TabSpec 객체에 TabWidget 객체가 출력할 탭의 이름을 설정한다.
        spec.setIndicator("뉴스피드",getResources().getDrawable(R.drawable.newsfeed));
        // TabSpec 객체에 FrameLayout 이 출력할 페이지를 설정한다.
        spec.setContent(intent);
        // 탭호스트에 해당 정보를 가진 탭을 추가한다.
        tab_host.addTab(spec);

        
        //
        intent = new Intent().setClass(this, FindCafeActivity.class);
        spec = tab_host.newTabSpec("findCafe");
        spec.setIndicator("카페찾기",getResources().getDrawable(R.drawable.findcafe));
        spec.setContent(intent);
        tab_host.addTab(spec);

        intent = new Intent().setClass(this, MyCafeActivity.class);
        spec = tab_host.newTabSpec("myCafe");
        spec.setIndicator("마이카페",getResources().getDrawable(R.drawable.mycafe));
        spec.setContent(intent);
        tab_host.addTab(spec);
        
        intent = new Intent().setClass(this, ExtraActivity.class);
        spec = tab_host.newTabSpec("extra");
        spec.setIndicator("부가기능",getResources().getDrawable(R.drawable.more));
        spec.setContent(intent);
        tab_host.addTab(spec);
        
        intent = new Intent().setClass(this, SettingActivity.class);
        spec = tab_host.newTabSpec("setting");
        spec.setIndicator("설정",getResources().getDrawable(R.drawable.setting));
        spec.setContent(intent);
        tab_host.addTab(spec);
        

        // 첫번째 탭을 선택한 상태로 지정한다.
        tab_host.setCurrentTab(0);
    }
}