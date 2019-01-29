package com.android.cytex.model;

public class Like {

    private String user_id;
    private String post_id;

    public Like() {
    }

    public Like(String user_id, String post_id) {
        this.user_id = user_id;
        this.post_id = post_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }
}
