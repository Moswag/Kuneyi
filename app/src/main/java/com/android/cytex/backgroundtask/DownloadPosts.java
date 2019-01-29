package com.android.cytex.backgroundtask;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.android.cytex.Interface.VolleySingleton;
import com.android.cytex.db.DbContract;
import com.android.cytex.db.DbHelper;
import com.android.cytex.model.Company;
import com.android.cytex.model.Posts;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DownloadPosts {
    StringRequest request;
    Context context;

    public DownloadPosts(Context context) {
        this.context = context;
    }


    public String getJSON() {
       final DbHelper dbHelper=new DbHelper(context);
        SQLiteDatabase database=dbHelper.getWritableDatabase();
        request = new StringRequest(Request.Method.GET, DbContract.SERVER_ALLPOSTLIST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray=new JSONArray(response);

                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=(JSONObject)jsonArray.get(i);
                        Posts posts=new Posts();
                        posts.setCompanyName(jsonObject.getString("company_name"));
                        posts.setCategory(jsonObject.getString("category"));
                        posts.setContent_type(jsonObject.getString("content_type"));
                        posts.setPost(jsonObject.getString("post"));
                        posts.setImg(jsonObject.getString("img_url"));
                        posts.setPostingDate(jsonObject.getString("created_at"));
                        posts.setLikes(jsonObject.getString("likes"));
                        posts.setComments(jsonObject.getString("comments"));
                        posts.setPost_id(jsonObject.getString("id"));
                        posts.setCompany_id(jsonObject.getString("company_id"));

                        boolean checkP=dbHelper.checkPost(jsonObject.getString("id"));
                        if(checkP){
                            //nothing
                        }
                        else{
                            saveToLocalStorage(posts);
                        }

                    }
                    Toast.makeText(context, "Posts downloaded", Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // saveToLocalStorage(name, surname, phonenumber, username, password, security, DbContract.SYNC_STATUS_FAILED);
                Toast.makeText(context, "Failed to connect to database", Toast.LENGTH_LONG).show();
            }
        });

        VolleySingleton.getInstance(context).addToRequestQueue(request);
        return null;
    }

    private void saveToLocalStorage(Posts posts){
        DbHelper dbHelper=new DbHelper(context);
        SQLiteDatabase database=dbHelper.getWritableDatabase();
        dbHelper.savePostsToLocalDatabase(posts,database);

        //  readFromLocalStorage();   //to refresh the recyclerview
        dbHelper.close();
    }
}
