package com.rngtng.irdude;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.util.Log;

import com.rngtng.irdude.database.Command;



public class IrControl {
	private Object irdaService;
	private Method irWrite;
	
	public IrControl(Object irdaService) {
		if( MainActivity.DEBUG )
			return;
		this.irdaService=irdaService;
		Class c = irdaService.getClass();
		Class p[] = { String.class };
		try {
			irWrite = c.getMethod("write_irsend", p);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
	
	public class InvokeIr extends Thread{
		
		private Method irWrite;
		private String data;
		private Object irdaService;
		
		public InvokeIr(Method irWrite,Object irdaService,String data){
			this.irWrite=irWrite;
			this.data=data;
			this.irdaService=irdaService;
		}
		
	    public void run() {
	    	try {
				irWrite.invoke(irdaService, data);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}
	
	public void irSend(String data) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {		
		if( MainActivity.DEBUG){
			Log.d("IrControl", "Sending data: "+data);
			return;	
		}
		if(data == null){
			Log.e("IrControl", "Data NULL");
			return;
		}
		(new InvokeIr(irWrite,irdaService,data)).start();		
	}
	
	public static String db2data(Command command){
		return db2data(command.frequency,command.repeatcount,command.desType,command.mainframe,command.repeatframe,
				command.toggleframe1,command.toggleframe2,command.toggleframe3,command.toggleframe4,
				command.endframe);
	}
	
	public static String db2data(int frequency,int repeatCount,String desType,
			String mainframe,String repeatframe,
			String toggleframe1,String toggleframe2,
			String toggleframe3,String toggleframe4,
			String endframe){		
		
		/*
		 * Dont have in DB: toggleframe3, toggleframe4 and endframe ( always null )
		 * desType = "Toggle" //toggleframe1 optional, toggleframe2 optional and mainframe optional, have at least 1
		 * desType = "Full_Repeat" //mainframe have (if not ERROR), toggleframe1 and toggleframe2 optional (1 case ,apparently error, IGNORE), repeatframe optional
		 * desType = "Partial_Repeat" //mainframe have (if not ERROR), repeatframe optional
		 */
				
		StringBuilder localStringBuilder = new StringBuilder(String.valueOf(frequency));
		
		if( desType.equalsIgnoreCase("Toggle") ){
			String repeat=null;
			boolean frame1 = toggleframe1 != null && toggleframe1.length() > 0, frame2 = toggleframe2 != null && toggleframe2.length() > 0;
			if( frame1 || frame2 ){
				repeat="";
				if( frame1 )
					repeat+=","+toggleframe1;
				if( frame2 )
					repeat+=","+toggleframe2;
				for(int i=0;i<repeatCount;i++)
					localStringBuilder.append(","+repeat);
			}			
			if( mainframe != null && mainframe.length() > 0 )
				localStringBuilder.append(","+mainframe);
		}else if(desType.equalsIgnoreCase("Full_Repeat") || desType.equalsIgnoreCase("Partial_Repeat")){
			if( mainframe == null )
				return null;
			if( repeatframe != null && repeatframe.length() > 0 )
				for(int i=0;i<repeatCount;i++)
					localStringBuilder.append(","+repeatframe);
			
			localStringBuilder.append(","+mainframe);
		}else{
			return null;
		}
					
	    return localStringBuilder.toString().replace(' ', ',');
	}
}
