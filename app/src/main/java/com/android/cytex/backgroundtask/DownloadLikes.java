package com.android.cytex.backgroundtask;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.android.cytex.Interface.VolleySingleton;
import com.android.cytex.constants.KuneyiConstants;
import com.android.cytex.db.DbContract;
import com.android.cytex.db.DbHelper;
import com.android.cytex.model.Comment;
import com.android.cytex.model.Like;
import com.android.cytex.util.URLConstants;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DownloadLikes {

    StringRequest request;
    Context context;
    private static final String TAG = "DownloadLikes";

    public DownloadLikes(Context context) {
        this.context = context;
    }


    public void getJSON() {
        final DbHelper dbHelper=new DbHelper(context);
        request = new StringRequest(Request.Method.GET, DbContract.SERVER_GET_LIKES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray=new JSONArray(response);

                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=(JSONObject)jsonArray.get(i);
                        Like like=new Like();
                        like.setPost_id(jsonObject.getString("posts_id"));
                        like.setUser_id(jsonObject.getString("user_id"));

                        boolean checkLike=dbHelper.checkLike(like.getPost_id(),like.getUser_id());
                        if(checkLike){
                            //nothing
                        }
                        else{
                            saveToLocalStorage(like);
                        }

                    }
                    Toast.makeText(context, "likes downloaded", Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Failed to connect to database", Toast.LENGTH_LONG).show();
            }
        });

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    private void saveToLocalStorage(Like like){
        DbHelper dbHelper=new DbHelper(context);
        SQLiteDatabase database=dbHelper.getWritableDatabase();
        dbHelper.saveLikesToLocalDatabase(like,true,database);
        dbHelper.close();
    }

    public void saveToRemoteServer(){
        DbHelper dbHelper=new DbHelper(context);
        List<Like> getLikes=dbHelper.getUnsyncedLikes();
        for(Like like: getLikes){
            postLike(like);
        }

        dbHelper.close();
    }

    public String postLike(final Like like) {
        request = new StringRequest(Request.Method.POST, DbContract.SERVER_POST_LIKES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if(jsonObject.getString(KuneyiConstants.REMOTE_RESPONSE).equals(KuneyiConstants.SUCCESS)){
                        saveToLocalStorage(like);
                        Log.i(TAG, "onResponse: "+KuneyiConstants.SUCCESS);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Failed to connect to database", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<>();
                params.put("post_id",like.getPost_id());
                params.put("user_id",like.getUser_id());
                return params;

            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(request);
        return null;
    }
}
