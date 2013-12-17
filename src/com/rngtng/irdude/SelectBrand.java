package com.rngtng.irdude;

import java.util.List;

import com.rngtng.irdude.database.Brand;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v4.app.NavUtils;

public class SelectBrand extends Activity {
	private int category=-1;
	private ArrayAdapter<Brand> listed;
	private ListView listView;
	private final int SELECT_CONTROL_REQUEST = 1; 
	private int selectedBrand;
	private Context myContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_brand);
		
		Bundle extras=getIntent().getExtras();		
		category=extras.getInt("category");
		Log.d("Received", "SelectBrands Received Value "+category);
		
		populateList();
		myContext=this;
		
		// Show the Up button in the action bar.
		setupActionBar();
	}

	private void populateList(){
		listView=(ListView)findViewById(R.id.ListBrands);
		
		List <Brand> brands=MainActivity.bd.getBrands(category);
		
		if(brands==null){
			Intent resultData = new Intent();
        	setResult(Activity.RESULT_CANCELED, resultData);
        	finish();
		}
		
		listed = new ArrayAdapter<Brand>(this, android.R.layout.simple_list_item_1,brands);
		
		listView.setAdapter(listed);
		listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	Brand nametItemSelected=listed.getItem(position);
            	selectedBrand=nametItemSelected.id;
            	
            	Intent it = new Intent(myContext,SelectControl.class);
            	it.putExtra("category", category);
            	it.putExtra("brand", selectedBrand);
        		startActivityForResult(it, SELECT_CONTROL_REQUEST);
            }
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == SELECT_CONTROL_REQUEST) {
			if (resultCode == RESULT_OK) {
				int control = data.getIntExtra("control", -1);
	            Log.d("Return", "Select Brand Returned "+control);
	            
            	Intent resultData = new Intent();
            	resultData.putExtra("control", control);
            	setResult(Activity.RESULT_OK, resultData);
            	finish();
			}
		}
	 }

	
	
	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select_brand, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.searchBrand:
			Log.d("Button", "SearchBrand pressed");
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
