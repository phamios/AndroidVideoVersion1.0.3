package com.admega.video.chautinhtri;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.admega.video.chautinhtri.R;
import com.dobao.bitmap.ImageFetcher;
import com.dobao.utils.StringUtils;
import com.ypyproductions.myvideo.constanst.IYoutubePlaylistConstants;
import com.ypyproductions.myvideo.object.VideoObject;

 
public class ListVideoAdapter extends BaseAdapter implements IYoutubePlaylistConstants {
	public static final String TAG = ListVideoAdapter.class.getSimpleName();

	private Context mContext;
	private ArrayList<VideoObject> listVideoObjects;

	private ImageFetcher mImageFetcher;

	public ListVideoAdapter(Context mContext, ArrayList<VideoObject> listVideoObjects, ImageFetcher mImageFetcher) {
		this.mContext = mContext;
		this.listVideoObjects = listVideoObjects;
		this.mImageFetcher = mImageFetcher;
	}

	@Override
	public int getCount() {
		if (listVideoObjects != null) {
			return listVideoObjects.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		if (listVideoObjects != null) {
			return listVideoObjects.get(arg0);
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
			convertView = mInflater.inflate(R.layout.item_video, null);
			convertView.setTag(mHolder);
		}
		else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		mHolder.mTvName = (TextView) convertView.findViewById(R.id.tv_title);
		mHolder.mTvStt = (TextView) convertView.findViewById(R.id.tv_stt);
		mHolder.mTvDuration = (TextView) convertView.findViewById(R.id.tv_duration);
		mHolder.mImgThumb = (ImageView) convertView.findViewById(R.id.img_thumb);
		mHolder.mImgPlay = (ImageView) convertView.findViewById(R.id.img_play);

		VideoObject mPlaylistObject = listVideoObjects.get(position);
		convertView.setBackgroundColor(mPlaylistObject.isSelected()?Color.parseColor("#606060") : 
			Color.parseColor("#444444"));
		mHolder.mImgPlay.setVisibility(mPlaylistObject.isSelected()?View.VISIBLE:View.GONE);
		mHolder.mTvStt.setVisibility(!mPlaylistObject.isSelected()?View.VISIBLE:View.GONE);
		mHolder.mTvName.setText(mPlaylistObject.getTitle());
		mHolder.mTvStt.setText(String.valueOf(position+1));
		int duration = mPlaylistObject.getDuration();
		String mStrDuration = "00:00";
		if (duration > 0) {
			String mStrMinute = String.valueOf(duration / 60);
			if (mStrMinute.length() == 1) {
				mStrMinute = "0" + mStrMinute;
			}
			String mStrSeconds = String.valueOf(duration % 60);
			if (mStrSeconds.length() == 1) {
				mStrSeconds = "0" + mStrSeconds;
			}
			mStrDuration = mStrMinute + ":" + mStrSeconds;
		}
		mHolder.mTvDuration.setText(mStrDuration);

		String urlPhoto = mPlaylistObject.getLinkThumb();
		if (!StringUtils.isStringEmpty(urlPhoto)) {
			mImageFetcher.loadImageNoDisplayDefault(urlPhoto, mHolder.mImgThumb);
		}

		return convertView;
	}

	public ArrayList<VideoObject> getListVideoObjects() {
		return listVideoObjects;
	}

	public void setListVideoObjects(ArrayList<VideoObject> listVideoObjects) {
		if(listVideoObjects!=null){
			this.listVideoObjects = listVideoObjects;
			notifyDataSetChanged();
		}
	}

	private static class ViewHolder {
		public TextView mTvName;
		public TextView mTvStt;
		public TextView mTvDuration;
		public ImageView mImgThumb;
		public ImageView mImgPlay;
	}
}
