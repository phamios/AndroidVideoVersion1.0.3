package com.ypyproductions.myvideo.net;

import java.util.ArrayList;

import android.content.Context;

import com.dobao.utils.DBLog;
import com.ypyproductions.myvideo.constanst.IYoutubePlaylistConstants;
import com.ypyproductions.myvideo.object.PlaylistObject;
import com.ypyproductions.myvideo.object.VideoObject;

public class YoutubeNetUtils implements IYoutubePlaylistConstants {
	
	public static final String TAG = YoutubeNetUtils.class.getSimpleName();
	
	public static ArrayList<PlaylistObject> getListPlaylistObjects(Context mContext,String userId){
		try {
			String url = String.format(URL_LIST_PLAYLISTS, userId);
			DBLog.d(TAG, "==========>url="+url);
			String mResponce =DownloadUtils.downloadString(url);
			return JsonParsingUtils.parsingPlaylists(mResponce);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static ArrayList<VideoObject> getListVideoObjects(Context mContext,String playlistId,int startIndex){
		try {
			String url = String.format(URL_DETAIL_PLAYLIST, playlistId,String.valueOf(startIndex));
			DBLog.d(TAG, "==========>url="+url);
			String mResponce =DownloadUtils.downloadString(url);
			return JsonParsingUtils.parsingVideos(mResponce);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
