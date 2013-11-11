package com.example.coffee;

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
        //Title Bar ����
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
			//fade in ���� fade out ���� ȭ�� ���� �ִϸ��̼�
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		}
	};
	// ��Ʈ�� ȭ���߰��� �ڷΰ��� ������ �������� 4���� ���� ������ �ߴ°��� ����
	@Override
	public void onBackPressed(){
		super.onBackPressed();
		h.removeCallbacks(irun);
	}
}