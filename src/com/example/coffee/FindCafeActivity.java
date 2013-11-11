package com.example.coffee;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class FindCafeActivity extends Activity
{
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findcafe);
        WebView myWebView = (WebView)findViewById(R.id.webview);
        myWebView.loadUrl("http://m.map.naver.com/");
    }
}