package com.example.coffee;
//This is Intro Activity
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;



public class IntroActivity extends Activity
{
	Handler h;
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Title Bar 제거
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.intro);
        h = new Handler();
        h.postDelayed(irun, 4000);
    }
    Runnable irun = new Runnable() {
		@Override
		public void run() {
			Intent i = new Intent(IntroActivity.this, MainActivity.class);
			startActivity(i);
			finish();
			//fade in 시작 fade out 으로 화면 종료 애니메이션
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		}
	};
	// 인트로 화면중간에 뒤로가기 눌러서 꺼졋을시 4초후 메인 페이지 뜨는것을 방지
	@Override
	public void onBackPressed(){
		super.onBackPressed();
		h.removeCallbacks(irun);
	}
}