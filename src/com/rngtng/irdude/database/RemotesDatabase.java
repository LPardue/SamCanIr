package com.rngtng.irdude.database;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

public class RemotesDatabase {
	private DatabaseHelper db;
	private final String brandTable="brand";
	private final String brandColumnId="_id";
	private final String brandColumnIdOri="oriId";
	private final String brandColumnName="name";
	private final String[] brandColumns={brandColumnId,brandColumnIdOri,brandColumnName};

	private final String controlTable="control";
	private final String controlColumnId="_id";
	private final String controlColumnIdOri="oriId";
	private final String controlColumnIdBrand="idBrand";
	private final String controlColumnName="name";
	private final String[] controlColumns={controlColumnId,controlColumnIdOri,controlColumnIdBrand,controlColumnName};
	
	private final String commandTable="command";
	private final String commandColumnId="_id";
	private final String commandColumnIdControl="idControl";
	private final String commandColumnUES="UES";
	private final String commandColumnInputFlag="inputflag";
	private final String commandColumnFunId="funId";
	private final String commandColumnFunName="funName";
	private final String commandColumnFunDisplayName="funDisplayName";
	private final String commandColumnDesType="desType";
	private final String commandColumnFrequency="frequency";
	private final String commandColumnRepeatcount="repeatcount";
	private final String commandColumnMainframe = "mainframe";
	private final String commandColumnRepeatframe = "repeatframe";
	private final String commandColumnToggleframe1 = "toggleframe1";
	private final String commandColumnToggleframe2="toggleframe2";
	private final String commandColumnToggleframe3="toggleframe3";
	private final String commandColumnToggleframe4="toggleframe4";
	private final String commandColumnEndframe="endframe";
	private final String[] commandColumns={
			commandColumnId,commandColumnIdControl,commandColumnUES,
			commandColumnInputFlag,commandColumnFunId,commandColumnFunName,
			commandColumnFunDisplayName,commandColumnDesType,commandColumnFrequency,
			commandColumnRepeatcount,commandColumnMainframe,commandColumnRepeatframe,
			commandColumnToggleframe1,commandColumnToggleframe2,commandColumnToggleframe3,
			commandColumnToggleframe4,commandColumnEndframe};
	
	public RemotesDatabase(Context context){
		db=new DatabaseHelper(context);
		try {
			db.createDataBase();
		} catch (IOException e) {
			Log.e("DB", "Erro ao copiar");
		}
		db.openDataBase();
	}
	
	public List<Brand> getBrands(int idCategory){
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(brandTable);
        
        LinkedList<Brand> ret=new LinkedList<Brand>();
        
        final String where="exists(select * from categoryBrand where _idCategory=? and _idBrand=_id)";
		String[] selectionArgs = new String[] {String.valueOf(idCategory)};
        Cursor cursor = builder.query(db.getReadableDatabase(),
        		brandColumns, where , selectionArgs, null, null, brandColumnName+" ASC");
        
        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        
        Brand add;
        
        int idIndex=cursor. getColumnIndex(brandColumnId);
        int idOriIndex=cursor.getColumnIndex(brandColumnIdOri);
        int nameIndex=cursor.getColumnIndex(brandColumnName);
        
        int idValue;
        int idOriValue;
        String nameValue;
        
		do{
			idValue=cursor.getInt(idIndex);
			idOriValue=cursor.getInt(idOriIndex);
			nameValue=cursor.getString(nameIndex);
			add=new Brand(idValue, idOriValue, nameValue);
			ret.add(add);
		}while(cursor.moveToNext());
		cursor.close();
        return ret;
	}
	
	
	public List<Control> getControls(int idCategory, int idBrand){
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(controlTable);
        
        LinkedList<Control> ret=new LinkedList<Control>();        
        final String where="idBrand = ? and exists(select * from categoryControl where _idCategory = ? and _idControl = _id)";
		String[] selectionArgs = new String[] {String.valueOf(idBrand),String.valueOf(idCategory)};
        Cursor cursor = builder.query(db.getReadableDatabase(),
        		controlColumns, where , selectionArgs, null, null, null);
        
        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        
        Control add;
        
        int idIndex=cursor. getColumnIndex(controlColumnId);
        int idOriIndex=cursor.getColumnIndex(controlColumnIdOri);
        int nameIndex=cursor.getColumnIndex(controlColumnName);
        
        int idValue;
        int idOriValue;
        String nameValue;
        
		do{
			idValue=cursor.getInt(idIndex);
			idOriValue=cursor.getInt(idOriIndex);
			nameValue=cursor.getString(nameIndex);
			add=new Control(idValue, idOriValue, idBrand,nameValue);
			ret.add(add);
		}while(cursor.moveToNext());
		cursor.close();
        return ret;
	}
	
	public List<Command> getControl(int idControl){
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(commandTable);
        
        final String where="idControl = ?";
		String[] selectionArgs = new String[] {String.valueOf(idControl)};
        Cursor cursor = builder.query(db.getReadableDatabase(),
        		commandColumns, where , selectionArgs, null, null, null);
        
        List<Command> ret=new LinkedList<Command>();
        
        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        
        Command add;
        
        int idIndex=cursor.getColumnIndex(commandColumnId);
        int idControlIndex=cursor.getColumnIndex(commandColumnIdControl);
        int uesIndex=cursor.getColumnIndex(commandColumnUES);
        int inputFlagIndex=cursor.getColumnIndex(commandColumnInputFlag);
        int funIdIndex=cursor.getColumnIndex(commandColumnFunId);
        int funNameIndex=cursor.getColumnIndex(commandColumnFunName);
        int funDisplayNameIndex=cursor.getColumnIndex(commandColumnFunDisplayName);
        int desTypeIndex=cursor.getColumnIndex(commandColumnDesType);
        int frequencyIndex=cursor.getColumnIndex(commandColumnFrequency);
        int repeatcountIndex=cursor.getColumnIndex(commandColumnRepeatcount);
        int mainframeIndex=cursor.getColumnIndex(commandColumnMainframe);
        int repeatframeIndex=cursor.getColumnIndex(commandColumnRepeatframe);
        int toggleframe1Index=cursor.getColumnIndex(commandColumnToggleframe1);
        int toggleframe2Index=cursor.getColumnIndex(commandColumnToggleframe2);
        int toggleframe3Index=cursor.getColumnIndex(commandColumnToggleframe3);
        int toggleframe4Index=cursor.getColumnIndex(commandColumnToggleframe4);
        int endframeIndex=cursor.getColumnIndex(commandColumnEndframe);
        
        int idValue;
        int idControlValue;
        int uesValue;
        char inputFlagValue;
        int funIdValue;
        String funNameValue;
        String funDisplayNameValue;
        String desTypeValue;
        int frequencyValue;
        int repeatcountValue;
        String mainframeValue;
        String repeatframeValue;
        String toggleframe1Value;
        String toggleframe2Value;
        String toggleframe3Value;
        String toggleframe4Value;
        String endframeValue;        
        		
        do{
	        idValue=cursor.getInt(idIndex);
			idControlValue=cursor.getInt(idControlIndex);
			uesValue=cursor.getInt(uesIndex);
			inputFlagValue=cursor.getString(inputFlagIndex).charAt(0);
			funIdValue=cursor.getInt(funIdIndex);
			funNameValue=cursor.getString(funNameIndex);
			funDisplayNameValue=cursor.getString(funDisplayNameIndex);
			desTypeValue=cursor.getString(desTypeIndex);
			frequencyValue=cursor.getInt(frequencyIndex);
			repeatcountValue=cursor.getInt(repeatcountIndex);
			mainframeValue=cursor.getString(mainframeIndex);
			repeatframeValue=cursor.getString(repeatframeIndex);
			toggleframe1Value=cursor.getString(toggleframe1Index);
			toggleframe2Value=cursor.getString(toggleframe2Index);
			toggleframe3Value=cursor.getString(toggleframe3Index);
			toggleframe4Value=cursor.getString(toggleframe4Index);
			endframeValue=cursor.getString(endframeIndex);
			add= new Command(idValue, idControlValue, uesValue, inputFlagValue, 
					funIdValue, funNameValue, funDisplayNameValue, desTypeValue, 
					frequencyValue, repeatcountValue, mainframeValue, repeatframeValue,
					toggleframe1Value, toggleframe2Value, toggleframe3Value,
					toggleframe4Value, endframeValue);
			ret.add(add);
        }while(cursor.moveToNext());
		return ret;
	}
	
	/*
	public void getCommands(int control) throws SQLException,IOException{
		
		db.openDataBase();
		
		String[] columns=new String[]{"frequency","repeatCount","desType","mainframe",
	 		"repeatframe","toggleframe1","toggleframe2","toggleframe3","toggleframe4","endframe"};
	 	
	 	String where="funName = ? and (select typeControl from control where _id=idControle) = ";
	 	
	 	//Cursor cursor = db.getReadableDatabase().query("command", columns, where, null, null, null, null);
	 	
	 	String[] selectionArgs=new String[]{"Power"};
	 	
	 	
	 	Cursor cursor = db.getReadableDatabase().query("command", columns, where, selectionArgs, null, null, null);
	 	
	 	if (cursor != null && cursor.moveToFirst()) {
	 		int frequency=cursor.getColumnIndex("frequency");
	 		int repeatcount=cursor.getColumnIndex("repeatcount");
	 		int desType=cursor.getColumnIndex("desType");
	 		int mainframe=cursor.getColumnIndex("mainframe");
	 		int repeatframe=cursor.getColumnIndex("repeatframe");
	 		int toggleframe1=cursor.getColumnIndex("toggleframe1");
	 		int toggleframe2=cursor.getColumnIndex("toggleframe2");
	 		int toggleframe3=cursor.getColumnIndex("toggleframe3");
	 		int toggleframe4=cursor.getColumnIndex("toggleframe4");
	 		int endframe=cursor.getColumnIndex("endframe");
	 		int number=cursor.getCount();
	 		int count=0;
	 		do{
	 			((ProgressBar)findViewById(R.id.progressBar1)).setProgress((count/number)*10000);
	 			try {
					irWrite.invoke(irdaService, db2data(
							cursor.getInt( frequency )
							,cursor.getInt( repeatcount )
							,cursor.getString( desType )
							,cursor.getString( mainframe )
							,cursor.getString( repeatframe )
							,cursor.getString( toggleframe1 )
							,cursor.getString( toggleframe2 )
							,cursor.getString( toggleframe3 )
							,cursor.getString( toggleframe4 )
							,cursor.getString( endframe )
							));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					cursor.close();
					return;
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					cursor.close();
					return;
				} catch (InvocationTargetException e) {
					e.printStackTrace();
					cursor.close();
					return;
				}
	 			count++;	 			
	 		}while(cursor.moveToNext());
        }
 
		if (cursor != null)
			cursor.close();
	}
	*/

}
