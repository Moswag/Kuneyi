package com.android.cytex.model;

public class Card {
    private String imageurl;
    private String title;
    private String place_type;

    public Card(String imageurl, String title, String place_type) {
        this.imageurl = imageurl;
        this.title = title;
        this.place_type=place_type;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlace_type() {
        return place_type;
    }

    public void setPlace_type(String place_type) {
        this.place_type = place_type;
    }
}
