package com.android.cytex.db;

public class DbContract {

    public static final int SYNC_STATUS_OK=0;
    public static final int SYNC_STATUS_FAILED=1;   

    public static String SERVER_LOGIN_URL="http://192.168.43.59/ku/api/login";
    public static String  SERVER_REGISTER_URL="http://192.168.43.59/ku/api/register";
    public static String  SERVER_COMPANYLIST_URL="http://192.168.43.59/ku/api/company";
    public static String  SERVER_ALLCOMPANYLIST_URL="http://192.168.43.59/ku/api/getAllcompanies";
    public static String  SERVER_ALLUSERLIST_URL="http://192.168.43.59/ku/api/getAllAndroidUsers";
    public static String  SERVER_ALLPOSTLIST_URL="http://192.168.43.59/ku/api/getAllPosts";
    public static String  SERVER_ALLCOMMENTSLIST_URL="http://192.168.43.59/ku/api/getAllComments";
    public static String  SERVER_CATEGORY_POSTS_URL="http://192.168.43.59/ku/api/posts";
    public static String  SERVER_RANDOM_POST_URL="http://192.168.43.59/ku/api/randomposts";
    public static String  SERVER_COMPANY_POST_URL="http://192.168.43.59/ku/api/companyposts";
    public static String  SERVER_COMMENT_GET_URL="http://192.168.43.59/ku/api/getcomments";
    public static String  SERVER_COMMENT_POST_URL="http://192.168.43.59/ku/api/postcomment";
    public static String  SERVER_URL_SECURITY="http://192.168.43.59/ku/api/security";
    public static String  SERVER_GET_LIKES="http://192.168.43.59/ku/api/getLikes";
    public static String  SERVER_POST_LIKES="http://192.168.43.59/ku/api/postLike";

    public static final String UI_UPDATE_BRADCAST="com.example.root.cytexsysnc.uiupdatebradcast";



    //database
    public static final String DATABASE_NAME="kuneyidb";

    //user table
    public static final String TABLE_NAME_ANDROID_USERS="android_users";
    //columns
    public static final String  NAME="name";
    public static final String  SURNAME="surname";
    public static final String  PHONENUMBER="phonenumber";
    public static final String  USERNAME="username";
    public static final String  PASSWORD="password";
    public static final String  SECURITY="security";
    public static final String SYSNC_STATUS="syncstatus";

    //company table
    public static final String TABLE_NAME_COMPANIES="companies";
    //columns
    public static final String  COMPANY_NAME="company_name";
    public static final String  COMPANY_EMAIL="email";
    public static final String  COMPANY_ADDRESS="company_address";
    public static final String  TEL="tel";
    public static final String  WEBSITE="website";
    public static final String  LOGO="logo";
    public static final String COMPANY_TYPE="company_type";
    public static final String RATING="rating";

    //company table
    public static final String TABLE_NAME_POSTS="posts";
    //columns
    public static final String  POST_COMPANY_NAME="company_name";
    public static final String  POST="post";
    public static final String  CONTENT_TYPE="content_type";
    public static final String  IMG_URL="img_url";
    public static final String  COMMENTS="comments";
    public static final String  CATEGORY="category";
    public static final String COMPANY_ID="company_id";
    public static final String POST_ID="post_id";
    public static final String POST_DATE="created_at";
    public static final String LIKES="likes";


    //comments table
    public static final String TABLE_NAME_COMMENTS="comments";
    //columns
    public static final String  COMMENT_POST_ID="posts_id";
    public static final String  COMMENT_USERNAME="user_name";
    public static final String  COMMENT_USER_ID="user_id";
    public static final String  COMMENT_CONTENT="comment";
    public static final String  COMMENT_DATE="created_at";
    public static final String  COMMENT_ID="comment_id";
    public static final String  COMMENT_STATUS="status";


    //likes table
    public static final String TABLE_NAME_LIKES="likes";
    //columns
    public static final String  LIKE_POST_ID="posts_id";
    public static final String  LIKE_USERNAME="user_name";




    public static final String  SUCCESS_SYNC="1";
    public static final String  FAILED_SYNC="0";



}
