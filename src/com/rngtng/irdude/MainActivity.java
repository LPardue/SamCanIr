package com.rngtng.irdude;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.rngtng.irdude.database.Command;
import com.rngtng.irdude.database.RemotesDatabase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

public class MainActivity extends Activity {
	private Object irdaService;
	private Method irWrite;
	private SparseArray<String> irData;
	private final int SELECT_CATEGORY_REQUEST = 1; 
	private final boolean DEBUG = true; 
	public static RemotesDatabase bd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);				
		bd=new RemotesDatabase(this);
		setContentView(R.layout.activity_main);
	 	
		irData = new SparseArray<String>();
		irData.put(
				R.id.buttonPower,
				db2data(36036,3,"Toggle",null,null,"96 32 16 32 16 16 16 16 16 32 32 16 16 16 16 16 16 16 16 16 16 16 16 16 16 16 16 16 16 16 16 16 16 16 32 16 16 32 16 16 16 3036","96 31 16 31 16 16 16 16 47 47 16 16 16 16 16 16 16 16 16 16 16 16 16 16 16 16 16 16 16 16 16 16 31 16 16 31 16 16 16 3043",null,null,null)
				);
		irData.put(
				R.id.buttonMute,
				db2data(42553,3,"Full_Repeat","7 118 7 364 7 118 7 364 7 118 7 364 7 118 7 119 7 118 7 241 7 241 7 241 7 241 7 1126",null,null,null,null,null,null)
				);
		irData.put(
				R.id.buttonVolDown,
				db2data(38461,1,"Toggle",null,null,"32 36 67 35 33 36 33 35 33 36 33 35 33 36 33 35 33 70 33 36 66 36 33 3574","32 36 33 36 66 36 33 35 33 36 33 35 33 36 33 35 33 70 33 36 66 36 33 3574",null,null,null)
				);
		irData.put(
				R.id.buttonVolUp,
				db2data(36036,3,"Toggle",null,null,"32 32 32 32 64 32 32 32 32 32 32 32 32 32 32 32 32 64 32 32 64 32 32 3261","32 32 64 32 32 32 32 32 32 32 32 32 32 32 32 32 32 64 32 32 64 32 32 3261",null,null,null)
				);
		irData.put(
				R.id.buttonChnP,
				db2data(38095,3,"Toggle",null,null,"34 34 69 34 34 34 34 34 34 34 34 34 34 34 34 34 34 69 34 34 69 34 34 3533","34 34 34 34 67 34 34 34 34 34 34 34 34 34 34 34 34 67 34 34 67 34 34 3534",null,null,null)
				);
		irData.put(
				R.id.buttonInput,
				db2data(36000,1,"Full_Repeat","96 29 17 30 17 14 17 14 17 30 32 14 17 14 17 14 17 14 17 14 17 14 17 14 17 14 17 14 17 14 17 14 17 14 32 14 17 30 17 14 17 3021 96 29 17 30 17 14 17 14 17 30 32 14 17 14 17 14 17 14 17 14 17 14 17 14 17 14 17 14 17 14 17 14 17 14 32 14 17 30 17 14 18 3021 96 29 17 30 17 14 17 14 17 30 32 14 17 14 17 14 17 14 17 14 17 14 17 14 17 14 17 14 17 14 17 14 17 14 32 14 17 30 17 14 17 3587",null,null,null,null,null,null)
				);
		irData.put(
				R.id.buttonCD,
				db2data(38095,3,"Toggle",null,null,"34 34 34 34 69 34 34 34 34 34 34 34 34 34 34 34 34 69 34 34 69 34 34 3539","34 34 69 34 34 34 34 34 34 34 34 34 34 34 34 34 34 69 34 34 69 34 34 3539",null,null,null)
				);
		irData.put(
				R.id.buttonChnM,
				db2data(38400,1,"Toggle",null,null,"34 33 35 33 68 33 35 32 35 33 35 32 35 33 35 32 35 67 35 33 68 33 35 3565","34 33 69 32 35 33 35 32 35 33 35 32 35 33 35 32 35 67 35 33 68 33 35 3565",null,null,null)
				);

		irInit();
	}
	
	private void setControl(int idControl){
		List<Command> list=bd.getControl(idControl);
		
		for(Command command: list){
			switch(command.funId){
			case 23: //Power
				irData.put(R.id.buttonPower, db2data(command));
				break;
			case 12: //Mute
				irData.put(R.id.buttonMute, db2data(command));
				break;
			case 14: //Vol-
				irData.put(R.id.buttonVolDown, db2data(command));
				break;
			case 13: //Vol+
				irData.put(R.id.buttonVolUp, db2data(command));
				break;
			case 16: //Chan-
				irData.put(R.id.buttonChnM, db2data(command));
				break;
			case 15: //Chan+
				irData.put(R.id.buttonChnP, db2data(command));
				break;
			case 25: //Input
				irData.put(R.id.buttonInput, db2data(command));
				break;
			case 217: //217|CD Input, 464|CD
				irData.put(R.id.buttonCD, db2data(command));
				break;
			}
		}
	}

	public void irInit() {
		if(!DEBUG){
			irdaService = this.getSystemService("irda");
			Class c = irdaService.getClass();
			Class p[] = { String.class };
			try {
				irWrite = c.getMethod("write_irsend", p);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}else{
			Log.d("IRsend", "Sending IR");
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
			if (data != null) {
				try {
					if(DEBUG){
						Log.d("Ir", data);
					}else{
						irWrite.invoke(irdaService, data);
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		
	}
	
	protected String db2data(Command command){
		return db2data(command.frequency,command.repeatcount,command.desType,command.mainframe,command.repeatframe,
				command.toggleframe1,command.toggleframe2,command.toggleframe3,command.toggleframe4,
				command.endframe);
	}
	
	protected String db2data(int frequency,int repeatCount,String desType,
			String mainframe,String repeatframe,
			String toggleframe1,String toggleframe2,
			String toggleframe3,String toggleframe4,
			String endframe){		
		
	    String str1=String.valueOf(frequency);
	    if( desType.equalsIgnoreCase("Toggle") ) //Full_Repeat and Partial_Repeat?
	    	str1 += "," + toggleframe1;
	    while (repeatCount > 0){
	      StringBuilder localStringBuilder = new StringBuilder(str1);
	      if (repeatframe != null && repeatframe.length() > 0){
	        for (int k = 0; k < repeatCount; k++)
	          localStringBuilder.append(",").append(repeatframe);
	        str1 = mainframe;
	      }else{
	        for (int j = 1; j < repeatCount; j++)
	          localStringBuilder.append(",").append(str1);
	        return localStringBuilder.toString().replace(' ', ',');
	      }
	    }
	    return str1.replace(' ', ',');
	    //*/
		/*
		String code=String.valueOf(frequency);
		if( desType.equalsIgnoreCase("Toggle") ){
			code += "," + toggleframe1;
			return code.replace(' ', ',');
		}else if( desType.equalsIgnoreCase("Full_Repeat") ){
		    if (repeatframe != null && repeatframe.length() > 0){
		    	  
		    }
			code += "," + toggleframe1;
			return code.replace(' ', ',');
			
		}else if( desType.equalsIgnoreCase("Partial_Repeat") ){
			
		}*/
	}

	protected String hex2dec(String irData) {
		List<String> list = new ArrayList<String>(Arrays.asList(irData
				.split(" ")));
		list.remove(0); // dummy
		int frequency = Integer.parseInt(list.remove(0), 16); // frequency
		list.remove(0); // seq1
		list.remove(0); // seq2

		for (int i = 0; i < list.size(); i++) {
			list.set(i, Integer.toString(Integer.parseInt(list.get(i), 16)));
		}

		frequency = (int) (1000000 / (frequency * 0.241246));
		list.add(0, Integer.toString(frequency));

		irData = "";
		for (String s : list) {
			irData += s + ",";
		}
		return irData;
	}

}
