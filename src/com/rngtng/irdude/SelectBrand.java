package com.rngtng.irdude;

import java.util.List;

import com.rngtng.irdude.database.Brand;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
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
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class SelectBrand extends ListActivity {
	private int category=-1;
	private ArrayAdapter<Brand> listed;
	private final int SELECT_CONTROL_REQUEST = 1; 
	private int selectedBrand;
	private Context myContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle extras=getIntent().getExtras();		
		category=extras.getInt("category");
		Log.d("Received", "SelectBrands Received Value "+category);
		
		populateList();
		myContext=this;
	}

	private void populateList(){
		
		ListView listView=this.getListView();
				
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
}
