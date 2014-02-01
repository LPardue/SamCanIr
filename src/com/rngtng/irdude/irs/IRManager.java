package com.rngtng.irdude.irs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.ConsumerIrManager;

public class IRManager {
	@SuppressLint("InlinedApi")
	public static IRConsumer getCurrentIRConssumer(Context context){
		Object irda = context.getSystemService("irda");
		if( irda != null ){
			return new SamsungIRBlaster(irda);
		}
		
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
			ConsumerIrManager consumerir = (ConsumerIrManager)context.getSystemService(Context.CONSUMER_IR_SERVICE);
			if( consumerir != null ){
				return new AndroidAPI19(consumerir);			
			}
		}
		
		return null;
	}
	
}
