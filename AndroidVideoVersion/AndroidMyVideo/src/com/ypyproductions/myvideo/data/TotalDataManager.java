package com.ypyproductions.myvideo.data;

import java.util.ArrayList;

import com.ypyproductions.myvideo.object.PlaylistObject;
 
public class TotalDataManager {
	
	public static final String TAG = TotalDataManager.class.getSimpleName();
	
	private static TotalDataManager totalDataManager;
	private ArrayList<PlaylistObject> listPlaylistObjects;
	
	
	public static TotalDataManager getInstance(){
		if(totalDataManager==null){
			totalDataManager = new TotalDataManager();
		}
		return totalDataManager;
	}

	private TotalDataManager() {
		
	}

	public void onDestroy(){
		if(listPlaylistObjects!=null){
			listPlaylistObjects.clear();
			listPlaylistObjects=null;
		}
		totalDataManager=null;
	}

	public ArrayList<PlaylistObject> getListPlaylistObjects() {
		return listPlaylistObjects;
	}

	public void setListPlaylistObjects(ArrayList<PlaylistObject> listPlaylistObjects) {
		this.listPlaylistObjects = listPlaylistObjects;
	}
	
	
	
	
	
	
}
