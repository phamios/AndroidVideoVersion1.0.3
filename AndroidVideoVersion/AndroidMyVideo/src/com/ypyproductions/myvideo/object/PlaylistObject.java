package com.ypyproductions.myvideo.object;

public class PlaylistObject {

	private String id;
	private String title;
	private String author;
	private String timeUpdate;
	private int numberVideos;
	private String description;
	private String linkThumb;

	public PlaylistObject() {
		super();
	}

	public PlaylistObject(String id, String title, String author, String timeUpdate, int numberVideos, String description, String linkThumb) {
		super();
		this.id = id;
		this.title = title;
		this.author = author;
		this.timeUpdate = timeUpdate;
		this.numberVideos = numberVideos;
		this.description = description;
		this.linkThumb = linkThumb;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTimeUpdate() {
		return timeUpdate;
	}

	public void setTimeUpdate(String timeUpdate) {
		this.timeUpdate = timeUpdate;
	}

	public int getNumberVideos() {
		return numberVideos;
	}

	public void setNumberVideos(int numberVideos) {
		this.numberVideos = numberVideos;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLinkThumb() {
		return linkThumb;
	}

	public void setLinkThumb(String linkThumb) {
		this.linkThumb = linkThumb;
	}

}
