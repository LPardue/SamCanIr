package com.rngtng.irdude.irs;

import android.annotation.TargetApi;
import android.hardware.ConsumerIrManager;
import android.util.Log;

import com.rngtng.irdude.database.Command;

@TargetApi(19)
public class AndroidAPI19 implements IRConsumer {
	private ConsumerIrManager consumerIr;
	
	public AndroidAPI19(ConsumerIrManager consumerIr){
		this.consumerIr = consumerIr;
	}
	
	@Override
	public void sendCommand(Command command) {
		sendCommand(command.frequency,command.repeatcount,command.desType,command.mainframe,command.repeatframe,
				command.toggleframe1,command.toggleframe2,command.toggleframe3,command.toggleframe4,
				command.endframe);
	}
	
	public class InvokeIr extends Thread{
		
		private ConsumerIrManager consumerIr;
		private int freq;
		private int[] data;
		
		public InvokeIr(ConsumerIrManager consumerIr,int freq,int[] data){
			this.freq=freq;
			this.data=data;
			this.consumerIr=consumerIr;
		}
		
	    public void run() {
	    	consumerIr.transmit(freq, data);
	    }
	}
	
	@Override
	public void sendCommand(int frequency, int repeatCount, String desType,
			String mainframe, String repeatframe, String toggleframe1,
			String toggleframe2, String toggleframe3, String toggleframe4,
			String endframe) {
		if( consumerIr.hasIrEmitter() == false ){
			Log.e("ConsumerIr", "no consumer ir service");
			return;
		}
		
		int[] data=null;
		
		if( desType.equalsIgnoreCase("Toggle") ){//????? How it work??
			boolean frame1 = toggleframe1 != null && toggleframe1.length() > 0,
					frame2 = toggleframe2 != null && toggleframe2.length() > 0;
			if( frame1 ){
				String frames[] = toggleframe1.split("[ ,]");
				data = new int[frames.length];
				for(int i=0; i<frames.length; i++)
					data[i]=Integer.valueOf(frames[i]);
			}else if ( frame2 ){
				String frames[] = toggleframe2.split("[ ,]");
				data = new int[frames.length];
				for(int i=0; i<frames.length; i++)
					data[i]=Integer.valueOf(frames[i]);
			}
		}else if(desType.equalsIgnoreCase("Full_Repeat") || desType.equalsIgnoreCase("Partial_Repeat")){
			if( mainframe == null )
				return;	
			String mainframes[]=mainframe.split("[ ,]");
			if( repeatframe != null && repeatframe.length() > 0 ){
				String repeatframes[]=repeatframe.split("[ ,]");
				data = new int[mainframes.length + (repeatframes.length * repeatCount)];
				for(int i=0; i<mainframes.length; i++)
					data[i]=Integer.valueOf(mainframes[i]);
				for(int i=0;i<repeatCount;i++)
					for(int j=0; j<repeatframes.length; j++)
						data[j]=Integer.valueOf(repeatframes[j]);
			}
		}else{
			return;
		}
	
		if( data != null )
			(new InvokeIr(consumerIr, frequency, data)).run();
	}

}
