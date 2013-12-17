package com.rngtng.irdude;

import java.util.List;

import com.rngtng.irdude.database.Control;

import android.os.Bundle;
import android.app.Activity;
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

public class SelectControl extends Activity {
	private int category;
	private int brand;
	private ArrayAdapter<Control> listed;
	private ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_control);
		
		Bundle extras=getIntent().getExtras();		
		category=extras.getInt("category");
		brand=extras.getInt("brand");
		Log.d("Received", "SelectBrands Received Value "+category);
		
		populateList();
		
		// Show the Up button in the action bar.
		setupActionBar();
	}

	private void populateList(){
		listView=(ListView)findViewById(R.id.ListControls);
		
		List <Control> brands=MainActivity.bd.getControls(category,brand);
		
		if(brands==null){
			Intent resultData = new Intent();
        	setResult(Activity.RESULT_CANCELED, resultData);
        	finish();
		}
		
		listed = new ArrayAdapter<Control>(this, android.R.layout.simple_list_item_1,brands);
		
		listView.setAdapter(listed);
		listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {            	
            	Control selectedControl=listed.getItem(position);            	
	            Intent resultData = new Intent();
            	resultData.putExtra("control", selectedControl.id);
            	setResult(Activity.RESULT_OK, resultData);
            	finish();
            }
		});
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
		getMenuInflater().inflate(R.menu.select_control, menu);
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
		}
		return super.onOptionsItemSelected(item);
	}

}
