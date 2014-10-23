package com.admega.video.studyshare;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Toast;

import com.admega.video.chautinhtri.R;
import com.dobao.dialog.utils.IDialogFragmentListener;
import com.dobao.net.task.IDBConstantURL;
import com.dobao.utils.ResolutionUtils;

 
public class DBFragmentActivity extends FragmentActivity implements IDBConstantURL, IDialogFragmentListener {
	
	public static final String TAG = DBFragmentActivity.class.getSimpleName();
	private ProgressDialog mProgressDialog;

	private int screenWidth;
	private int screenHeight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setFormat(PixelFormat.RGBA_8888);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		this.createProgressDialog();
		
		int[] mRes=ResolutionUtils.getDeviceResolution(this);
		if(mRes!=null && mRes.length==2){
			screenWidth=mRes[0];
			screenHeight=mRes[1];
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showDialogFragment(DIALOG_QUIT_APPLICATION);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void showDialogFragment(int idDialog) {
		FragmentManager mFragmentManager = getSupportFragmentManager();
		switch (idDialog) {
		case DIALOG_LOSE_CONNECTION:
			createWarningDialog(DIALOG_LOSE_CONNECTION, R.string.title_warning, R.string.info_lose_internet).show(mFragmentManager, "DIALOG_LOSE_CONNECTION");
			break;
		case DIALOG_EMPTY:
			createWarningDialog(DIALOG_EMPTY, R.string.title_warning, R.string.info_empty).show(mFragmentManager, "DIALOG_EMPTY");
			break;
		case DIALOG_QUIT_APPLICATION:
			createQuitDialog().show(mFragmentManager, "DIALOG_QUIT_APPLICATION");
			break;
		case DIALOG_SEVER_ERROR:
			createWarningDialog(DIALOG_SEVER_ERROR, R.string.title_warning, R.string.info_server_error).show(mFragmentManager, "DIALOG_SEVER_ERROR");
			break;
		default:
			break;
		}
	}

	public DialogFragment createWarningDialog(int idDialog, int titleId, int messageId) {
		DBAlertFragment mDAlertFragment = DBAlertFragment.newInstance(idDialog, android.R.drawable.ic_dialog_alert, titleId, android.R.string.ok, messageId);
		return mDAlertFragment;
	}

	private DialogFragment createQuitDialog() {
		int mTitleId = R.string.title_confirm;
		int mYesId = R.string.title_yes;
		int mNoId = R.string.title_no;
		int iconId = R.drawable.ic_launcher;
		int messageId = R.string.quit_message;

		DBAlertFragment mDAlertFragment = DBAlertFragment.newInstance(DIALOG_QUIT_APPLICATION, iconId, mTitleId, mYesId, mNoId, messageId);
		return mDAlertFragment;

	}

	private void createProgressDialog() {
		this.mProgressDialog = new ProgressDialog(this);
		this.mProgressDialog.setIndeterminate(true);
		this.mProgressDialog.setCancelable(false);
		this.mProgressDialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					return true;
				}
				return false;
			}
		});
	}
	
	public void showToast(int resId) {
		showToast(getString(resId));
	}

	public void showToast(String message) {
		Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	public void showToastWithLongTime(int resId) {
		showToastWithLongTime(getString(resId));
	}
	
	public void showToastWithLongTime(String message) {
		Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	public void showProgressDialog() {
		if (mProgressDialog != null) {
			mProgressDialog.setMessage(this.getString(R.string.loading));
			mProgressDialog.show();
		}
	}

	public void dimissProgressDialog() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	@Override
	public void doPositiveClick(int idDialog) {
		switch (idDialog) {
		case DIALOG_QUIT_APPLICATION:
			onDestroyData();
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void doNegativeClick(int idDialog) {

	}
	
	public void onDestroyData(){
		
	}

}
