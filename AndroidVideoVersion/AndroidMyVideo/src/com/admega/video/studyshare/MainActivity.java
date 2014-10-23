package com.admega.video.studyshare;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
  
 













import java.util.Random;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

import com.admega.video.chautinhtri.ListPlaylistAdapter;
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
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.ypyproductions.myvideo.constanst.GaTracking;
import com.ypyproductions.myvideo.constanst.IYoutubePlaylistConstants;
import com.ypyproductions.myvideo.data.TotalDataManager;
import com.ypyproductions.myvideo.net.YoutubeNetUtils;
import com.ypyproductions.myvideo.object.PlaylistObject;

public class MainActivity extends DBFragmentActivity implements IYoutubePlaylistConstants {

	public static final String TAG = MainActivity.class.getSimpleName();

	private GridView mListView;
	private ArrayList<PlaylistObject> mListPlaylistObjects;
	private ListPlaylistAdapter mListPlaylistAdapter;

	private ImageFetcher mImgFetcher;

	private SearchView searchView;

	private DBSynchonizationThread mDBSynchonization;

	private AdView adView;

	private InterstitialAd mInterstitial;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ImageCacheParams cacheParams = new ImageCacheParams(this, getString(R.string.cache));
		float percent = 0.25f;
		cacheParams.setMemCacheSizePercent(percent);
		cacheParams.compressQuality = 100;

		mImgFetcher = new ImageFetcher(this, 480, 360);
		mImgFetcher.setLoadingImage(R.drawable.logo);
		mImgFetcher.addImageCache(getSupportFragmentManager(), cacheParams);

		this.mListView = (GridView) findViewById(R.id.list_playlist);
		
		GaTracking.track_view(this, "Home", "MyVideo");
		EasyTracker.getInstance(this).activityStart(this); 
	 
		displayAdmob();
		startInterstitial();
		mListPlaylistObjects = TotalDataManager.getInstance().getListPlaylistObjects();
		if (mListPlaylistObjects != null && mListPlaylistObjects.size() > 0) {
			renderListView();
		}
		else {
			this.startLoadPlaylist();
		}
		mDBSynchonization = new DBSynchonizationThread();
		handleIntent(getIntent());
		 
	}
	
	 
	
	private void displayAdmob(){ 
		AdView adViewCurrent = (AdView) this.findViewById(R.id.adView);   
		AdRequest adRequestCurrent = new AdRequest.Builder().build(); 
		adViewCurrent.loadAd(adRequestCurrent);
	}
	 
	
	public void startInterstitial() {
		mInterstitial = new InterstitialAd(MainActivity.this);
		
		mInterstitial.setAdUnitId(ADMOB_ID_INTERTESTIAL);
 
		AdRequest adRequest = new AdRequest.Builder().build();
 
		mInterstitial.loadAd(adRequest);
		mInterstitial.setAdListener(new AdListener() {
			public void onAdLoaded() { 
				displayInterstitial();
			}
		});
	}

	public void displayInterstitial() {
		// If Ads are loaded, show Interstitial else show nothing.
		if (mInterstitial.isLoaded()) {
			mInterstitial.show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		MenuItem menuItem = menu.findItem(R.id.action_search);
		searchView = (SearchView) menuItem.getActionView();
		searchView.setSubmitButtonEnabled(true);
		searchView.setQueryHint(Html.fromHtml("<font color = #ffffff>" + getResources().getString(R.string.title_search_playlist) + "</font>"));

		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		searchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				startSearchPlaylist(newText);
				return true;
			}
		});

		return true;
	}

	private void startSearchPlaylist(final String query) {
		if (mListPlaylistObjects != null && mListPlaylistObjects.size() > 0) {
			DBLog.d(TAG, "=============>startFind="+query);
			mDBSynchonization.queueAction(new IDBCallback() {
				@Override
				public void onAction() {
					if(!StringUtils.isStringEmpty(query)){
						final ArrayList<PlaylistObject> mNewPlaylist = new ArrayList<PlaylistObject>();
						for (PlaylistObject mPlaylistObject : mListPlaylistObjects) {
							if (mPlaylistObject.getTitle().toLowerCase(Locale.US).startsWith(query.toLowerCase(Locale.US))
									|| mPlaylistObject.getDescription().toLowerCase(Locale.US).contains(query.toLowerCase(Locale.US))) {
								mNewPlaylist.add(mPlaylistObject);
								
							}
						}
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								if(mListPlaylistAdapter!=null){
									mListPlaylistAdapter.setListPlaylistObjects(mNewPlaylist);
								}
							}
						});

					}
					else{
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								if(mListPlaylistAdapter!=null){
									mListPlaylistAdapter.setListPlaylistObjects(mListPlaylistObjects);
								}
							}
						});
					}
				}
			});

		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case R.id.action_reload:
			startLoadPlaylist();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void startLoadPlaylist() {
		if (!ApplicationUtils.isOnline(this)) {
			showDialogFragment(DIALOG_LOSE_CONNECTION);
			return;
		}
		mListView.setAdapter(null);
		if (mListPlaylistObjects != null) {
			mListPlaylistObjects.clear();
			mListPlaylistObjects = null;
		}
		mListPlaylistAdapter = null;

		DBTask mDbTask = new DBTask(new IDBTaskListener() {

			@Override
			public void onPreExcute() {
				showProgressDialog();
			}

			@Override
			public void onDoInBackground() {
				mListPlaylistObjects = YoutubeNetUtils.getListPlaylistObjects(MainActivity.this, YOUTUBE_ACCOUNT);
				TotalDataManager.getInstance().setListPlaylistObjects(mListPlaylistObjects);

			}

			@Override
			public void onPostExcute() {
				dimissProgressDialog();
				if (mListPlaylistObjects == null) {
					Toast.makeText(MainActivity.this, R.string.info_server_error, Toast.LENGTH_LONG).show();
					return;
				}
				renderListView();

			}

		});
		mDbTask.execute();
	}

	public void renderListView() {
		mListPlaylistAdapter = new ListPlaylistAdapter(MainActivity.this, mListPlaylistObjects, mImgFetcher);
		mListView.setAdapter(mListPlaylistAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
				if(mListPlaylistObjects.get(position).getNumberVideos()<=0){
					showToast(R.string.info_no_video);
					return;
				}
				Intent mIt = new Intent(MainActivity.this, ListPlaylistActivity.class);
				mIt.putExtra(ListPlaylistActivity.KEY_INDEX, position);
				DirectionUtils.changeActivity(MainActivity.this, R.anim.slide_in_from_right, R.anim.slide_out_to_left, true, mIt);
			}
		});
	}

	@Override
	public void onDestroyData() {
		super.onDestroyData();
		TotalDataManager.getInstance().onDestroy();
		if (mInterstitial!=null && mInterstitial.isLoaded()) {
			mInterstitial.show();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EasyTracker.getInstance(this).activityStop(this);
		if (adView != null) {
			adView.destroy();
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
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		handleIntent(intent);
	}

	private void handleIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			startSearchPlaylist(query);
		}
	}
}
