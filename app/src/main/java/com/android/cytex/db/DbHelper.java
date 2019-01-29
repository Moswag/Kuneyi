package com.android.cytex.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.android.cytex.model.AndroidUser;
import com.android.cytex.model.Comment;
import com.android.cytex.model.Company;
import com.android.cytex.model.Like;
import com.android.cytex.model.Posts;

import java.util.ArrayList;
import java.util.List;

public class DbHelper  extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION=1;
    public  SQLiteDatabase db;

    //android users table
    private static final String CREATE_TABLE_ANDROID_USERS="create table "+DbContract.TABLE_NAME_ANDROID_USERS+
            " (id integer primary key autoincrement, "+DbContract.NAME+" text,"+DbContract.SURNAME+" text,"+
            DbContract.PHONENUMBER+" text,"+DbContract.USERNAME+" text,"+DbContract.PASSWORD+" text,"+
            DbContract.SECURITY+" text,"+DbContract.SYSNC_STATUS+" integer);";



    //companies table
    private static final String CREATE_TABLE_COMPANIES="create table "+DbContract.TABLE_NAME_COMPANIES+
            " (id integer primary key autoincrement, "+DbContract.COMPANY_NAME+" text,"+DbContract.COMPANY_EMAIL+" text,"+
            DbContract.COMPANY_ADDRESS+" text,"+DbContract.TEL+" text,"+DbContract.WEBSITE+" text,"+
            DbContract.LOGO+" text,"+DbContract.COMPANY_TYPE+" text,"+DbContract.RATING+" text);";




    //posts table
    private static final String CREATE_TABLE_POSTS="create table "+DbContract.TABLE_NAME_POSTS+
            " (id integer primary key autoincrement, "+DbContract.POST_COMPANY_NAME+" text,"+DbContract.POST+" text,"+
            DbContract.CONTENT_TYPE+" text,"+DbContract.IMG_URL+" text,"+DbContract.COMMENTS+" text,"+
            DbContract.CATEGORY+" text,"+DbContract.COMPANY_ID+" text,"+DbContract.POST_ID+" text,"+DbContract.POST_DATE+" text,"+
            DbContract.LIKES+" text);";


    //comments table
    private static final String CREATE_TABLE_COMMENTS="create table "+DbContract.TABLE_NAME_COMMENTS+
            " (id integer primary key autoincrement, "+DbContract.COMMENT_POST_ID+" text,"+DbContract.COMMENT_USERNAME+" text,"+
            DbContract.COMMENT_USER_ID+" text,"+DbContract.COMMENT_CONTENT+" text,"+DbContract.COMMENT_ID+" text,"+DbContract.COMMENT_STATUS+" text,"+
            DbContract.COMMENT_DATE+" text);";

    //comments table
    private static final String CREATE_TABLE_LIKES="create table "+DbContract.TABLE_NAME_LIKES+
            " (id integer primary key autoincrement, "+DbContract.LIKE_USERNAME+" text,"+DbContract.LIKE_POST_ID+" text,"+
            DbContract.SYSNC_STATUS+" text);";






    //drop tables
    private static final String DROP_TABLE_ANDROID_USERS="drop table if exists "+DbContract.TABLE_NAME_ANDROID_USERS;
    private static final String DROP_TABLE_COMPANIES="drop table if exists "+DbContract.TABLE_NAME_COMPANIES;
    private static final String DROP_TABLE_POSTS="drop table if exists "+DbContract.TABLE_NAME_POSTS;
    private static final String DROP_TABLE_COMMENTS="drop table if exists "+DbContract.TABLE_NAME_COMMENTS;
    private static final String DROP_TABLE_LIKES="drop table if exists "+DbContract.TABLE_NAME_LIKES;

    public DbHelper(Context context){
        super(context,DbContract.DATABASE_NAME,null,DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ANDROID_USERS);
        db.execSQL(CREATE_TABLE_COMPANIES);
        db.execSQL(CREATE_TABLE_POSTS);
        db.execSQL(CREATE_TABLE_COMMENTS);
        db.execSQL(CREATE_TABLE_LIKES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_ANDROID_USERS);
        db.execSQL(DROP_TABLE_COMPANIES);
        db.execSQL(DROP_TABLE_POSTS);
        db.execSQL(DROP_TABLE_COMMENTS);
        db.execSQL(DROP_TABLE_LIKES);
        onCreate(db);

    }


    //login
    public String getlogin(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT * FROM " + DbContract.TABLE_NAME_ANDROID_USERS + " WHERE " + DbContract.USERNAME + " LIKE '%" + username +"%'";
        //String sql = "SELECT * FROM " + DbContract.TABLE_NAME_ANDROID_USERS + " WHERE " + DbContract.USERNAME + " LIKE '%" + username + "%' and "+  DbContract.PASSWORD +" like '%"+ password +"%'";
        Cursor cursor = db.rawQuery(sql,null);
        if (cursor.getCount() < 1) {
            cursor.close();
            return "NOT EXIST";
        }
        cursor.moveToFirst();
        String password = cursor.getString(cursor.getColumnIndex(DbContract.PASSWORD));
        cursor.close();
        return password;
    }

    //login
    public List<Company> getCompanies(String category) {
        List<Company> listCompanies=new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + DbContract.TABLE_NAME_COMPANIES + " WHERE " + DbContract.COMPANY_TYPE + " LIKE '%" + category +"%'";
        Cursor cursor = db.rawQuery(sql,null);

        while(cursor.moveToNext()){

         String  COMPANY_NAME=cursor.getString(cursor.getColumnIndex(DbContract.COMPANY_NAME));
         String  COMPANY_EMAIL=cursor.getString(cursor.getColumnIndex(DbContract.COMPANY_EMAIL));
            String  COMPANY_ADDRESS=cursor.getString(cursor.getColumnIndex(DbContract.COMPANY_ADDRESS));
            String  TEL=cursor.getString(cursor.getColumnIndex(DbContract.TEL));
            String  WEBSITE=cursor.getString(cursor.getColumnIndex(DbContract.WEBSITE));
            String  LOGO=cursor.getString(cursor.getColumnIndex(DbContract.LOGO));
            String COMPANY_TYPE=cursor.getString(cursor.getColumnIndex(DbContract.COMPANY_TYPE));
            String RATING=cursor.getString(cursor.getColumnIndex(DbContract.RATING));

            listCompanies.add(new Company(COMPANY_NAME, COMPANY_TYPE, COMPANY_ADDRESS, WEBSITE,
                    RATING, TEL, LOGO, COMPANY_EMAIL
                    ));
        }

        cursor.close();
        return listCompanies;
    }


    public List<Posts> getCategoryPosts(String category) {
        List<Posts> listPosts=new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + DbContract.TABLE_NAME_POSTS + " WHERE " + DbContract.CATEGORY + " LIKE '%" + category +"%'";
        Cursor cursor = db.rawQuery(sql,null);

        while(cursor.moveToNext()){

            String  POST_COMPANY_NAME=cursor.getString(cursor.getColumnIndex(DbContract.POST_COMPANY_NAME));
            String  POST=cursor.getString(cursor.getColumnIndex(DbContract.POST));
            String  CONTENT_TYPE=cursor.getString(cursor.getColumnIndex(DbContract.CONTENT_TYPE));
            String  IMG_URL=cursor.getString(cursor.getColumnIndex(DbContract.IMG_URL));
            String  COMMENTS=cursor.getString(cursor.getColumnIndex(DbContract.COMMENTS));
            String  CATEGORY=cursor.getString(cursor.getColumnIndex(DbContract.CATEGORY));
            String COMPANY_ID=cursor.getString(cursor.getColumnIndex(DbContract.COMPANY_ID));
            String POST_ID=cursor.getString(cursor.getColumnIndex(DbContract.POST_ID));
            String POST_DATE=cursor.getString(cursor.getColumnIndex(DbContract.POST_DATE));
            String LIKES=cursor.getString(cursor.getColumnIndex(DbContract.LIKES));

            listPosts.add(new Posts(POST, IMG_URL, POST_COMPANY_NAME, POST_DATE,
                    LIKES, COMMENTS, POST_ID, CONTENT_TYPE,CATEGORY,COMPANY_ID
            ));
        }

        cursor.close();
        return listPosts;
    }

    public List<Posts> getCompanyPosts(String company_name) {
        List<Posts> listPosts=new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + DbContract.TABLE_NAME_POSTS + " WHERE " + DbContract.POST_COMPANY_NAME + " LIKE '%" + company_name +"%'";
        Cursor cursor = db.rawQuery(sql,null);

        while(cursor.moveToNext()){

            String  POST_COMPANY_NAME=cursor.getString(cursor.getColumnIndex(DbContract.POST_COMPANY_NAME));
            String  POST=cursor.getString(cursor.getColumnIndex(DbContract.POST));
            String  CONTENT_TYPE=cursor.getString(cursor.getColumnIndex(DbContract.CONTENT_TYPE));
            String  IMG_URL=cursor.getString(cursor.getColumnIndex(DbContract.IMG_URL));
            String  COMMENTS=cursor.getString(cursor.getColumnIndex(DbContract.COMMENTS));
            String  CATEGORY=cursor.getString(cursor.getColumnIndex(DbContract.CATEGORY));
            String COMPANY_ID=cursor.getString(cursor.getColumnIndex(DbContract.COMPANY_ID));
            String POST_ID=cursor.getString(cursor.getColumnIndex(DbContract.POST_ID));
            String POST_DATE=cursor.getString(cursor.getColumnIndex(DbContract.POST_DATE));
            String LIKES=cursor.getString(cursor.getColumnIndex(DbContract.LIKES));

            listPosts.add(new Posts(POST, IMG_URL, POST_COMPANY_NAME, POST_DATE,
                    LIKES, COMMENTS, POST_ID, CONTENT_TYPE,CATEGORY,COMPANY_ID
            ));
        }

        cursor.close();
        return listPosts;
    }

    public List<Comment> getPostsComments(String post_id) {
        List<Comment> listComments=new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + DbContract.TABLE_NAME_COMMENTS + " WHERE " + DbContract.COMMENT_POST_ID + " LIKE '%" + post_id +"%'";
        Cursor cursor = db.rawQuery(sql,null);


        while(cursor.moveToNext()){

            String  COMMENT_POST_ID=cursor.getString(cursor.getColumnIndex(DbContract.COMMENT_POST_ID));
            String  COMMENT_USERNAME=cursor.getString(cursor.getColumnIndex(DbContract.COMMENT_USERNAME));
            String  COMMENT_USER_ID=cursor.getString(cursor.getColumnIndex(DbContract.COMMENT_USER_ID));
            String  COMMENT_CONTENT=cursor.getString(cursor.getColumnIndex(DbContract.COMMENT_CONTENT));
            String  COMMENT_DATE=cursor.getString(cursor.getColumnIndex(DbContract.COMMENT_DATE));
            String  COMMENT_ID=cursor.getString(cursor.getColumnIndex(DbContract.COMMENT_ID));
//String posts_id, String user_name, String user_id, String comment, String date, String comment_id

            listComments.add(new Comment(COMMENT_POST_ID, COMMENT_USERNAME, COMMENT_USER_ID, COMMENT_CONTENT,
                    COMMENT_DATE, COMMENT_ID
            ));
        }

        cursor.close();
        return listComments;
    }

    public Cursor getCompanyPostsORComments(String table_name,String field_name, String constraint){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + table_name + " WHERE " + field_name + " LIKE '%" + constraint +"%'";
        return db.rawQuery(sql,null);
    }

    public List<Comment> getUnsyncedComments() {
        List<Comment> listComments=new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + DbContract.TABLE_NAME_COMMENTS + " WHERE " + DbContract.COMMENT_STATUS + " LIKE '%" + DbContract.FAILED_SYNC +"%'";
        Cursor cursor = db.rawQuery(sql,null);


        while(cursor.moveToNext()){

            String  COMMENT_POST_ID=cursor.getString(cursor.getColumnIndex(DbContract.COMMENT_POST_ID));
            String  COMMENT_USERNAME=cursor.getString(cursor.getColumnIndex(DbContract.COMMENT_USERNAME));
            String  COMMENT_USER_ID=cursor.getString(cursor.getColumnIndex(DbContract.COMMENT_USER_ID));
            String  COMMENT_CONTENT=cursor.getString(cursor.getColumnIndex(DbContract.COMMENT_CONTENT));
            String  COMMENT_DATE=cursor.getString(cursor.getColumnIndex(DbContract.COMMENT_DATE));
            String  COMMENT_ID=cursor.getString(cursor.getColumnIndex(DbContract.COMMENT_ID));

            listComments.add(new Comment(COMMENT_POST_ID, COMMENT_USERNAME, COMMENT_USER_ID, COMMENT_CONTENT,
                    COMMENT_DATE, COMMENT_ID
            ));
        }

        cursor.close();
        return listComments;
    }

    public String countComments(String post_id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+DbContract.TABLE_NAME_COMMENTS+" WHERE "+DbContract.COMMENT_POST_ID+"= "+post_id, null);
        return Integer.toString(cursor.getCount());
    }
    //login
    public boolean checkPost(String post_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT * FROM " + DbContract.TABLE_NAME_POSTS + " WHERE " + DbContract.POST_ID + " LIKE '%" + post_id +"%'";
        Cursor cursor = db.rawQuery(sql,null);
        if (cursor.getCount() < 1) {
            cursor.close();
            return false;
        }
        cursor.moveToFirst();
        String post = cursor.getString(cursor.getColumnIndex(DbContract.POST));
        cursor.close();
        return true;
    }

    public boolean checkComment(String comment_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + DbContract.TABLE_NAME_COMMENTS + " WHERE " + DbContract.COMMENT_ID + " LIKE '%" + comment_id +"%'";
        Cursor cursor = db.rawQuery(sql,null);
        if (cursor.getCount() < 1) {
            cursor.close();
            return false;
        }
        cursor.moveToFirst();
//        String post = cursor.getString(cursor.getColumnIndex(DbContract.POST));
        cursor.close();
        return true;
    }



    public boolean checkCompany(String company_name,String company_type) {
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT * FROM " + DbContract.TABLE_NAME_COMPANIES+ " WHERE " + DbContract.COMPANY_NAME + " LIKE '%" + company_name +"%'"+"AND "+DbContract.COMPANY_TYPE + " LIKE '%" + company_type +"%'";
        Cursor cursor = db.rawQuery(sql,null);
        if (cursor.getCount() < 1) {
            cursor.close();
            return false;
        }
        cursor.moveToFirst();
        cursor.close();
        return true;
    }

    //user tables
    public void saveUserToLocalDatabase(AndroidUser user, SQLiteDatabase database){
        ContentValues contentValues=new ContentValues();
        contentValues.put(DbContract.NAME,user.getName());
        contentValues.put(DbContract.SURNAME,user.getSurname());
        contentValues.put(DbContract.PHONENUMBER,user.getPhonenumber());
        contentValues.put(DbContract.USERNAME,user.getUsername());
        contentValues.put(DbContract.PASSWORD,user.getPassword());
        contentValues.put(DbContract.SECURITY,user.getSecurity());
        contentValues.put(DbContract.SYSNC_STATUS,user.getSync_status());
        database.insert(DbContract.TABLE_NAME_ANDROID_USERS,null,contentValues);
    }

    public Cursor readUserFromLocalDatabase(SQLiteDatabase database){
        String[] projection={DbContract.NAME,DbContract.SURNAME,DbContract.PHONENUMBER,DbContract.USERNAME,DbContract.PASSWORD,DbContract.SECURITY,DbContract.SYSNC_STATUS};
        return (database.query(DbContract.TABLE_NAME_ANDROID_USERS,projection,null,null,null,null,null));
    }

    public void updateUserLocalDatabase(String name, int sync_status,SQLiteDatabase database){
        ContentValues contentValues=new ContentValues();
        contentValues.put(DbContract.SYSNC_STATUS,sync_status);
        String selection=DbContract.DATABASE_NAME+" LIKE ?";
        String[] selection_args={name};
        database.update(DbContract.TABLE_NAME_ANDROID_USERS,contentValues,selection,selection_args);
    }

    //companies tables
    public void saveCompaniesToLocalDatabase(Company company, SQLiteDatabase database){
        ContentValues contentValues=new ContentValues();
        contentValues.put(DbContract.COMPANY_NAME,company.getCompany_name());
        contentValues.put(DbContract.COMPANY_EMAIL,company.getCompany_email());
        contentValues.put(DbContract.COMPANY_ADDRESS,company.getCompany_address());
        contentValues.put(DbContract.TEL,company.getTel());
        contentValues.put(DbContract.WEBSITE,company.getWebsite());
        contentValues.put(DbContract.LOGO,company.getLogo());
        contentValues.put(DbContract.COMPANY_TYPE,company.getCompany_type());
        contentValues.put(DbContract.RATING,company.getRating());
        database.insert(DbContract.TABLE_NAME_COMPANIES,null,contentValues);
    }

    public Cursor readCompaniesFromLocalDatabase(SQLiteDatabase database){
        String[] projection={DbContract.COMPANY_NAME,DbContract.COMPANY_EMAIL,DbContract.COMPANY_ADDRESS,DbContract.TEL,DbContract.LOGO,DbContract.WEBSITE,DbContract.COMPANY_TYPE,DbContract.RATING};
        return (database.query(DbContract.TABLE_NAME_COMPANIES,projection,null,null,null,null,null));
    }

    public void updateCompaniesLocalDatabase(String name, int sync_status,SQLiteDatabase database){
        ContentValues contentValues=new ContentValues();
        contentValues.put(DbContract.SYSNC_STATUS,sync_status);
        String selection=DbContract.DATABASE_NAME+" LIKE ?";
        String[] selection_args={name};
        database.update(DbContract.TABLE_NAME_COMPANIES,contentValues,selection,selection_args);
    }


    //post methods
    public void savePostsToLocalDatabase(Posts posts, SQLiteDatabase database){
        ContentValues contentValues=new ContentValues();
        contentValues.put(DbContract.POST_COMPANY_NAME,posts.getCompanyName());
        contentValues.put(DbContract.POST,posts.getPost());
        contentValues.put(DbContract.CONTENT_TYPE,posts.getContent_type());
        contentValues.put(DbContract.IMG_URL,posts.getImg());
        contentValues.put(DbContract.COMMENTS,posts.getComments());
        contentValues.put(DbContract.CATEGORY,posts.getCategory());
        contentValues.put(DbContract.COMPANY_ID,posts.getCompany_id());
        contentValues.put(DbContract.POST_ID,posts.getPost_id());
        contentValues.put(DbContract.POST_DATE,posts.getPostingDate());
        contentValues.put(DbContract.LIKES,posts.getLikes());
        database.insert(DbContract.TABLE_NAME_POSTS,null,contentValues);
    }

    public Cursor readPostsFromLocalDatabase(SQLiteDatabase database){
        String[] projection={DbContract.POST_COMPANY_NAME,DbContract.POST,DbContract.CONTENT_TYPE,DbContract.IMG_URL,DbContract.COMMENTS,DbContract.CATEGORY,
                DbContract.COMPANY_ID,DbContract.POST_ID,DbContract.POST_DATE,DbContract.LIKES};
        return (database.query(DbContract.TABLE_NAME_POSTS,projection,null,null,null,null,DbContract.POST_ID+" DESC"));
    }

    public void updatePostsLocalDatabase(String name, int sync_status,SQLiteDatabase database){
        ContentValues contentValues=new ContentValues();
        contentValues.put(DbContract.SYSNC_STATUS,sync_status);
        String selection=DbContract.DATABASE_NAME+" LIKE ?";
        String[] selection_args={name};
        database.update(DbContract.TABLE_NAME_POSTS,contentValues,selection,selection_args);
    }


    //comments methods
    public void saveCommentsToLocalDatabase(Comment comments, SQLiteDatabase database){
        ContentValues contentValues=new ContentValues();
        contentValues.put(DbContract.COMMENT_POST_ID,comments.getPosts_id());
        contentValues.put(DbContract.COMMENT_USERNAME,comments.getUser_name());
        contentValues.put(DbContract.COMMENT_USER_ID,comments.getUser_id());
        contentValues.put(DbContract.COMMENT_CONTENT,comments.getComment());
        contentValues.put(DbContract.COMMENT_DATE,comments.getDate());
        contentValues.put(DbContract.COMMENT_ID,comments.getPosts_id());
        contentValues.put(DbContract.COMMENT_STATUS,comments.getComment_status());

        database.insert(DbContract.TABLE_NAME_COMMENTS,null,contentValues);
    }

    public Cursor readCommentsFromLocalDatabase(SQLiteDatabase database){
        String[] projection={DbContract.COMMENT_POST_ID,DbContract.COMMENT_USERNAME,DbContract.COMMENT_USER_ID,DbContract.COMMENT_CONTENT,DbContract.COMMENT_DATE,DbContract.COMMENT_ID};
        return (database.query(DbContract.TABLE_NAME_COMMENTS,projection,null,null,null,null,null));
    }

    public void updateCommentsLocalDatabase(String name, int sync_status,SQLiteDatabase database){
        ContentValues contentValues=new ContentValues();
        contentValues.put(DbContract.SYSNC_STATUS,sync_status);
        String selection=DbContract.DATABASE_NAME+" LIKE ?";
        String[] selection_args={name};
        database.update(DbContract.TABLE_NAME_COMMENTS,contentValues,selection,selection_args);
    }

    //comments methods
    public void saveLikesToLocalDatabase(Like like,boolean sync, SQLiteDatabase database){
        ContentValues contentValues=new ContentValues();
        contentValues.put(DbContract.LIKE_USERNAME,like.getUser_id());
        contentValues.put(DbContract.LIKE_POST_ID,like.getPost_id());
        boolean didDync=checkLike(like.getPost_id(),like.getUser_id());
        if(sync){
            if(didDync){
                Log.i("DBHelper","already synced");
            }
            else {
                contentValues.put(DbContract.SYSNC_STATUS, DbContract.SUCCESS_SYNC);
            }
        }
        else{
            if(didDync){
                Log.i("DBHelper","already synced");
            }
            else {
                contentValues.put(DbContract.SYSNC_STATUS, DbContract.FAILED_SYNC);
            }
        }

        database.insert(DbContract.TABLE_NAME_LIKES,null,contentValues);
    }

    public void updateLikesLocalDatabase(String post_id, SQLiteDatabase database){
        ContentValues contentValues=new ContentValues();
        contentValues.put(DbContract.SYSNC_STATUS,DbContract.SUCCESS_SYNC);
        String selection=DbContract.LIKE_POST_ID+" LIKE ?";
        String[] selection_args={post_id};
        database.update(DbContract.TABLE_NAME_LIKES,contentValues,selection,selection_args);
    }

    public String countLikes(String post_id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+DbContract.TABLE_NAME_LIKES+" WHERE "+DbContract.LIKE_POST_ID+"= "+post_id, null);
        return Integer.toString(cursor.getCount());
    }

    public boolean checkLike(String post_id,String user_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + DbContract.TABLE_NAME_LIKES + " WHERE " + DbContract.LIKE_POST_ID + " LIKE '%" + post_id +"%' AND "+DbContract.LIKE_USERNAME + " LIKE '%" + user_id +"%'";
        Cursor cursor = db.rawQuery(sql,null);
        if (cursor.getCount() < 1) {
            cursor.close();
            return false;
        }
        cursor.moveToFirst();
        cursor.close();
        return true;
    }

    public List<Like> getUnsyncedLikes() {
        List<Like> listLikes=new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + DbContract.TABLE_NAME_LIKES + " WHERE " + DbContract.SYSNC_STATUS + " LIKE '%" + DbContract.FAILED_SYNC +"%'";
        Cursor cursor = db.rawQuery(sql,null);


        while(cursor.moveToNext()){

            Like like=new Like();
            like.setPost_id(cursor.getString(cursor.getColumnIndex(DbContract.LIKE_POST_ID)));
            like.setUser_id(cursor.getString(cursor.getColumnIndex(DbContract.LIKE_USERNAME)));

            listLikes.add(like);
        }

        cursor.close();
        return listLikes;
    }



}
