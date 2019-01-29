package com.android.cytex.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.cytex.R;
import com.android.cytex.acivities.ViewPost;
import com.android.cytex.backgroundtask.DownloadLikes;
import com.android.cytex.db.DbHelper;
import com.android.cytex.model.Like;
import com.android.cytex.model.VideoModel;
import com.robert.autoplayvideo.CustomViewHolder;
import com.robert.autoplayvideo.VideosAdapter;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;
import butterknife.ButterKnife;



public class AutoPlayVideoAdapter extends VideosAdapter {
    private String TAG = "AutoPlayVideoAdapter";
    Context context;

    private final List<VideoModel> list;
    private final Picasso picasso;

    public class MyViewHolder extends CustomViewHolder {
        final TextView company_name, post,date,comment,share,like;
        final ImageView img_vol, img_playback;
        final AppCompatImageView userIcon;

        //to mute/un-mute video (optional)
        boolean isMuted;
        public MyViewHolder(View x) {
            super(x);
            context=x.getContext();
            company_name = ButterKnife.findById(x, R.id.company_name);
            img_vol = ButterKnife.findById(x, R.id.img_vol);
            img_playback = ButterKnife.findById(x, R.id.img_playback);
            userIcon = ButterKnife.findById(x, R.id.fb_user_icon);
            post = ButterKnife.findById(x, R.id.post);
            date = ButterKnife.findById(x, R.id.date);
            share = ButterKnife.findById(x, R.id.fb_button_share);
            like= ButterKnife.findById(x, R.id.button_like);
            comment = ButterKnife.findById(x, R.id.button_comment);
        }

        //override this method to get callback when video starts to play
        @Override
        public void videoStarted() {
            super.videoStarted();
            img_playback.setImageResource(R.drawable.ic_pause);
            if (isMuted) {
                muteVideo();
                img_vol.setImageResource(R.drawable.ic_mute);
            } else {
                unmuteVideo();
                img_vol.setImageResource(R.drawable.ic_unmute);
            }
        }
        @Override
        public void pauseVideo() {
            super.pauseVideo();
            img_playback.setImageResource(R.drawable.ic_play);
        }
    }
    public AutoPlayVideoAdapter(List<VideoModel> list_urls, Picasso p) {
        this.list = list_urls;
        this.picasso = p;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_card2, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {
        DbHelper dbHelper=new DbHelper(context);
        String countLikes= dbHelper.countLikes(list.get(position).getPost_id());
        ((MyViewHolder) holder).company_name.setText(list.get(position).getName());
        ((MyViewHolder) holder).post.setText(list.get(position).getPost());
        ((MyViewHolder) holder).date.setText(list.get(position).getDate());
        ((MyViewHolder) holder).like.setText(countLikes+" Likes");
        int comments=Integer.parseInt(list.get(position).getComments());
        if(comments==0){
            ((MyViewHolder) holder).comment.setText("No Comments");
        }
        else {
            ((MyViewHolder) holder).comment.setText(list.get(position).getComments() + " Comments");
        }
        picasso.load(R.mipmap.ic_launcher).config(Bitmap.Config.RGB_565).into(((MyViewHolder) holder).userIcon);
        //todo
        holder.setImageUrl(list.get(position).getImage_url());
        holder.setVideoUrl(list.get(position).getVideo_url());

        //load image into imageview
        if (list.get(position).getImage_url() != null && !list.get(position).getImage_url().isEmpty()) {
            picasso.load(holder.getImageUrl()).config(Bitmap.Config.RGB_565).into(holder.getImageView());

            Log.e(TAG, "--->ImageUrl=" + holder.getImageUrl());
        }

        ((MyViewHolder) holder).like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadLikes downloadLikes=new DownloadLikes(context);
                Like like=new Like("wmoswa",list.get(position).getPost_id());
                //save like
                downloadLikes.postLike(like);

            }
        });

        holder.setLooping(true); //optional - true by default

        //to play pause videos manually (optional)
        ((MyViewHolder) holder).img_playback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.isPlaying()) {
                    holder.pauseVideo();
                    holder.setPaused(true);
                } else {
                    holder.playVideo();
                    holder.setPaused(false);
                }
            }
        });

        //to mute/un-mute video (optional)
        ((MyViewHolder) holder).img_vol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((MyViewHolder) holder).isMuted) {
                    holder.unmuteVideo();
                    ((MyViewHolder) holder).img_vol.setImageResource(R.drawable.ic_unmute);
                } else {
                    holder.muteVideo();
                    ((MyViewHolder) holder).img_vol.setImageResource(R.drawable.ic_mute);
                }
                ((MyViewHolder) holder).isMuted = !((MyViewHolder) holder).isMuted;
            }
        });

        //to mute/un-mute video (optional)
        ((MyViewHolder) holder).comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context, ViewPost.class);
                intent.putExtra("post",list.get(position).getPost());
                intent.putExtra("company_name",list.get(position).getName());
                intent.putExtra("image_url",list.get(position).getImage_url());
                intent.putExtra("created_at",list.get(position).getDate());
                intent.putExtra("post_id",list.get(position).getPost_id());
                context.startActivity(intent);
            }
        });

        //to mute/un-mute video (optional)
        ((MyViewHolder) holder).share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                final File photoFile = new File(list.get(position).getVideo_url());
                sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(photoFile));
                context.startActivity(Intent.createChooser(sendIntent, "Share using"));

            }
        });


        if (list.get(position).getVideo_url() == null) {
            ((MyViewHolder) holder).img_vol.setVisibility(View.GONE);
            ((MyViewHolder) holder).img_playback.setVisibility(View.GONE);
        } else {
            ((MyViewHolder) holder).img_vol.setVisibility(View.VISIBLE);
            ((MyViewHolder) holder).img_playback.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    @Override
    public int getItemViewType(int position) {
        return 0;
    }


}