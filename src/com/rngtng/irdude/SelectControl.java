package com.rngtng.irdude;

import java.util.List;

import com.rngtng.irdude.database.Command;
import com.rngtng.irdude.database.ControlAndPower;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v4.app.NavUtils;

public class SelectControl extends Activity {
	private int category;
	private int brand;
	private ArrayAdapter<ControlAndPower> listed;
	private ListView listView;
	private ControlAndPower selected=null;
	private IrControl ir;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_control);
		
		ir=new IrControl(this.getSystemService("irda"));
		
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
		
		List <ControlAndPower> controls=MainActivity.bd.getControlsAndPower(category,brand);
		
		if(controls==null){
			Intent resultData = new Intent();
        	setResult(Activity.RESULT_CANCELED, resultData);
        	finish();
		}
		
		listed = new ArrayAdapter<ControlAndPower>(this, android.R.layout.simple_list_item_1,controls);
		
		listView.setAdapter(listed);
		listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) { 
            	selected=listed.getItem(position);
            }
		});
		
		
		//set OnClick on XML file don't work :.(
		((Button)findViewById(R.id.buttonChsConChose)).setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				if( selected == null )
					return;
	            Intent resultData = new Intent();
	        	resultData.putExtra("control", selected.id);
	        	setResult(Activity.RESULT_OK, resultData);
	        	finish();
			}
		});
		
		((Button)findViewById(R.id.buttonChsConPower)).setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				if( selected == null )
					return;
				Command command=selected.power;
				try {
					ir.irSend(IrControl.db2data(command));
				} catch (Exception e) {
					Log.e("IrSend", "Send Power Teste");
				}
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
