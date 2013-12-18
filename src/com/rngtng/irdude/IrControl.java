package com.rngtng.irdude;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	
	public void irSend(String data) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {		
		if( MainActivity.DEBUG )
			return;
		irWrite.invoke(irdaService, data);
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
	}
	
	public static String hex2dec(String irData) {
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
