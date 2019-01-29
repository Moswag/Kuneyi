package com.android.cytex.model;

public class Company {

    private String company_name;
    private String company_type;
    private String company_address;
    private String company_email;
    private String website;
    private String rating;
    private String tel;
    private String logo;



    public Company() {
    }

    public Company(String company_name, String company_type, String company_address, String website, String rating, String tel, String logo,String company_email) {
        this.company_name = company_name;
        this.company_type = company_type;
        this.company_address = company_address;
        this.website = website;
        this.rating = rating;
        this.tel = tel;
        this.logo = logo;
        this.company_email = company_email;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCompany_type() {
        return company_type;
    }

    public void setCompany_type(String company_type) {
        this.company_type = company_type;
    }

    public String getCompany_address() {
        return company_address;
    }

    public void setCompany_address(String company_address) {
        this.company_address = company_address;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getCompany_email() {
        return company_email;
    }

    public void setCompany_email(String company_email) {
        this.company_email = company_email;
    }
}


