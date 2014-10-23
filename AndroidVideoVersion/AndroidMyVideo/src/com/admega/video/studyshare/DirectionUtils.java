package com.admega.video.studyshare;

import android.app.Activity;
import android.content.Intent;

 
public class DirectionUtils {
	
	public static void changeActivity(Activity mActivity, int animIn, int animOut,boolean hasFinish, Intent mIntent){
		if(mActivity==null||mIntent==null){
			return;
		}
		mActivity.startActivity(mIntent);
		mActivity.overridePendingTransition(animIn, animOut);
		if(hasFinish){
			mActivity.finish();
		}
	}
}
