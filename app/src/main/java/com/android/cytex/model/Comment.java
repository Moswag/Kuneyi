package com.android.cytex.model;

public class Comment {

    private String posts_id;
    private String user_name;
    private String user_id;
    private String comment;
    private String date;
    private String comment_id;
    private String comment_status;


    public Comment() {

    }

    public Comment(String posts_id, String user_name, String user_id, String comment, String date, String comment_id) {
        this.posts_id = posts_id;
        this.user_name = user_name;
        this.user_id = user_id;
        this.comment = comment;
        this.date = date;
        this.comment_id = comment_id;
    }

    public String getPosts_id() {
        return posts_id;
    }

    public void setPosts_id(String posts_id) {
        this.posts_id = posts_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getComment_status() {
        return comment_status;
    }

    public void setComment_status(String comment_status) {
        this.comment_status = comment_status;
    }
}
