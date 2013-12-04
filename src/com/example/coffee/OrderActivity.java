package com.example.coffee;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class OrderActivity extends Activity {
	private NfcAdapter nfcAdapter;
	private PendingIntent pendingIntent;
	static private String return_value =null;
	private String return_ip;
	private String return_port;
	private TextView tagDesc;
	static public String currentTagID = "ID:";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order);

		tagDesc = (TextView) findViewById(R.id.tagDesc);

		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		Intent intent = new Intent(this, getClass())
				.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
		tagDesc.setText("NFC¸¦ NFC를 접촉해주세요");

		Button btn = (Button) findViewById(R.id.goBtn);
		btn.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				connectCafe(return_ip, Integer.parseInt(return_port));
			}
		});
	}
	public void getIP(String str){
		String[] temp = return_value.split("``");
		return_ip = temp[0];
		if(temp.length ==2){
			return_port = temp[1];
			
		}
		tagDesc.setText(return_ip +". "+return_port);
	}
	public void getIPfromDB(String tagID) {
		TCPclient_DB tcpThread = new TCPclient_DB(tagID);
		Thread thread = new Thread(tcpThread);
		thread.start();
	}

	public void connectCafe(String ip, int port) {
		TCPclient_cafe tcpThread = new TCPclient_cafe(ip, port);
		Thread thread = new Thread(tcpThread);
		thread.start();
	}

	public void viberate() {
		Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibe.vibrate(5000);
	}

	private class TCPclient_DB implements Runnable {
		private Socket inetSocket = null;
		private String tagID;
		private String ip = "192.168.36.102";
		private int port = 5555;

		public TCPclient_DB(String ID) {
			this.tagID = ID;
		}


		@Override
		public void run() {
			try {
				Log.d("TCP", "C: Connecting...");

				inetSocket = new Socket(ip, port);
				try {
					PrintWriter out = new PrintWriter(
							new BufferedWriter(new OutputStreamWriter(
									inetSocket.getOutputStream())), true);

					out.println("9``"+tagID);

					BufferedReader in = new BufferedReader(
							new InputStreamReader(inetSocket.getInputStream()));
					return_value = in.readLine();
					if(return_value == null){
						return_value = "등록된 ip가 ''없습니다.";
					}
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
	private class TCPclient_cafe implements Runnable {
		private Socket inetSocket = null;
		private String tagID;
		private String ip;
		private int port;
		private boolean isOk = true;
		int count = 0;
		String str = "진행중";


		public TCPclient_cafe(String ip, int port) {
			this.ip = ip;
			this.port = port;
		}

		@Override
		public void run() {
			try {
				Log.d("TCP", "C: Connecting...");
				
				while(true){
					Thread.sleep(1000);
					inetSocket = new Socket(ip, port);
					try {
						PrintWriter out = new PrintWriter(
								new BufferedWriter(new OutputStreamWriter(
										inetSocket.getOutputStream())), true);
						
						out.println("9``"+tagID);
						
						BufferedReader in = new BufferedReader(
								new InputStreamReader(inetSocket.getInputStream()));
						return_value = in.readLine();
						for(int i = 0; i < count; i++){
							for(int j = 0; j < i; j++){
								str += ".";
							}
						}
						Log.d("TCP", return_value + "...");
						Log.d("TCP", "C: ¿¬°áÁß...");
//						tagDesc.setText(str);
						if(return_value.equalsIgnoreCase("done")){
							Log.d("TCP", "C: end_woojin...");
							viberate();
//							tagDesc.setText("done");
							inetSocket.close();
							break;
							
						}else{
						}
					} catch (Exception e) {
						Log.e("TCP", "C: Error1", e);
						Toast.makeText(getApplicationContext(), "c: error" + e,
								Toast.LENGTH_LONG).show();
					} finally {
						
					}
				}
			} catch (Exception e) {
				Log.e("TCP", "C: Error2", e);
				Toast.makeText(getApplicationContext(), "c: error2" + e,
						Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	protected void onPause() {
		if (nfcAdapter != null) {
			nfcAdapter.disableForegroundDispatch(this);
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (nfcAdapter != null) {
			nfcAdapter
					.enableForegroundDispatch(this, pendingIntent, null, null);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		if (tag != null) {
			byte[] tagId = tag.getId();
			tagDesc.setText("NFC(" + toHexString(tagId)
					+ ")가 인식되었습니다.\n연결 하시겠습니까?");
			currentTagID = toHexString(tagId);
		}
	
		getIPfromDB(currentTagID);
		new Handler().postDelayed(new Runnable() {
		     @Override
		     public void run() {
		    	 getIP(return_value);
		     }
		}, 3000);
		System.out.println(currentTagID);
	}

	public static final String CHARS = "0123456789ABCDEF";

	public static String toHexString(byte[] data) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < data.length; ++i) {
			sb.append(CHARS.charAt((data[i] >> 4) & 0x0F)).append(
					CHARS.charAt(data[i] & 0x0F));
		}
		return sb.toString();
	}
}