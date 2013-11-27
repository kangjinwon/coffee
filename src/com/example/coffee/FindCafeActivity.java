package com.example.coffee;
import android.os.Bundle;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

public class FindCafeActivity extends FragmentActivity {
	private GoogleMap mgooglemap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.findcafe);
		setUpMapIfNeeded();
	}
	@Override
	protected void onResume(){
		super.onResume();
		setUpMapIfNeeded();
	}
	private void setUpMapIfNeeded() {
		if(mgooglemap==null){
			mgooglemap = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.fragment)).getMap();
			if(mgooglemap!=null){
				setUpMap();
			}
		}
	}

	private void setUpMap() {
		mgooglemap.addMarker(new MarkerOptions().position(new LatLng(37.22245294756333, 127.18674659729004)).title("Marker"));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}