package com.android.cytex.fragment;


import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.cytex.R;
import com.android.cytex.adapter.CompanyRecyclerViewAdapter;
import com.android.cytex.db.DbContract;
import com.android.cytex.db.DbHelper;
import com.android.cytex.model.Company;
import com.android.cytex.model.VideoModel;
import com.android.cytex.util.SelectedOptionSession;
import com.android.cytex.util.URLConstants;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class CompanyFragment extends Fragment {

    private List<Company> lstCompany;
    private RecyclerView recyclerView;
    private boolean doubleBackToExitPressedOnce;
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    };
    private Handler mHandler = new Handler();
    SelectedOptionSession selectedOptionSession;
    private String category,company_name;



    public CompanyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_company, container, false);

        selectedOptionSession=new SelectedOptionSession(getActivity());
        HashMap<String, String> selected = selectedOptionSession.getSelectedData();
        category = selected.get(SelectedOptionSession.KEY_CATEGORY);

        Toast.makeText(getActivity(),"from shared pref "+category,Toast.LENGTH_LONG).show();

        Picasso p = Picasso.with(getActivity());

        lstCompany =new ArrayList<>();

        recyclerView=(RecyclerView) view.findViewById(R.id.recyclerviewid);
        DbHelper dbHelper=new DbHelper(getActivity());
        SQLiteDatabase database=dbHelper.getReadableDatabase();

        lstCompany=dbHelper.getCompanies(category);


        dbHelper.close();

        CompanyRecyclerViewAdapter myAdapter=new CompanyRecyclerViewAdapter(getActivity(), lstCompany,p);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(myAdapter);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP
                        && keyCode == KeyEvent.KEYCODE_BACK) {

                    if (doubleBackToExitPressedOnce) {
                        // super.onBackPressed();

                        if (mHandler != null) {
                            mHandler.removeCallbacks(mRunnable);
                        }

                        getActivity().finish();

                        return true;
                    }

                    doubleBackToExitPressedOnce = true;
                    Toast.makeText(getActivity(),
                            "Please click BACK again to exit",
                            Toast.LENGTH_SHORT).show();

                    mHandler.postDelayed(mRunnable, 2000);

                }
                android.os.Process.killProcess(android.os.Process.myPid());
                return true;
            }
        });


        return view;
    }


}



