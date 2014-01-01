package com.rngtng.irdude;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.rngtng.irdude.database.Command;
import com.rngtng.irdude.database.RemotesDatabase;
import com.rngtng.irdude.utils.CommonValues;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

public class MainActivity extends Activity {
	private SparseArray<String> irData;
	private final int SELECT_CATEGORY_REQUEST = 1; 
	public static RemotesDatabase bd;
	private IrControl ir;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);				
		bd=new RemotesDatabase(this);
		setContentView(R.layout.activity_main);
	 	
		irData = new SparseArray<String>();
		irData.put(
				R.id.buttonPower,
				IrControl.db2data(36036,3,"Toggle",null,null,"96 32 16 32 16 16 16 16 16 32 32 16 16 16 16 16 16 16 16 16 16 16 16 16 16 16 16 16 16 16 16 16 16 16 32 16 16 32 16 16 16 3036","96 31 16 31 16 16 16 16 47 47 16 16 16 16 16 16 16 16 16 16 16 16 16 16 16 16 16 16 16 16 16 16 31 16 16 31 16 16 16 3043",null,null,null)
				);
		irData.put(
				R.id.buttonMute,
				IrControl.db2data(42553,3,"Full_Repeat","7 118 7 364 7 118 7 364 7 118 7 364 7 118 7 119 7 118 7 241 7 241 7 241 7 241 7 1126",null,null,null,null,null,null)
				);
		irData.put(
				R.id.buttonVolDown,
				IrControl.db2data(38461,1,"Toggle",null,null,"32 36 67 35 33 36 33 35 33 36 33 35 33 36 33 35 33 70 33 36 66 36 33 3574","32 36 33 36 66 36 33 35 33 36 33 35 33 36 33 35 33 70 33 36 66 36 33 3574",null,null,null)
				);
		irData.put(
				R.id.buttonVolUp,
				IrControl.db2data(36036,3,"Toggle",null,null,"32 32 32 32 64 32 32 32 32 32 32 32 32 32 32 32 32 64 32 32 64 32 32 3261","32 32 64 32 32 32 32 32 32 32 32 32 32 32 32 32 32 64 32 32 64 32 32 3261",null,null,null)
				);
		irData.put(
				R.id.buttonChnP,
				IrControl.db2data(38095,3,"Toggle",null,null,"34 34 69 34 34 34 34 34 34 34 34 34 34 34 34 34 34 69 34 34 69 34 34 3533","34 34 34 34 67 34 34 34 34 34 34 34 34 34 34 34 34 67 34 34 67 34 34 3534",null,null,null)
				);
		irData.put(
				R.id.buttonInput,
				IrControl.db2data(36000,1,"Full_Repeat","96 29 17 30 17 14 17 14 17 30 32 14 17 14 17 14 17 14 17 14 17 14 17 14 17 14 17 14 17 14 17 14 17 14 32 14 17 30 17 14 17 3021 96 29 17 30 17 14 17 14 17 30 32 14 17 14 17 14 17 14 17 14 17 14 17 14 17 14 17 14 17 14 17 14 17 14 32 14 17 30 17 14 18 3021 96 29 17 30 17 14 17 14 17 30 32 14 17 14 17 14 17 14 17 14 17 14 17 14 17 14 17 14 17 14 17 14 17 14 32 14 17 30 17 14 17 3587",null,null,null,null,null,null)
				);
		irData.put(
				R.id.buttonCD,
				IrControl.db2data(38095,3,"Toggle",null,null,"34 34 34 34 69 34 34 34 34 34 34 34 34 34 34 34 34 69 34 34 69 34 34 3539","34 34 69 34 34 34 34 34 34 34 34 34 34 34 34 34 34 69 34 34 69 34 34 3539",null,null,null)
				);
		irData.put(
				R.id.buttonChnM,
				IrControl.db2data(38400,1,"Toggle",null,null,"34 33 35 33 68 33 35 32 35 33 35 32 35 33 35 32 35 67 35 33 68 33 35 3565","34 33 69 32 35 33 35 32 35 33 35 32 35 33 35 32 35 67 35 33 68 33 35 3565",null,null,null)
				);

		ir=new IrControl(this.getSystemService("irda"));
	}
	
	private void setControl(int idControl){
		List<Command> list=bd.getCommands(idControl);
		
		for(Command command: list){
			switch(command.funId){
			case 23: //Power
				irData.put(R.id.buttonPower, IrControl.db2data(command));
				break;
			case 12: //Mute
				irData.put(R.id.buttonMute, IrControl.db2data(command));
				break;
			case 14: //Vol-
				irData.put(R.id.buttonVolDown, IrControl.db2data(command));
				break;
			case 13: //Vol+
				irData.put(R.id.buttonVolUp, IrControl.db2data(command));
				break;
			case 16: //Chan-
				irData.put(R.id.buttonChnM, IrControl.db2data(command));
				break;
			case 15: //Chan+
				irData.put(R.id.buttonChnP, IrControl.db2data(command));
				break;
			case 25: //Input
				irData.put(R.id.buttonInput, IrControl.db2data(command));
				break;
			case 217: //217|CD Input, 464|CD
				irData.put(R.id.buttonCD, IrControl.db2data(command));
				break;
			}
		}
	}
	
	public void changeControl(View view){
		Intent it = new Intent(this, SelectCategory.class);
		startActivityForResult(it, SELECT_CATEGORY_REQUEST);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode){
		case SELECT_CATEGORY_REQUEST:
			if (resultCode == RESULT_OK) {
				int control = data.getIntExtra("control", -1);
	            Log.d("Return", "Select Brand Returned "+control);
	            setControl(control);
			}
			break;
		}
	 }
	
	public void irSend(View view) {
			String data = irData.get(view.getId());
			if(CommonValues.DEBUG){
				Log.d("Ir", data);			
			}else if(data != null) {
				try {
					ir.irSend(data);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
			
	}
}
