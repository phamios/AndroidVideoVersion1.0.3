package com.admega.video.studyshare;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import java.util.ArrayList;
import java.util.Locale;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.admega.video.chautinhtri.ListVideoAdapter;
import com.admega.video.chautinhtri.R;
import com.dobao.bitmap.ImageCache.ImageCacheParams;
import com.dobao.bitmap.ImageFetcher;
import com.dobao.net.task.DBTask;
import com.dobao.net.task.IDBCallback;
import com.dobao.net.task.IDBTaskListener;
import com.dobao.utils.ApplicationUtils;
import com.dobao.utils.DBLog;
import com.dobao.utils.DBSynchonizationThread;
import com.dobao.utils.StringUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;
import com.ypyproductions.myvideo.constanst.IYoutubePlaylistConstants;
import com.ypyproductions.myvideo.data.TotalDataManager;
import com.ypyproductions.myvideo.net.YoutubeNetUtils;
import com.ypyproductions.myvideo.object.PlaylistObject;
import com.ypyproductions.myvideo.object.VideoObject;

public class ListPlaylistActivity extends YouTubeFailureRecoveryActivity implements IYoutubePlaylistConstants
	,YouTubePlayer.OnFullscreenListener,OnScrollListener, OnClickListener{
	
	public static final String TAG = ListPlaylistActivity.class.getSimpleName();
	public static final String KEY_INDEX = "index";
	
	private ListView mListView;
	private ArrayList<VideoObject> mListVideoObjects;
	private ListVideoAdapter mListVideoAdapter;

	private ImageFetcher mImgFetcher;
	private int mIndexPlaylist=-1;
	
	private TextView mTvTitle;
	private TextView mTvNumberVideo;
	private DBSynchonizationThread mDBSynchonization;
	private TextView mTvNumberVideo1;
	private YouTubePlayerView mYouTubePlayerView;
	private YouTubePlayer mYouTubePlayer;
	private boolean isFullscreen;
	private String mIdYoutube="";
	private PlaylistObject mPlaylistObject;
	private RelativeLayout mFooterView;
	
	private boolean isAllowAddPage;
	private boolean isStartAddingPage;
	private DBTask mDbTask;
	private int mCurrentPost;
	private TextView mTvCopyRight;
	private AdView adView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFormat(PixelFormat.RGBA_8888);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_deltail_playlist);
		
		ImageCacheParams cacheParams = new ImageCacheParams(this, getString(R.string.cache));
		float percent = 0.25f;
		cacheParams.setMemCacheSizePercent(percent);
		cacheParams.compressQuality = 100;

		mImgFetcher = new ImageFetcher(this, 480, 360);
		mImgFetcher.setLoadingImage(R.drawable.logo);
		mImgFetcher.addImageCache(getFragmentManager(), cacheParams);
		
		this.mListView =(ListView) findViewById(R.id.list_video);
		this.mTvTitle =(TextView) findViewById(R.id.tv_title);
		this.mTvNumberVideo =(TextView) findViewById(R.id.tv_numbervideo);
		this.mTvNumberVideo1 =(TextView) findViewById(R.id.tv_numbervideo1);
		this.mTvCopyRight =(TextView) findViewById(R.id.tv_copyright);
		this.mTvNumberVideo.setText("0/0");
		this.mFooterView =(RelativeLayout)findViewById(R.id.layout_footer);
		
		this.mTvCopyRight .setText(YOUTUBE_ACCOUNT);
		
		setUpLayoutAdmob();
		mYouTubePlayerView =(YouTubePlayerView) findViewById(R.id.player);
		Intent mIntent  =getIntent();
		if(mIntent!=null){
			mIndexPlaylist =mIntent.getIntExtra(KEY_INDEX, -1); 
		}
		if(mIndexPlaylist>=0){
			setUpInfo();
		}
		else{
			finish();
			return;
		}
		mYouTubePlayerView.initialize(YOUTUBE_SIGN_KEY, this);
		doLayout();
		
		mDBSynchonization = new DBSynchonizationThread();
		
	}
	
	private void setUpLayoutAdmob() {
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout_ad);
		if(SHOW_ADVERTISEMENT){
			adView = new AdView(this);
			adView.setAdUnitId(ADMOB_ID_BANNER);
			adView.setAdSize(AdSize.BANNER);

			layout.addView(adView);
			AdRequest mAdRequest = new AdRequest.Builder().build();
			adView.loadAd(mAdRequest);
			
		}
		else{
			RelativeLayout.LayoutParams mLayoutParams = new RelativeLayout.LayoutParams(android.widget.RelativeLayout.LayoutParams.MATCH_PARENT, android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
			mLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			mFooterView.setLayoutParams(mLayoutParams);
			layout.setVisibility(View.GONE);
		}
	}
	
	private void doLayout() {
		RelativeLayout.LayoutParams playerParams = (RelativeLayout.LayoutParams) mYouTubePlayerView.getLayoutParams();
		if (isFullscreen) {
			playerParams.width = LayoutParams.MATCH_PARENT;
			playerParams.height = LayoutParams.MATCH_PARENT;
		} 
		else {
			if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				playerParams.width =MATCH_PARENT;
				playerParams.height = MATCH_PARENT;
			}
			else {
				playerParams.width =MATCH_PARENT;
				playerParams.height = WRAP_CONTENT;
			}
		}
	}
	
	private void setUpInfo(){
		final PlaylistObject mPlaylistObject = TotalDataManager.getInstance().getListPlaylistObjects().get(mIndexPlaylist);
		if(mPlaylistObject!=null){
			mTvTitle.setText(mPlaylistObject.getTitle());
			int numberVideo = mPlaylistObject.getNumberVideos();
			String video = "";
			if (numberVideo <= 1) {
				video = String.format(getString(R.string.format_video), String.valueOf(numberVideo));
			}
			else {
				video = String.format(getString(R.string.format_videos), String.valueOf(numberVideo));
			}
			mTvNumberVideo1.setText(video);
			mTvNumberVideo.setText("1/"+String.valueOf(numberVideo));
		}
	}

	
	protected void startSearchVideo(final String query) {
		if (mListVideoObjects != null && mListVideoObjects.size() > 0) {
			DBLog.d(TAG, "=============>startFind="+query);
			mDBSynchonization.queueAction(new IDBCallback() {
				@Override
				public void onAction() {
					if(!StringUtils.isStringEmpty(query)){
						final ArrayList<VideoObject> mNewVideos = new ArrayList<VideoObject>();
						for (VideoObject mVideoObject : mListVideoObjects) {
							if (mVideoObject.getTitle().toLowerCase(Locale.US).startsWith(query.toLowerCase(Locale.US))
									|| mVideoObject.getDescription().toLowerCase(Locale.US).contains(query.toLowerCase(Locale.US))) {
								mNewVideos.add(mVideoObject);
								
							}
						}
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								if(mListVideoAdapter!=null){
									mListVideoAdapter.setListVideoObjects(mNewVideos);
								}
							}
						});

					}
					else{
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								if(mListVideoAdapter!=null){
									mListVideoAdapter.setListVideoObjects(mListVideoObjects);
								}
							}
						});
					}
				}
			});

		}
	}
	
	private void startLoadVideo(){
		if(!ApplicationUtils.isOnline(this)){
			showDialog(DIALOG_LOSE_CONNECTION);
			return;
		}
		mListView.setAdapter(null);
		if(mListVideoObjects!=null){
			mListVideoObjects.clear();
			mListVideoObjects=null;
		}
		mListVideoAdapter=null;
		
		this.mTvNumberVideo.setText("");
		this.mTvTitle.setText("");
		
		mPlaylistObject = TotalDataManager.getInstance().getListPlaylistObjects().get(mIndexPlaylist);
		
		DBTask  mDbTask = new DBTask(new IDBTaskListener() {
			
			@Override
			public void onPreExcute() {
				showProgressDialog();
			}
			
			@Override
			public void onDoInBackground() {
				mListVideoObjects = YoutubeNetUtils.getListVideoObjects(ListPlaylistActivity.this, mPlaylistObject.getId(),1);
				
			}
			@Override
			public void onPostExcute() {
				dimissProgressDialog();
				if(mListVideoObjects==null){
					showToast(R.string.info_server_error);
					return;
				}
				mListVideoAdapter = new ListVideoAdapter(ListPlaylistActivity.this, mListVideoObjects, mImgFetcher);
				mListView.setAdapter(mListVideoAdapter);
				mListView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
						playVideo(mListVideoObjects.get(position));
					}
				});
				mTvTitle.setText(mPlaylistObject.getTitle());
				if(mListVideoObjects.size()<MAX_RESULT){
					isAllowAddPage=false;
					mListView.setOnScrollListener(null);
				}
				else{
					isAllowAddPage=true;
					mListView.setOnScrollListener(ListPlaylistActivity.this);
				}
				if(mListVideoObjects.size()>0){
					playVideo(mListVideoObjects.get(0));
				}
			}
			
		});
		mDbTask.execute();
	}
	
	private void playVideo(VideoObject mVideoObject){
		if(mYouTubePlayer!=null){
			mIdYoutube=mVideoObject.getVideoId();
			mCurrentPost=mListVideoObjects.indexOf(mVideoObject);
			mYouTubePlayer.loadVideo(mIdYoutube);
			mTvNumberVideo.setText(String.valueOf(mCurrentPost+1)+"/"+String.valueOf(mPlaylistObject.getNumberVideos()));
			for(VideoObject mVideoObject2:mListVideoObjects){
				mVideoObject2.setSelected(false);
			}
			mVideoObject.setSelected(true);
			if(mListVideoAdapter!=null){
				mListVideoAdapter.notifyDataSetChanged();
			}
			mListView.post(new Runnable() {
				@Override
				public void run() {
					mListView.setSelection(mCurrentPost);
				}
			});
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (adView != null) {
			adView.destroy();
		}
		if(mDbTask!=null){
			mDbTask.cancel(true);
			mDbTask=null;
		}
		if(mListVideoObjects!=null){
			mListVideoObjects.clear();
			mListVideoObjects=null;
		}
		if(mDBSynchonization!=null){
			mDBSynchonization.onDestroy();
			mDBSynchonization=null;
		}
		if (mImgFetcher != null) {
			mImgFetcher.setExitTasksEarly(true);
			mImgFetcher.closeCache();
			mImgFetcher = null;
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			Intent mIntent = new Intent(this, MainActivity.class);
			DirectionUtils.changeActivity(ListPlaylistActivity.this, R.anim.slide_in_from_left, R.anim.slide_out_to_right, true, mIntent);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	

	@Override
	public void onInitializationSuccess(Provider arg0, YouTubePlayer mPlayer, boolean wasRestored) {
		this.mYouTubePlayer = mPlayer;
		mPlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
		mPlayer.setOnFullscreenListener(this);
		startLoadVideo();
	}

	@Override
	protected Provider getYouTubePlayerProvider() {
		return mYouTubePlayerView;
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		doLayout();
	}

	@Override
	public void onFullscreen(boolean isFullscreen) {
		this.isFullscreen=isFullscreen;
		DBLog.d(TAG, "==============>isFullscreen="+isFullscreen);
	    doLayout();
	}
	
	private void showFooterView() {
		if(mFooterView.getVisibility()!=View.VISIBLE){
			this.mFooterView.setVisibility(View.VISIBLE);
		}
	}
	
	private void hideFooterView(){
		if(mFooterView.getVisibility()==View.VISIBLE){
			this.mFooterView.setVisibility(View.GONE);
		}
	}
	private void onLoadNextVideo() {
		mDbTask = new DBTask(new IDBTaskListener() {

			private ArrayList<VideoObject> mLisNewtVideoObjects;

			@Override
			public void onPreExcute() {
				
			}

			@Override
			public void onDoInBackground() {
				mLisNewtVideoObjects = YoutubeNetUtils.getListVideoObjects(ListPlaylistActivity.this, mPlaylistObject.getId(),mListVideoObjects.size()+1);
			}

			@Override
			public void onPostExcute() {
				hideFooterView();
				isStartAddingPage=false;
				if(mLisNewtVideoObjects==null){
					showToast(R.string.info_server_error);
					return;
				}
				if(mLisNewtVideoObjects.size()==0){
					isAllowAddPage=false;
					mListView.setOnScrollListener(null);
					return;
				}
				else{
					if(mLisNewtVideoObjects.size()<MAX_RESULT){
						isAllowAddPage=false;
						mListView.setOnScrollListener(null);
					}
					else{
						isAllowAddPage=true;
					}
					synchronized (mListVideoObjects) {
						for(VideoObject mVideoObject:mLisNewtVideoObjects){
							mListVideoObjects.add(mVideoObject);
						}
						if(mListVideoAdapter!=null){
							mListVideoAdapter.notifyDataSetChanged();
						}
					}
				}
			}

		});
		mDbTask.execute();
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if (mListVideoAdapter != null) {
			if (isAllowAddPage) {
				int size = mListVideoAdapter.getCount();
				if (mListView.getLastVisiblePosition() == size - 1) {
					if (ApplicationUtils.isOnline(this)) {
						showFooterView();
						if (!isStartAddingPage) {
							isStartAddingPage = true;
							onLoadNextVideo();
						}
					}
				}
				else{
					if(!isStartAddingPage){
						hideFooterView();
					}
				}
			}
			else {
				hideFooterView();
			}
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_next:
			onNextVideo();
			break;
		case R.id.btn_prev:
			onPrevVideo();
			break;
		default:
			break;
		}
	}
	private void onNextVideo(){
		if(mListVideoObjects!=null && mListVideoObjects.size()>0){
			mCurrentPost+=1;
			if(mCurrentPost>=mListVideoObjects.size()){
				mCurrentPost=mListVideoObjects.size()-1;
			}
			playVideo(mListVideoObjects.get(mCurrentPost));
		}
	}
	private void onPrevVideo(){
		if(mListVideoObjects!=null && mListVideoObjects.size()>0){
			mCurrentPost-=1;
			if(mCurrentPost<=0){
				mCurrentPost=0;
			}
			playVideo(mListVideoObjects.get(mCurrentPost));
		}
	}
}
