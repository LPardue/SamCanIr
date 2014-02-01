package com.rngtng.irdude.irs;

import com.rngtng.irdude.database.Command;

public interface IRConsumer {
	public void sendCommand(Command command);
	public void sendCommand(int frequency,int repeatCount,String desType,
			String mainframe,String repeatframe,
			String toggleframe1,String toggleframe2,
			String toggleframe3,String toggleframe4,
			String endframe);
}
