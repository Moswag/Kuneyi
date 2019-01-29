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
import com.android.cytex.model.Company;
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

public class DownloadComments {

    StringRequest request;
    Context context;
    private static final String TAG = "DownloadComments";

    public DownloadComments(Context context) {
        this.context = context;
    }


    public String getJSON() {
        final DbHelper dbHelper=new DbHelper(context);
        SQLiteDatabase database=dbHelper.getWritableDatabase();
        request = new StringRequest(Request.Method.GET, DbContract.SERVER_ALLCOMMENTSLIST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray=new JSONArray(response);

                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=(JSONObject)jsonArray.get(i);
                        Comment comment=new Comment();
                        comment.setPosts_id(jsonObject.getString("posts_id"));
                        comment.setComment(jsonObject.getString("comment"));
                        comment.setDate(jsonObject.getString("created_at"));
                        comment.setUser_id(jsonObject.getString("user_id"));
                        comment.setUser_name(jsonObject.getString("user_name"));
                        comment.setComment_id(jsonObject.getString("id"));
                        comment.setComment_status(DbContract.SUCCESS_SYNC);

                        boolean checkConment=dbHelper.checkComment(jsonObject.getString("id"));
                        if(checkConment){
                            //nothing
                        }
                        else{
                            saveToLocalStorage(comment);
                        }

                    }
                    Toast.makeText(context, "Comments downloaded", Toast.LENGTH_LONG).show();

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

    private void saveToLocalStorage(Comment comment){
        DbHelper dbHelper=new DbHelper(context);
        SQLiteDatabase database=dbHelper.getWritableDatabase();
        dbHelper.saveCommentsToLocalDatabase(comment,database);

        //  readFromLocalStorage();   //to refresh the recyclerview
        dbHelper.close();
    }

    public void saveToRemoteServer(){
        DbHelper dbHelper=new DbHelper(context);
        List<Comment> getComments=dbHelper.getUnsyncedComments();
        for(Comment comment: getComments){
            postCommment(comment);
        }

        dbHelper.close();
    }

    private String postCommment(final Comment comment) {
        request = new StringRequest(Request.Method.POST, URLConstants.COMMENT_GET_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if(jsonObject.getString(KuneyiConstants.REMOTE_RESPONSE).equals(KuneyiConstants.SUCCESS)){
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
                params.put("post_id",comment.getPosts_id());
                params.put("comment",comment.getComment());
                params.put("user_name",comment.getUser_name());
                params.put("user_id",comment.getUser_id());
                return params;

            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(request);
        return null;
    }

}
