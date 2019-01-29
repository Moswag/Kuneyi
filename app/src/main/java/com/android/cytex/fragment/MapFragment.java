package com.android.cytex.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.cytex.MapsActivity;
import com.android.cytex.R;
import com.android.cytex.adapter.CustomListAdapter;
import com.android.cytex.model.Card;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment {

    private static final String TAG="Tab2Fragment";
    private ListView listView;

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_map, container, false);

        listView=(ListView) view.findViewById(R.id.listview);


        ArrayList<Card> list=new ArrayList<>();
        list.add(new Card("drawable://"+ R.drawable.bar,"Bar","bar"));
        list.add(new Card("drawable://"+ R.drawable.cafe,"Cafe","cafe"));
        list.add(new Card("drawable://"+ R.drawable.fastfood,"Fast food","cafe"));
        list.add(new Card("drawable://"+ R.drawable.hotel,"Hotel","lodging"));
        list.add(new Card("drawable://"+ R.drawable.supermarket,"Supermarket","supermarket"));
        list.add(new Card("drawable://"+ R.drawable.outdoor,"Outdoor","amusement_park"));
        list.add(new Card("drawable://"+ R.drawable.restaurant,"Restaurant","restaurant"));
        list.add(new Card("drawable://"+ R.drawable.sporting,"Sporting","stadium"));
        list.add(new Card("drawable://"+ R.drawable.wedding,"Wedding","church"));


        CustomListAdapter mAdapter=new CustomListAdapter(getActivity(),R.layout.map_list,list);

        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(),listView.getItemAtPosition(i).toString(),Toast.LENGTH_LONG).show();
                Intent intent=new Intent(getActivity(), MapsActivity.class);
                intent.putExtra("placetype","bar");
                startActivity(intent);
            }
        });


        return view;
    }

}
