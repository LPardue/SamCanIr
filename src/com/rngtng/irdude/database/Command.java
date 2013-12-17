package com.rngtng.irdude.database;

public class Command {

	public int id;
	public int idControl;
	public int UES;
	public char inputflag;
	public int funId;
	public String funName;
	public String funDisplayName;
	public String desType;
	public int frequency;
	public int repeatcount;
	public String mainframe;
	public String repeatframe;
	public String toggleframe1;
	public String toggleframe2;
	public String toggleframe3;
	public String toggleframe4;
	public String endframe;
	
	public Command(int id, int idControl, int uES, char inputflag, int funId,
			String funName, String funDisplayName, String desType,
			int frequency, int repeatcount, String mainframe,
			String repeatframe, String toggleframe1, String toggleframe2,
			String toggleframe3, String toggleframe4, String endframe) {
		super();
		this.id = id;
		this.idControl = idControl;
		UES = uES;
		this.inputflag = inputflag;
		this.funId = funId;
		this.funName = funName;
		this.funDisplayName = funDisplayName;
		this.desType = desType;
		this.frequency = frequency;
		this.repeatcount = repeatcount;
		this.mainframe = mainframe;
		this.repeatframe = repeatframe;
		this.toggleframe1 = toggleframe1;
		this.toggleframe2 = toggleframe2;
		this.toggleframe3 = toggleframe3;
		this.toggleframe4 = toggleframe4;
		this.endframe = endframe;
	}
	
	
	
}
