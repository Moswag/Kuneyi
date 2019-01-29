package com.android.cytex.backgroundtask;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.android.cytex.Interface.VolleySingleton;
import com.android.cytex.db.DbContract;
import com.android.cytex.db.DbHelper;
import com.android.cytex.model.Company;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DownloadCompanies {
    StringRequest request;
    Context context;
    private static final String TAG = "DownloadCompanies";

    public DownloadCompanies(Context context) {
        this.context = context;
    }


    public String getJSON() {
        final DbHelper dbHelper=new DbHelper(context);
        SQLiteDatabase database=dbHelper.getWritableDatabase();
        request = new StringRequest(Request.Method.GET, DbContract.SERVER_ALLCOMPANYLIST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray=new JSONArray(response);

                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=(JSONObject)jsonArray.get(i);
                        Company company=new Company();
                        company.setCompany_name(jsonObject.getString("company_name"));
                        company.setCompany_address(jsonObject.getString("company_address"));
                        company.setCompany_email(jsonObject.getString("email"));
                        company.setCompany_type(jsonObject.getString("company_type"));
                        company.setTel(jsonObject.getString("tel"));
                        company.setLogo(jsonObject.getString("logo"));
                        company.setWebsite(jsonObject.getString("website"));
                        company.setRating(jsonObject.getString("rating"));
                        
                        if(dbHelper.checkCompany(jsonObject.getString("company_name"),jsonObject.getString("company_type"))){
                            Log.i(TAG, "onResponse: The company is already contained in the local database");
                        }
                        else{
                            saveToLocalStorage(company);
                        }

                        



                    }
                    Toast.makeText(context, "Companies downloaded", Toast.LENGTH_LONG).show();

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

    private void saveToLocalStorage(Company company){
        DbHelper dbHelper=new DbHelper(context);
        SQLiteDatabase database=dbHelper.getWritableDatabase();
        dbHelper.saveCompaniesToLocalDatabase(company,database);

        //  readFromLocalStorage();   //to refresh the recyclerview
        dbHelper.close();
    }

}



