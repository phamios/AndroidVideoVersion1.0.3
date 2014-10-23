package com.admega.video.chautinhtri;

import java.util.ArrayList;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ypyproductions.myvideo.constanst.IYoutubePlaylistConstants;
import com.ypyproductions.myvideo.object.PlaylistObject;
import com.admega.video.chautinhtri.R;
import com.dobao.bitmap.ImageFetcher;
import com.dobao.utils.StringUtils;

 
public class ListPlaylistAdapter extends BaseAdapter implements IYoutubePlaylistConstants {
	public static final String TAG = ListPlaylistAdapter.class.getSimpleName();

	private Context mContext;
	private ArrayList<PlaylistObject> listPlaylistObjects;

	private ImageFetcher mImageFetcher;

	public ListPlaylistAdapter(Context mContext, ArrayList<PlaylistObject> listPlaylistObjects, ImageFetcher mImageFetcher) {
		this.mContext = mContext;
		this.listPlaylistObjects = listPlaylistObjects;
		this.mImageFetcher = mImageFetcher;
	}

	@Override
	public int getCount() {
		if (listPlaylistObjects != null) {
			return listPlaylistObjects.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		if (listPlaylistObjects != null) {
			return listPlaylistObjects.get(arg0);
		}
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder mHolder;
		LayoutInflater mInflater;
		if (convertView == null) {
			mHolder = new ViewHolder();
			mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.item_playlist, null);
			convertView.setTag(mHolder);
		}
		else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		mHolder.mTvName = (TextView) convertView.findViewById(R.id.tv_title);
		mHolder.mTvNumberVideo1 = (TextView) convertView.findViewById(R.id.tv_numbervideo1);
		mHolder.mTvNumberVideo = (TextView) convertView.findViewById(R.id.tv_numbervideo);
		mHolder.mImgThumb = (ImageView) convertView.findViewById(R.id.img_thumb);

		PlaylistObject mPlaylistObject = listPlaylistObjects.get(position);

		mHolder.mTvName.setText(mPlaylistObject.getTitle());
		
		int numberVideo = mPlaylistObject.getNumberVideos();
		mHolder.mTvNumberVideo.setText(String.valueOf(numberVideo));
		
		String video = "";
		if (numberVideo <= 1) {
			video = String.format(mContext.getString(R.string.format_video), String.valueOf(numberVideo));
		}
		else {
			video = String.format(mContext.getString(R.string.format_videos), String.valueOf(numberVideo));
		}
		mHolder.mTvNumberVideo1.setText(Html.fromHtml(video));

		String urlPhoto = mPlaylistObject.getLinkThumb();
		if (!StringUtils.isStringEmpty(urlPhoto)) {
			mImageFetcher.loadImageNoDisplayDefault(urlPhoto, mHolder.mImgThumb);
		}
		else{
			mHolder.mImgThumb.setImageResource(R.drawable.no_image);
		}

		return convertView;
	}
	
	

	public ArrayList<PlaylistObject> getListPlaylistObjects() {
		return listPlaylistObjects;
	}

	public void setListPlaylistObjects(ArrayList<PlaylistObject> listPlaylistObjects) {
		if(listPlaylistObjects!=null){
			this.listPlaylistObjects = listPlaylistObjects;
			this.notifyDataSetChanged();
		}
	}



	private static class ViewHolder {
		public TextView mTvName;
		public TextView mTvNumberVideo;
		public TextView mTvNumberVideo1;
		public ImageView mImgThumb;
	}
}
