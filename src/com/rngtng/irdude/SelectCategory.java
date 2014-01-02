package com.rngtng.irdude;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;

public class SelectCategory extends Activity {
	private final int SELECT_BRAND_REQUEST = 1; 
	private int category;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_category);
	}
	
	public void ButtonPress(View view){
		Intent it = new Intent(this, SelectBrand.class);
		category=-1;
		switch(view.getId()){
		case R.id.tv: category=1; break;
		case R.id.stb: category=2; break;
		case R.id.dvd: category=3; break;
		case R.id.blur: category=4; break;
		case R.id.av: category=5; break;
		case R.id.streamingMP: category=6; break;
		case R.id.projector: category=10; break;
		case R.id.dvdHT: category=13; break;
		case R.id.blurHT: category=14; break;
		case R.id.distribuitor: category=15; break;
		case R.id.notebook: category=16; break;
		case R.id.universal: category=17; break;
		case R.id.ac: category=18; break;
		case R.id.dvr: category=20; break;
		case R.id.camera: category=21; break;
		default: Log.wtf("Button", "Button Unregistred");
		}
		it.putExtra("category", category);
		startActivityForResult(it, SELECT_BRAND_REQUEST);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == SELECT_BRAND_REQUEST) {
			if (resultCode == RESULT_OK) {
				int control = data.getIntExtra("control", -1);
	            Log.d("Return", "Select Brand Returned "+control);
	            
            	Intent resultData = new Intent();
            	resultData.putExtra("control", control );
            	setResult(Activity.RESULT_OK, resultData);
            	finish();
			}
		}
	 }

}
