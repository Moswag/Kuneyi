package com.android.cytex.model;

/**
 * Created by robert on 17/08/03.
 */
public class VideoModel {
    private String image_url;
    private String video_url;
    private String name;
    private String date;
    private String post;
    private String post_id;
    private String comments;

    public VideoModel(String video_url, String image_url, String name) {
        this.video_url = video_url;
        this.image_url = image_url;
        this.name = name;
    }

    public VideoModel(String image_url, String name) {
        this.image_url = image_url;
        this.name = name;
    }

    public VideoModel(String image_url, String video_url, String name, String post, String date, String post_id,String comments) {
        this.image_url = image_url;
        this.video_url = video_url;
        this.name = name;
        this.post = post;
        this.date = date;
        this.post_id = post_id;
        this.comments=comments;
    }

    public VideoModel(String image_url, String name, String post, String date, String post_id,String comments) {
        this.image_url = image_url;
        this.name = name;
        this.post = post;
        this.date = date;
        this.post_id = post_id;
        this.comments=comments;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getVideo_url() {
        return video_url;
    }

    public String getName() {
        return name;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
