package layout;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.cytex.MapsActivity;
import com.android.cytex.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListPlaces extends Fragment {
    ListView listView;


    public ListPlaces() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_list_places, container, false);
        listView=(ListView) v.findViewById(R.id.listview);

        List<String> place_list = new ArrayList<String>();
        place_list.add("gas_station");
        place_list.add("hospital");
        place_list.add("restaurant");
        place_list.add("police");
        place_list.add("shopping_mall");
        place_list.add("supermarket");
        place_list.add("lodging");



        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,place_list);

        listView.setAdapter(arrayAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(),listView.getItemAtPosition(i).toString(),Toast.LENGTH_LONG).show();
                Intent intent=new Intent(getActivity(), MapsActivity.class);
                intent.putExtra("placetype",listView.getItemAtPosition(i).toString());
                startActivity(intent);
            }
        });











        return v;
    }

}
