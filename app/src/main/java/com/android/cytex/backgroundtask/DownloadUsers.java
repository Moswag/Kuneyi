package com.android.cytex.backgroundtask;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.android.cytex.Interface.VolleySingleton;
import com.android.cytex.db.DbContract;
import com.android.cytex.db.DbHelper;
import com.android.cytex.model.AndroidUser;
import com.android.cytex.model.Company;
import com.android.cytex.model.User;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DownloadUsers {


    StringRequest request;
    Context context;

    public DownloadUsers(Context context) {
        this.context = context;
    }


    public String getJSON() {
        DbHelper dbHelper=new DbHelper(context);
        SQLiteDatabase database=dbHelper.getWritableDatabase();
        request = new StringRequest(Request.Method.GET, DbContract.SERVER_ALLUSERLIST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray=new JSONArray(response);

                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=(JSONObject)jsonArray.get(i);
                        AndroidUser user=new AndroidUser();
                        user.setName(jsonObject.getString("name"));
                        user.setSurname(jsonObject.getString("surname"));
                        user.setPhonenumber(jsonObject.getString("phonenumber"));
                        user.setUsername(jsonObject.getString("username"));
                        user.setPassword(jsonObject.getString("password"));
                        user.setSecurity(jsonObject.getString("security"));
                        user.setSync_status(DbContract.SYNC_STATUS_OK);


                        saveToLocalStorage(user);

                    }
                    Toast.makeText(context, "Users downloaded", Toast.LENGTH_LONG).show();

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

    private void saveToLocalStorage(AndroidUser androidUser){
        DbHelper dbHelper=new DbHelper(context);
        SQLiteDatabase database=dbHelper.getWritableDatabase();
        dbHelper.saveUserToLocalDatabase(androidUser,database);

        //  readFromLocalStorage();   //to refresh the recyclerview
        dbHelper.close();
    }
}
