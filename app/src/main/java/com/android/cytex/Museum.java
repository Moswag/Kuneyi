package com.android.cytex;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class Museum extends AppCompatActivity {


    AlertDialog.Builder builder;
    private RequestQueue requestQueue;
    private static final String URL = "http://cytex.000webhostapp.com";
    private StringRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_museum);

        requestQueue = Volley.newRequestQueue(this);
        builder=new AlertDialog.Builder(Museum.this);

        TextView name=(TextView) findViewById(R.id.name);
        TextView distance=(TextView) findViewById(R.id.distance);
        TextView use=(TextView) findViewById(R.id.use);
        ImageView imageView=(ImageView) findViewById(R.id.img1);
        ImageView imageView2=(ImageView) findViewById(R.id.img2);
        TextView activities=(TextView) findViewById(R.id.activities);






        final String placename=getIntent().getExtras().getString("name");
        final String placetype=getIntent().getExtras().getString("placetype");
        final String distanceinKM=getIntent().getExtras().getString("distance");
         double km=Double.valueOf(distanceinKM);

        activities.setText("Activities at "+ placename);
        name.setText("Place Name: " + placename);
        distance.setText("Distance: "+distanceinKM +" KM");
        if(km>0 && km<=1){
            use.setText("You can walk to "+ placename);
        }
        else if(km>1 && km<5){
            use.setText("You can cycle to "+ placename);
        }
        else{
            use.setText("To travel to  "+ placename+" please use a car ");
        }



if(placetype.equals("gas_station")){
    imageView.setImageResource(R.drawable.gas_station1);
    imageView2.setImageResource(R.drawable.gas_station2);
}
else if(placetype.equals("hospital")){
    imageView.setImageResource(R.drawable.hospital1);
    imageView2.setImageResource(R.drawable.hospital2);
}
else if(placetype.equals("restaurant")){
    imageView.setImageResource(R.drawable.restaurant1);
    imageView2.setImageResource(R.drawable.restaurant2);
}
else if(placetype.equals("police")){
    imageView.setImageResource(R.drawable.police);
    imageView2.setImageResource(R.drawable.police2);
}
else if(placetype.equals("shopping_mall")){
    imageView.setImageResource(R.drawable.shopping_mall);
    imageView2.setImageResource(R.drawable.shopping_mall2);
}
else if(placetype.equals("supermarket")){
    imageView.setImageResource(R.drawable.supermarket1);
    imageView2.setImageResource(R.drawable.supermarket2);
}
else if(placetype.equals("lodging")){
    imageView.setImageResource(R.drawable.lodging1);
    imageView2.setImageResource(R.drawable.lodging2);
}

else{
    imageView.setImageResource(R.drawable.ba1);
    imageView2.setImageResource(R.drawable.ba2);
}




    }
}
