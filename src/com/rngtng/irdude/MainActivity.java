package com.rngtng.irdude;

import java.util.List;

import com.rngtng.irdude.database.Command;
import com.rngtng.irdude.database.RemotesDatabase;
import com.rngtng.irdude.irs.IRConsumer;
import com.rngtng.irdude.irs.IRManager;
import com.rngtng.irdude.utils.CommonValues;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

public class MainActivity extends Activity {
	private SparseArray<Command> irData;
	private final int SELECT_CATEGORY_REQUEST = 1; 
	public static RemotesDatabase bd;
	private IRConsumer ir;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);				
		bd=new RemotesDatabase(this);
		setContentView(R.layout.activity_main);
	 	
		
		//new Command(int id, int idControl, int uES, char inputflag, int funId,
		//		String funName, String funDisplayName, String desType,
		//		int frequency, int repeatcount, 
		//		String mainframe,
		//		String repeatframe, 
		//		String toggleframe1, 
		//		String toggleframe2,
		//		String toggleframe3, 
		//		String toggleframe4, 
		//		String endframe)
		
		
		irData = new SparseArray<Command>();
		irData.put(
				R.id.buttonPower,
				new Command(0, 0, 0, '\0', 0, null, null, "Toggle", 38028, 1, null, null, "34 29 67 29 34 61 67 29 30 32 30 32 30 32 30 63 30 32 65 30 32 3234",null, null, null, null)
				);		
		irData.put(
				R.id.buttonMute,
				new Command(0, 0, 0, '\0', 0, null, null,"Toggle",38028,1,null, null, "34 29 34 29 67 61 67 29 30 32 30 32 30 32 30 63 30 32 65 63 32 3201",null, null, null, null)
				);
		irData.put(
				R.id.buttonVolDown,
				new Command(0, 0, 0, '\0', 0, null, null,"Toggle",38028,1,null, null, "34 29 34 29 67 61 63 32 30 32 30 32 30 63 65 30 32 30 32 63 32 3201",null, null, null, null)
				);
		irData.put(
				R.id.buttonVolUp,
				new Command(0, 0, 0, '\0', 0, null, null,"Toggle",38028,1,null, null, "34 29 67 29 34 61 63 32 30 32 30 32 30 63 65 30 32 30 32 30 32 3234",null, null, null, null)
				);
		irData.put(
				R.id.buttonChnP,
				new Command(0, 0, 0, '\0', 0, null, null,"Toggle",38028,1,null, null, "34 29 34 29 67 61 63 32 30 32 30 63 65 29 32 30 32 30 32 30 32 3236",null, null, null, null)
				);
		irData.put(
				R.id.buttonInput,
				new Command(0, 0, 0, '\0', 0, null, null,"Toggle",38028,1,null, null, "34 29 34 29 67 61 63 32 30 32 30 32 30 32 32 63 32 30 65 63 32 3201",null, null, null, null)
				);
		irData.put(
				R.id.buttonCD,
				new Command(0, 0, 0, '\0', 0, null, null,"Toggle",38028,1,null, null, "34 29 34 29 67 61 63 32 30 32 30 32 30 32 32 63 32 30 65 63 32 3201",null, null, null, null)
				);
		irData.put(
				R.id.buttonChnM,
				new Command(0, 0, 0, '\0', 0, null, null,"Toggle",38028,1,null, null, "34 29 67 29 34 61 63 32 30 32 30 63 65 30 32 30 32 30 32 63 32 3201",null, null, null, null)
				);

	
		ir = IRManager.getCurrentIRConssumer(this);
	}
	
	private void setControl(int idControl){
		List<Command> list=bd.getCommands(idControl);
		
		for(Command command: list){
			switch(command.funId){
			case 23: //Power
				irData.put(R.id.buttonPower, command);
				break;
			case 12: //Mute
				irData.put(R.id.buttonMute, command);
				break;
			case 14: //Vol-
				irData.put(R.id.buttonVolDown, command);
				break;
			case 13: //Vol+
				irData.put(R.id.buttonVolUp, command);
				break;
			case 16: //Chan-
				irData.put(R.id.buttonChnM, command);
				break;
			case 15: //Chan+
				irData.put(R.id.buttonChnP, command);
				break;
			case 25: //Input
				irData.put(R.id.buttonInput, command);
				break;
			case 217: //217|CD Input, 464|CD
				irData.put(R.id.buttonCD, command);
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
			Command data = irData.get(view.getId());
			if(CommonValues.DEBUG){
				Log.d("Ir", data.toString());			
			}else if(data != null) {
				try {
					ir.sendCommand(data);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			}
			
	}
}
