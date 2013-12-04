package com.example.coffee;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class NewsFeedActivity extends Activity {
	private String return_msg;
	TextView tv;
	private ListView listView;
	private ArrayList<String> arrayList;
	private ArrayAdapter<String> adapter;
	private static final int MENU_ITEM=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String msg;
		msg = "7``";//7 is shownewsfeed() on server
		TCPclient tcpThread = new TCPclient(msg);
		Thread thread = new Thread(tcpThread);
		thread.start();
		setContentView(R.layout.newfeed);
		
		new Handler().postDelayed(new Runnable() {
		     @Override
		     public void run() {
		    	 JSONParser(return_msg);
		     }
		}, 3000); // 5초후 실행
	}
	public boolean onCreateOptionsMenu(Menu menu){
		super.onCreateOptionsMenu(menu);
		menu.setQwertyMode(true);
		MenuItem m0 = menu.add(0,MENU_ITEM,0,"글쓰기");
		{
			m0.setIcon(R.drawable.menu_insert);
		}
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item){
		Toast.makeText(this, "item"+item.getItemId()+" selected", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(this, NewsFeedInsert.class);
        this.startActivity(intent);
		return true;
	}

	private void JSONParser(String msg) {
		arrayList = new ArrayList<String>();
		try {
			JSONObject json = new JSONObject(msg);
			JSONArray jarray = json.getJSONArray("data");
			for (int i = 0; i < jarray.length(); i++) {
				JSONObject jtmp = jarray.getJSONObject(i);
				String name = jtmp.getString("name");
				String date = jtmp.getString("date");
				String brand = jtmp.getString("brand");
				String branch = jtmp.getString("branch");
				String content = jtmp.getString("content");

				// add listview
				arrayList.add("작성자 : " + name + "\n" + "날짜 : " + date +"\n"+"위치 : " +brand+" "+branch+"\n"+"내용 : \n"+content);
			}
			// adapter 객체 생성
			adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, arrayList);

			// 리스트뷰 객체 생성 & 어댑터 설정
			listView = (ListView) findViewById(R.id.listView1);
			listView.setAdapter(adapter);
			listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

			// 리스트뷰에 아이템 클릭 리스너 부착
			listView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View v,
						int position, long id) {
					// 아이템을 터치하면 토스트를 띄운다.
					String str = (String) adapter.getItem(position);
					Toast.makeText(getBaseContext(), str, Toast.LENGTH_SHORT)
							.show();
				}
			});

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private class TCPclient implements Runnable {
		private Socket inetSocket = null;
		private String msg;

		public TCPclient(String _msg) {
			this.msg = _msg;
		}

		@Override
		public void run() {
			try {
				Log.d("TCP", "C: Connecting...");

				inetSocket = new Socket("192.168.36.102", 5555);
				// inetSocket.connect(socketAddr);
				try {
					PrintWriter out = new PrintWriter(
							new BufferedWriter(new OutputStreamWriter(
									inetSocket.getOutputStream())), true);

					out.println(msg);

					BufferedReader in = new BufferedReader(
							new InputStreamReader(inetSocket.getInputStream()));
					return_msg = in.readLine();
					Log.d("TCP", "C: Server send to me this message -->"
							+ return_msg);
				} catch (Exception e) {
					Log.e("TCP", "C: Error1", e);
					Toast.makeText(getApplicationContext(), "c: error" + e,
							Toast.LENGTH_LONG).show();
				} finally {
					inetSocket.close();
				}
			} catch (Exception e) {
				Log.e("TCP", "C: Error2", e);
				Toast.makeText(getApplicationContext(), "c: error2" + e,
						Toast.LENGTH_LONG).show();
			}

		}

	}

}