package com.android.cytex.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.cytex.Interface.FragmentCommunication;
import com.android.cytex.R;
import com.android.cytex.adapter.RecyclerViewAdapterCategory;
import com.android.cytex.model.Category;
import com.android.cytex.util.SelectedOptionSession;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Welcome extends Fragment {

    List<Category> lstCategory;
    SelectedOptionSession selectedOptionSession;


    public Welcome() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_welcome, container, false);
        selectedOptionSession = new SelectedOptionSession(getActivity());


        lstCategory = new ArrayList<>();

        lstCategory.add(new Category("Bar", "BAR", "Description Category", "drawable://"+R.drawable.bar));
        lstCategory.add(new Category("Cafe", "CAFE", "Description Category","drawable://"+R.drawable.cafe));
        lstCategory.add(new Category("Fast Food", "FAST_FOOD", "Description Category", "drawable://"+R.drawable.fastfood));
        lstCategory.add(new Category("Hotel", "HOTEL", "Description Category", "drawable://"+R.drawable.hotel));

        lstCategory.add(new Category("Outdoor Place", "OUTDOOR_PLACE", "Description Category", "drawable://"+R.drawable.outdoor));
        lstCategory.add(new Category("Restaurant", "RESTAURANT", "Description Category", "drawable://"+R.drawable.restaurant));
        lstCategory.add(new Category("Sporting Activities", "SPORTING_ACTIVITY", "Description Category", "drawable://"+R.drawable.sporting));
        lstCategory.add(new Category("SuperMarket", "SUPERMARKET", "Description Category", "drawable://"+R.drawable.supermarket));
        lstCategory.add(new Category("Wedding Venue", "WEDDING_VENUE", "Description Category", "drawable://"+R.drawable.wedding));

        RecyclerView myrv = (RecyclerView) view.findViewById(R.id.recyclerview_id);
        RecyclerViewAdapterCategory myAdapter = new RecyclerViewAdapterCategory(getActivity(), lstCategory, communication);
        myrv.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        myrv.setAdapter(myAdapter);

        return view;
    }


    FragmentCommunication communication = new FragmentCommunication() {
        @Override
        public void respond(int position, String name) {
            AllListFragment allListFragment = new AllListFragment();
            Bundle bundle = new Bundle();
            bundle.putString("category", name);
            selectedOptionSession.createSelectedOption(name);
            Toast.makeText(getActivity(),"you selected "+name,Toast.LENGTH_LONG).show();
            allListFragment.setArguments(bundle);
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.frame, allListFragment).commit();
        }


    };
}
