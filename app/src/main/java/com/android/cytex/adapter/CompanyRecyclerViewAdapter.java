package com.android.cytex.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.cytex.R;
import com.android.cytex.acivities.Kuneyi;
import com.android.cytex.acivities.ViewCompanyActivity;
import com.android.cytex.model.Company;
import com.android.cytex.util.SelectedOptionSession;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import java.util.List;


public class CompanyRecyclerViewAdapter extends RecyclerView.Adapter<CompanyRecyclerViewAdapter.MyViewHolder>{

    private Context mContext;
    private List<Company> mData;
    RequestOptions option;
    SelectedOptionSession selectedOptionSession;
    private final Picasso picasso;

    public CompanyRecyclerViewAdapter(Context mContext, List<Company> mData,Picasso p) {
        this.mContext = mContext;
        this.mData = mData;
        this.picasso = p;

        // Request option for glide
        option=new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater inflater=LayoutInflater.from(mContext);
        view=inflater.inflate(R.layout.company_row_item,viewGroup,false);
        selectedOptionSession = new SelectedOptionSession(mContext);
        final MyViewHolder viewHolder=new MyViewHolder(view);


        viewHolder.view_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(mContext, ViewCompanyActivity.class);  //to be changed
                i.putExtra("anime_name",mData.get(viewHolder.getAdapterPosition()).getCompany_name());
                i.putExtra("anime_description",mData.get(viewHolder.getAdapterPosition()).getCompany_address());
                i.putExtra("anime_studio",mData.get(viewHolder.getAdapterPosition()).getWebsite());
                i.putExtra("anime_category",mData.get(viewHolder.getAdapterPosition()).getCompany_type());
                i.putExtra("anime_rating",mData.get(viewHolder.getAdapterPosition()).getRating());
                i.putExtra("anime_nb_episode",mData.get(viewHolder.getAdapterPosition()).getWebsite());
                i.putExtra("anime_img",mData.get(viewHolder.getAdapterPosition()).getLogo());
                selectedOptionSession.createSelectedOption(mData.get(viewHolder.getAdapterPosition()).getCompany_name(),true);
                mContext.startActivity(i);

            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {

        myViewHolder.tv_name.setText(mData.get(position).getCompany_name());
        myViewHolder.tv_category.setText(mData.get(position).getCompany_address());
        myViewHolder.tv_studio.setText(mData.get(position).getWebsite());
        myViewHolder.tv_rating.setText(mData.get(position).getRating());

        //load image from the internet and set into imageview using glide

        //Glide.with(mContext).load(mData.get(position).getLogo()).apply(option).into(myViewHolder.img_thumbnail);
        picasso.load(mData.get(position).getLogo()).config(Bitmap.Config.RGB_565).into(myViewHolder.img_thumbnail);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name;
        TextView tv_rating;
        TextView tv_studio;
        TextView tv_category;
        ImageView img_thumbnail;
        LinearLayout view_container;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            view_container=itemView.findViewById(R.id.container);

            tv_name=itemView.findViewById(R.id.anime_name);
            tv_category=itemView.findViewById(R.id.categorie);
            tv_rating=itemView.findViewById(R.id.rating);
            tv_studio=itemView.findViewById(R.id.studio);
            img_thumbnail=itemView.findViewById(R.id.thumbnail);

        }
    }

}
