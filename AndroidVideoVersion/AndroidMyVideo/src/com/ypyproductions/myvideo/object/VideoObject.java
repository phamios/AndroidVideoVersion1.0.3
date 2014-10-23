package com.ypyproductions.myvideo.object;

 
public class VideoObject {

	private String id;
	private String videoId;
	private String timeUpdate;
	private String title;
	private String description;
	private int duration;
	private float rating;
	private String linkThumb;
	private String linkVideo;
	private boolean isSelected;

	public VideoObject(String id, String videoId, String timeUpdate, String title, String description, int duration, float rating, String linkThumb, String linkVideo) {
		super();
		this.id = id;
		this.videoId = videoId;
		this.timeUpdate = timeUpdate;
		this.title = title;
		this.description = description;
		this.duration = duration;
		this.rating = rating;
		this.linkThumb = linkThumb;
		this.linkVideo = linkVideo;
	}

	public VideoObject() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVideoId() {
		return videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	public String getTimeUpdate() {
		return timeUpdate;
	}

	public void setTimeUpdate(String timeUpdate) {
		this.timeUpdate = timeUpdate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}

	public String getLinkThumb() {
		return linkThumb;
	}

	public void setLinkThumb(String linkThumb) {
		this.linkThumb = linkThumb;
	}

	public String getLinkVideo() {
		return linkVideo;
	}

	public void setLinkVideo(String linkVideo) {
		this.linkVideo = linkVideo;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

}
