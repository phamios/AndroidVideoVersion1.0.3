package com.ypyproductions.myvideo.net;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dobao.utils.DBLog;
import com.dobao.utils.StringUtils;
import com.ypyproductions.myvideo.constanst.IYoutubePlaylistConstants;
import com.ypyproductions.myvideo.object.PlaylistObject;
import com.ypyproductions.myvideo.object.VideoObject;

public class JsonParsingUtils implements IYoutubePlaylistConstants {

	public static final String TAG = JsonParsingUtils.class.getSimpleName();

	public static ArrayList<PlaylistObject> parsingPlaylists(String data) {
		if(!StringUtils.isStringEmpty(data)){
			try {
				JSONObject mJsonObject = new JSONObject(data);
				JSONObject mJsDataObject = mJsonObject.getJSONObject("data");
				if(mJsDataObject.opt("items")!=null){
					JSONArray mJsonArray  = mJsDataObject.getJSONArray("items");
					int size = mJsonArray.length();
					if(size>0){
						ArrayList<PlaylistObject> mListPlaylistObjects = new ArrayList<PlaylistObject>();
						for(int i=0;i<size;i++){
							JSONObject mJsItem = mJsonArray.getJSONObject(i);
							String id = mJsItem.getString("id");
							String updated= mJsItem.getString("updated");
							String author =mJsItem.getString("author");
							String title = mJsItem.getString("title");
							String description = mJsItem.getString("description");
							int sizeVideo = mJsItem.getInt("size");
							
							String linkThumb="";
							if(mJsItem.opt("thumbnail")!=null){
								JSONObject mJsThumbObject = mJsItem.getJSONObject("thumbnail");
								linkThumb = mJsThumbObject.getString("hqDefault");
							}
							PlaylistObject mPlaylistObject = new PlaylistObject(id, title, author, updated, sizeVideo, description, linkThumb);
							mListPlaylistObjects.add(mPlaylistObject);
						}
						DBLog.d(TAG, "===============>parsingPlaylists="+mListPlaylistObjects.size());
						return mListPlaylistObjects;
					}
				}
			}
			catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	public static ArrayList<VideoObject> parsingVideos(String data) {
		if(!StringUtils.isStringEmpty(data)){
			try {
				JSONObject mJsonObject = new JSONObject(data);
				JSONObject mJsDataObject = mJsonObject.getJSONObject("data");
				if(mJsDataObject.opt("items")!=null){
					JSONArray mJsonArray  = mJsDataObject.getJSONArray("items");
					int size = mJsonArray.length();
					if(size>0){
						ArrayList<VideoObject> mListVideoObjects = new ArrayList<VideoObject>();
						for(int i=0;i<size;i++){
							JSONObject mJsItem = mJsonArray.getJSONObject(i);
							String id = mJsItem.getString("id");
							
							JSONObject mJsVideoObject = mJsItem.getJSONObject("video");
							String videoId = mJsVideoObject.getString("id");
							String updated= mJsVideoObject.getString("updated");
							String title = mJsVideoObject.getString("title");
							String description = mJsVideoObject.getString("description");
							
							JSONObject mJsThumbObject = mJsVideoObject.getJSONObject("thumbnail");
							String linkThumb = mJsThumbObject.getString("hqDefault");
							
							JSONObject mJsPlayerObject = mJsVideoObject.getJSONObject("player");
							String linkVideo = mJsPlayerObject.getString("default");
							
							int duration = mJsVideoObject.getInt("duration");
							
							float rating=0;
							if(mJsVideoObject.opt("rating")!=null){
								rating = (float) mJsVideoObject.getDouble("rating");
							}
							
							VideoObject mVideoObject = new VideoObject(id, videoId, updated, title, description, duration, rating, linkThumb, linkVideo);
							mListVideoObjects.add(mVideoObject);
							
						}
						DBLog.d(TAG, "===============>parsingVideos="+mListVideoObjects.size());
						return mListVideoObjects;
					}
				}
			}
			catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
