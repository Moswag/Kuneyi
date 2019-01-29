package com.android.cytex.acivities;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.cytex.R;
import com.android.cytex.fragment.ViewCompanyPostsFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class ViewCompanyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_company);

        String name= getIntent().getExtras().getString("anime_name");
        String description= getIntent().getExtras().getString("anime_description");
        String studio= getIntent().getExtras().getString("anime_studio");
        String category= getIntent().getExtras().getString("anime_category");
        String rating= getIntent().getExtras().getString("anime_rating");
        int episode= getIntent().getExtras().getInt("anime_nb_episode");
        String image_url= getIntent().getExtras().getString("anime_img");

        //ini views
        CollapsingToolbarLayout collapsingToolbarLayout=findViewById(R.id.collapsingtoolbar_id);
        collapsingToolbarLayout.setTitleEnabled(true);

        TextView tv_name=findViewById(R.id.aa_anime_name);
        TextView tv_studio=findViewById(R.id.aa_studio);
        TextView tv_categorize=findViewById(R.id.aa_categorie);
        TextView tv_description=findViewById(R.id.aa_description);
        TextView tv_rating=findViewById(R.id.aa_rating);
        ImageView img=findViewById(R.id.aa_thumbnail);


        //setting values to each view
        tv_name.setText(name);
        tv_categorize.setText(category);
        tv_description.setText(description);
        tv_studio.setText(studio);
        tv_rating.setText(rating);

        collapsingToolbarLayout.setTitle(name);


        RequestOptions requestOptions=new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);


        //set image using glide

        Glide.with(this).load(image_url).apply(requestOptions).into(img);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame,
                new ViewCompanyPostsFragment(tv_name.getText().toString())).commit();
    }
}
