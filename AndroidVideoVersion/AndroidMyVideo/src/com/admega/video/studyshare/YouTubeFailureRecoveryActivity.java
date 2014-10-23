package com.admega.video.studyshare;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Toast;

import com.admega.video.chautinhtri.R;
import com.dobao.dialog.utils.AlertDialogUtils;
import com.dobao.dialog.utils.AlertDialogUtils.IOnDialogListener;
import com.dobao.net.task.IDBConstantURL;
import com.dobao.utils.ResolutionUtils;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.ypyproductions.myvideo.constanst.IYoutubePlaylistConstants;
 
public abstract class YouTubeFailureRecoveryActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, IYoutubePlaylistConstants, IDBConstantURL {

	private static final int RECOVERY_DIALOG_REQUEST = 1;
	private int screenWidth;
	private int screenHeight;
	private ProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.getWindow().setFormat(PixelFormat.RGBA_8888);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		this.createProgressDialog();

		int[] mRes = ResolutionUtils.getDeviceResolution(this, Configuration.ORIENTATION_PORTRAIT);
		if (mRes != null && mRes.length == 2) {
			screenWidth = mRes[0];
			screenHeight = mRes[1];
		}

	}
	
	public void showToast(int resId) {
		showToast(getString(resId));
	}

	public void showToast(String message) {
		Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	@Override
	public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
		if (errorReason.isUserRecoverableError()) {
			errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
		}
		else {
			String errorMessage = String.format(getString(R.string.error_player), errorReason.toString());
			Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == RECOVERY_DIALOG_REQUEST) {
			getYouTubePlayerProvider().initialize(YOUTUBE_SIGN_KEY, this);
		}
	}

	protected abstract YouTubePlayer.Provider getYouTubePlayerProvider();

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
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_LOSE_CONNECTION:
			createWarningDialog(DIALOG_LOSE_CONNECTION, R.string.title_warning, R.string.info_lose_internet);
			break;
		case DIALOG_EMPTY:
			createWarningDialog(DIALOG_EMPTY, R.string.title_warning, R.string.info_empty);
			break;
		case DIALOG_QUIT_APPLICATION:
			createQuitDialog();
			break;
		case DIALOG_SEVER_ERROR:
			createWarningDialog(DIALOG_SEVER_ERROR, R.string.title_warning, R.string.info_server_error);
			break;
		default:
			break;
		}
		return super.onCreateDialog(id);
	}
	
	public Dialog createWarningDialog(int idDialog, int titleId, int messageId) {
		return AlertDialogUtils.createInfoDialog(this, android.R.drawable.ic_dialog_alert, titleId, android.R.string.ok, messageId, null);
	}

	private Dialog createQuitDialog() {
		int mTitleId = R.string.title_confirm;
		int mYesId = R.string.title_yes;
		int mNoId = R.string.title_no;
		int iconId = R.drawable.ic_launcher;
		int messageId = R.string.quit_message;
		
		return AlertDialogUtils.createFullDialog(this,iconId, mTitleId, mYesId,mNoId, messageId, new IOnDialogListener() {
			@Override
			public void onClickButtonPositive() {
				
			}
			
			@Override
			public void onClickButtonNegative() {
				
			}
		});

	}

}
