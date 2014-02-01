package com.rngtng.irdude;

import java.util.List;

import com.rngtng.irdude.database.Command;
import com.rngtng.irdude.database.ControlAndPower;
import com.rngtng.irdude.irs.IRConsumer;
import com.rngtng.irdude.irs.IRManager;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class SelectControl extends Activity {
	private int category;
	private int brand;
	private ArrayAdapter<ControlAndPower> listed;
	private ListView listView;
	private ControlAndPower selected=null;
	private IRConsumer ir;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_control);
		
		ir = IRManager.getCurrentIRConssumer(this);
		
		Bundle extras=getIntent().getExtras();		
		category=extras.getInt("category");
		brand=extras.getInt("brand");
		Log.d("Received", "SelectBrands Received Value "+category);
		
		populateList();
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
					ir.sendCommand(command);
				} catch (Exception e) {
					Log.e("IrSend", "Send Power Teste");
				}
			}
		});
		
	}

}
