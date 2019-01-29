package com.android.cytex.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.cytex.Interface.FragmentCommunication;
import com.android.cytex.R;
import com.android.cytex.fragment.CompanyFragment;
import com.android.cytex.model.Category;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;


public class RecyclerViewAdapterCategory extends RecyclerView.Adapter<RecyclerViewAdapterCategory.MyViewHolder>{
    RequestOptions option;



    private Context mContext;
    private static List<Category> mData;
    Context context;

    private FragmentCommunication mCommunicator;



    public RecyclerViewAdapterCategory(Context mContext, List<Category> mData, FragmentCommunication communication) {
        this.mContext = mContext;
        this.mData = mData;
        this.mCommunicator=communication;
        option=new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);
        setupImageLoader();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater mInflator=LayoutInflater.from(mContext);
        view=mInflator.inflate(R.layout.cardview_item_category,viewGroup,false);
        MyViewHolder holder = new MyViewHolder(view,mCommunicator);
        return holder;
    }



    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position) {

        myViewHolder.tv_book_title.setText(mData.get(position).getTitle());
       // myViewHolder.img_book_thumbnail.setImageResource(mData.get(position).getThumbnail());     //to be reverted

//load image from the internet and set into imageview using glide

       // Glide.with(mContext).load(mData.get(position).getThumbnail()).apply(option).into(myViewHolder.img_book_thumbnail);

        ImageLoader imageLoader = ImageLoader.getInstance();

        int defaultImage = mContext.getResources().getIdentifier("@drawable/image_failed",null,mContext.getPackageName());

        //create display options
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(defaultImage)
                .showImageOnFail(defaultImage)
                .showImageOnLoading(defaultImage).build();

        //download and display image from url
        imageLoader.displayImage(mData.get(position).getThumbnail(), myViewHolder.img_book_thumbnail, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

               // holder.dialog.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

               // holder.dialog.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

              //  holder.dialog.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });



        CompanyFragment companyFragment =new CompanyFragment();
                String selectedCategory=mData.get(position).getTitle();
                //storing data in bundle
                Bundle bundle=new Bundle();
                bundle.putString("category",selectedCategory);
                //passing data to fragment
                companyFragment.setArguments(bundle);
//
//                FragmentManager fragmentTransaction=((AppCompatActivity)mContext).getSupportFragmentManager();
//                fragmentTransaction.beginTransaction().replace(R.id.frame,companyFragment).commit();
//


//                Intent intent=new Intent(mContext,BarActivity.class);
//                //passiing data to the next activity
//                intent.putExtra("Title",mData.get(position).getTitle());
//                intent.putExtra("Description",mData.get(position).getDescription());
//                intent.putExtra("Thumbnail",mData.get(position).getThumbnail());
//                //start activity
//                mContext.startActivity(intent);




    }



    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_book_title;
        ImageView img_book_thumbnail;
        CardView cardView;

        FragmentCommunication mComminication;



        public MyViewHolder(@NonNull View itemView, FragmentCommunication Communicator) {
            super(itemView);

            tv_book_title=(TextView) itemView.findViewById(R.id.book_title_id);
            img_book_thumbnail=(ImageView) itemView.findViewById(R.id.book_img_id);
            mComminication=Communicator;
            cardView=(CardView) itemView.findViewById(R.id.cardview_id);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mComminication.respond(getAdapterPosition(),mData.get(getAdapterPosition()).getTitle());
        }
    }

    private void setupImageLoader(){
        // UNIVERSAL IMAGE LOADER SETUP
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                mContext)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
        // END - UNIVERSAL IMAGE LOADER SETUP
    }

}
