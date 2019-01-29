package com.android.cytex.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.cytex.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutDeveloper extends Fragment {


    public AboutDeveloper() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_developer, container, false);
    }

}
