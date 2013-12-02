package com.example.coffee;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class FindCafeActivity extends FragmentActivity implements
		LocationListener {
	private GoogleMap mmap;
	private LocationManager locationManager;
	private String provider;
	private double lat;
	private double lng;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.findcafe);

		// main 에서 네트워크 쓰레드 사용가능.
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());

		GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(FindCafeActivity.this);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, true);

		// mapki ��ü ��

		/**
		 * 현재위치 표시
		 * 
		 */
		if (provider == null) {
			new AlertDialog.Builder(FindCafeActivity.this)
					.setTitle("google map")
					.setNeutralButton("ok",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									startActivityForResult(
											new Intent(
													android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),
											0);
								}
							})
					.setOnCancelListener(
							new DialogInterface.OnCancelListener() {
								@Override
								public void onCancel(DialogInterface dialog) {
									finish();
								}
							}).show();
		} else {
			locationManager.requestLocationUpdates(provider, 1, 1,
					FindCafeActivity.this);
			setUpMapIfNeeded();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 0:
			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			Criteria criteria = new Criteria();
			provider = locationManager.getBestProvider(criteria, true);
			if (provider == null) {
				finish();
			} else {
				locationManager.requestLocationUpdates(provider, 1L, 2F,
						FindCafeActivity.this);
				setUpMapIfNeeded();
			}
			break;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		locationManager.removeUpdates(this);
	}

	private void setUpMapIfNeeded() {
		if (mmap == null) {
			mmap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.fragment)).getMap();

			if (mmap != null) {// map 실행
				setUpMap();
			}
		}
	}

	private void setUpMap() {
		mmap.setMyLocationEnabled(true);
		mmap.getMyLocation();

		// gps 이용 현재 좌표 받아오기
		myLocation();

		// 카메라 이동
		LatLng location = new LatLng(lat, lng); // 위치 좌표 설정
		CameraPosition cp = new CameraPosition.Builder().target((location))
				.zoom(16).build();
		mmap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));
		searchOnMap("cafe");
	}

	boolean locationTag = true;

	
	//가장 최근위치 좌표 받아오기
	public void myLocation() {
		Location loc = locationManager.getLastKnownLocation(provider);
		lat = loc.getLatitude();
		lng = loc.getLongitude();
	}

	@Override
	public void onLocationChanged(Location location) {
		if (locationTag) {
			Log.d("myLog", "onLocationChanged: !!" + "onLocationChanged!!");
//			Toast.makeText(FindCafeActivity.this,
//					"위도  : " + lat + " 경도 : " + lng, Toast.LENGTH_SHORT).show();
			locationTag = false;
		}

	}

	// Marker 추가 함수
	void addMarker(double lat, double lng, String title, String number) {
		mmap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.coffe_marker))
				.position(new LatLng(lat, lng)).title(title).snippet(number));
	}

	@Override
	public void onBackPressed() {
		this.finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setUpMapIfNeeded();

	}

	// ///////////////// JSON SEARCH
	private void searchOnMap(String search) {
		// Location loc = locationManager.getLastKnownLocation(provider);
		// double lat1 = loc.getLatitude();
		// double lng1 = loc.getLongitude();

		StringBuilder responseBuilder = new StringBuilder();
		// URL을 이용하여 검색신청을 하고, 결과를 받아 responseBuilder에 저장한다.
		try {

			URL url = new URL(
					"http://ajax.googleapis.com/ajax/services/search/local?v=1.0&q="
							+ URLEncoder.encode(search, "UTF-8") + "&sll="
							+ lat + "," + lng + "&hl=kr" + "&rsz=8");
			BufferedReader in = new BufferedReader(new InputStreamReader(
					url.openStream(), "UTF-8")); // 8.13 수정
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				responseBuilder.append(inputLine);
			}
			in.close();

		} catch (MalformedURLException me) {
			me.printStackTrace();
		} catch (UnsupportedEncodingException ue) {
			ue.printStackTrace();
		} catch (IOException ie) {
			ie.printStackTrace();
		}
		// 위의 결과를 JSONObject를 이용해 원하는 정보를 parsing 한다
		try {
			JSONObject json = new JSONObject(responseBuilder.toString());

			json = json.getJSONObject("responseData");
			JSONArray jarray = json.getJSONArray("results");

			Log.d("jarray.length() : ", Integer.toString(jarray.length()));

			for (int i = 0; i < jarray.length(); i++) {
				// 결과별로 결과 object 얻기
				JSONObject jtmp = jarray.getJSONObject(i);

				String streetAddr = jtmp.getString("streetAddress"); // street
																		// 주소 파싱
				String title = jtmp.getString("titleNoFormatting"); // html 이
																	// 포함되지 않은
																	// 주소

				JSONArray jarray_tmp = jtmp.getJSONArray("phoneNumbers"); // 번호
																			// 파싱
				JSONObject jtmp2 = jarray_tmp.getJSONObject(0); // 처음 번호만 파싱함
				String number = jtmp2.getString("number");

				// full address 파싱
				String addr_tmp = jtmp.getString("addressLines"); // addressLines(ket)
																	// 값을 얻어온다.
				String[] addr_tmp2 = addr_tmp.split("\""); // ["...."] 이 구조이기
															// 때문에 중간 문자열만 가지고온다
				String addr = addr_tmp2[1]; // { "[" , ".....", "]" } 이렇게 되어있는
											// 것에서 중간 문자열을 선택

				// 주소를 이용하여 위도경도 얻어오기
				// Geocoder를 이용하면 위도경도로 주소를 얻어오거나
				// 주소를 이용해 위도 경도의 정보를 얻어 올 수 있다

				List<Address> addrList = null;
				Geocoder geocoder = new Geocoder(this, Locale.KOREAN);
				try {
					addrList = geocoder.getFromLocationName(addr, 2);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// `` // 주소정보만
				// // 얻어온다
				Address adr_tmp = addrList.get(0); // list에서 사용할 Address Object를
													// 가지고 온다
				String[] location = new String[2];
				location[0] = Double.toString(adr_tmp.getLatitude()); // 위도
				location[1] = Double.toString(adr_tmp.getLongitude()); // 경도
				//
				// JSon Parsing 목록
				// result += "Title : " + title + "\n";
				// result += "Address : " + addr + "\n";
				// result += "StreetAddr : " + streetAddr + "\n";
				// result += "Phone : " + number + "\n";
				// result += "Latitude : " + location[0] + "\n";
				// result += "Longitude : " + location[1] + "\n\n";

				// marker 추가
				addMarker(adr_tmp.getLatitude(), adr_tmp.getLongitude(), title,
						number);
			}

			Log.d("dd", jarray.getString(0));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}
}
