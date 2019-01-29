package com.android.cytex;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class PlainPlaceActivity extends AppCompatActivity {
    TextView placename,distance,advice;
    Button contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plain_place);

        String name=getIntent().getExtras().getString("name");
        String nzira=getIntent().getExtras().getString("distance");
        int km=Integer.valueOf(nzira);

        placename=(TextView) findViewById(R.id.placename);
        distance=(TextView) findViewById(R.id.distance);
        advice=(TextView) findViewById(R.id.advice);

        placename.setText(name);
        distance.setText(nzira);


        if(km<1){
            advice.setText("The distance is close, you can walk");
        }
        else if(km>=1 &&km<5){
            advice.setText("You can use a bicycle, the distance is close");
        }
        else if(km>=5&& km<15){
            advice.setText("You can use a taxi, press the button below to get in contact with the tax providers");
        }
        else if(km>=15&& km<200){
            advice.setText("You can use a bus, press the button below to get in contact with the tax providers");
        }
        else{

                advice.setText("You can use a plane, press the button below to get in contact with fly operators");

        }

        contact=(Button) findViewById(R.id.contact);

        contact.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });
    }
}
