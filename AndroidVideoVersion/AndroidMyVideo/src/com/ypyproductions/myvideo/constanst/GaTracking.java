package com.ypyproductions.myvideo.constanst;
 
import android.content.Context;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder; 


public class GaTracking {
	
	private static EasyTracker easyTracker = null;
	
	public static void track_view(Context context,String action, String event_name){
		easyTracker = EasyTracker.getInstance(context);
		//EasyTracker.getInstance(context).activityStart((Activity) context);
		easyTracker.send(MapBuilder.createEvent(action,event_name, "view", null).build());
	}
}
