package com.android.cytex.model;

public class Posts {

    private String post;
    private String img;
    private String companyName;
    private String postingDate;
    private String likes;
    private String comments;
    private String post_id;
    private String content_type;
    private String category;
    private String company_id;

    public Posts() {
    }

    public Posts(String post, String img, String companyName, String postingDate, String likes, String comments, String post_id, String content_type, String category, String company_id) {
        this.post = post;
        this.img = img;
        this.companyName = companyName;
        this.postingDate = postingDate;
        this.likes = likes;
        this.comments = comments;
        this.post_id = post_id;
        this.content_type = content_type;
        this.category = category;
        this.company_id = company_id;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPostingDate() {
        return postingDate;
    }

    public void setPostingDate(String postingDate) {
        this.postingDate = postingDate;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getContent_type() {
        return content_type;
    }

    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }
}
