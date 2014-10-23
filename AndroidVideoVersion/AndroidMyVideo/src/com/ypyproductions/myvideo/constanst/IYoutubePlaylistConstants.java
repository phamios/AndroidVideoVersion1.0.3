package com.ypyproductions.myvideo.constanst;

public interface IYoutubePlaylistConstants {
	
	public static final boolean DEBUG=true;
	
	public static final String YOUTUBE_SIGN_KEY= "AIzaSyDibL_fEnR-bz41x_PAwc11d8NbKNFOcfc";
	
	public static final String YOUTUBE_ACCOUNT="UCKJsZkZ3h2guTqs-d9EKGzA";
	
	public static final boolean SHOW_ADVERTISEMENT=true;
	
	public static final String ADMOB_ID_BANNER = "ca-app-pub-1214276490829950/4724504624"; 
	public static final String ADMOB_ID_INTERTESTIAL = "ca-app-pub-1214276490829950/1631437420"; 
	
	public static final String KEY_STATE_ID="state_id";
	public static final String KEY_CITY_ID="city_id";
	public static final String KEY_LOCATION_ID="location_id";
	public static final String KEY_NAME_FRAGMENT="name_fragment";
	public static final String KEY_ID_FRAGMENT="id_fragment";
	public static final String KEY_NAME_KEYWORD="keyword";
	public static final String KEY_EVENT_ID="event_id";
	public static final int MAX_RESULT=25;
	
	public static final String URL_LIST_PLAYLISTS="https://gdata.youtube.com/feeds/api/users/%1$s/playlists?v=2&alt=jsonc";
	public static final String URL_DETAIL_PLAYLIST="https://gdata.youtube.com/feeds/api/playlists/%1$s?v=2&alt=jsonc&max-results=25&start-index=%2$s";
	public static final String URL_VIDEO_YOUTUBE="http://youtu.be/%1$s";
	
	public static final String KEY_URL = "url";
	public static final String KEY_URL_YOUTUBE = "youtube_id";
	
}
