package com.admega.video.studyshare;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.admega.video.chautinhtri.R;
import com.dobao.utils.ApplicationUtils;
import com.dobao.utils.DBLog;
import com.ypyproductions.myvideo.constanst.IYoutubePlaylistConstants;

 

public class SplashActivity extends DBFragmentActivity implements IYoutubePlaylistConstants {
	
	public static final String TAG=SplashActivity.class.getSimpleName();
	
	private ProgressBar mProgressBar;
	private boolean isPressBack;
	private Handler mHandler = new Handler();
	private boolean isLoading;

	private TextView mTvVersion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN );
	    
	    this.setContentView(R.layout.splash);
		this.mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
		this.mTvVersion = (TextView) findViewById(R.id.tv_version);
		this.mTvVersion.setText(String.format(getString(R.string.info_version_format), getVersionName(this)));
		
		DBLog.setDebug(DEBUG);

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(!ApplicationUtils.isOnline(this)){
			isPressBack=true;
			isLoading=false;
			showDialogFragment(DIALOG_LOSE_CONNECTION);
		}
		else{
			if(!isLoading){
				isLoading=true;
				mHandler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						mProgressBar.setVisibility(View.INVISIBLE);
						Intent mIntent = new Intent(SplashActivity.this, MainActivity.class);
						DirectionUtils.changeActivity(SplashActivity.this, R.anim.slide_in_from_right, R.anim.slide_out_to_left, true, mIntent);
					}
				}, 2000);
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mHandler.removeCallbacksAndMessages(null);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			if(isPressBack){
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private String getVersionName(Context mContext) {
		PackageInfo pinfo;
		try {
			pinfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
			String versionName = pinfo.versionName;
			return versionName;
		}
		catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

}
