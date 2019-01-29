package com.android.cytex.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.cytex.R;
import com.android.cytex.acivities.Kuneyi;
import com.android.cytex.model.Posts;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;



public class SelectedCategoryPostsAdapter extends RecyclerView.Adapter<SelectedCategoryPostsAdapter.Holderview> {
 private List<Posts> postlist;
 private Context context;
    RequestOptions option;

    // Estimates bandwidth
    BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
    // TrackSelector is used When a piece of media contains multiple tracks of a given type,
    // for example multiple video tracks in different qualities or multiple audio tracks in
    // different languages
    // Adaptative: selected track is updated to be the one of highest quality given the
    // current network conditions and the state of the buffer
    TrackSelection.Factory videoTrackSelectionFactory =
            new AdaptiveTrackSelection.Factory(bandwidthMeter);

    TrackSelector trackSelector =
            new DefaultTrackSelector(videoTrackSelectionFactory);

    // Controls buffering of media
    LoadControl loadControl = new DefaultLoadControl();

    SimpleExoPlayer player;



    public SelectedCategoryPostsAdapter(List<Posts> productlist, Context context) {
        this.postlist = productlist;
        this.context = context;

        // Request option for glide
        option=new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);
    }

    @NonNull
    @Override
    public Holderview onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       View layout= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.selected_company_posts,viewGroup,false);

         player = ExoPlayerFactory.newSimpleInstance(context, trackSelector, loadControl);
        return new Holderview(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull Holderview holderview, final int position) {

        holderview.v_name.setText(postlist.get(position).getPost());
        holderview.v_like.setText(postlist.get(position).getLikes());
        holderview.v_comment.setText(postlist.get(position).getComments());

        String cat=postlist.get(position).getContent_type();
        if(cat.equals("video")) {
            // Bind the player to the view.
            holderview.simpleExoPlayerView.setVisibility(View.VISIBLE);
            holderview.simpleExoPlayerView.setPlayer(player);
            // DataSource instance through which media data is loaded.
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                    Util.getUserAgent(context, context.getString(R.string.app_name)));

            // Produces Extractor instances for parsing the media data.
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

           // String mp4VideoUrl = postlist.get(position).getImg();

            Uri mp4VideoUri = Uri.parse(postlist.get(position).getImg());

            // MediaSource representing the media to be played.
            MediaSource videoSource = new ExtractorMediaSource(mp4VideoUri,
                    dataSourceFactory, extractorsFactory, null, null);

            // Prepare the player with the source.
            player.prepare(videoSource);

            // Start to play when player is ready
            player.setPlayWhenReady(false);
        }
        else {
            holderview.v_image.setVisibility(View.VISIBLE);
            //glide
            Glide.with(context).load(postlist.get(position).getImg()).apply(option).into(holderview.v_image);
        }

        holderview.v_like_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"You liked",Toast.LENGTH_LONG).show();
            }
        });

        holderview.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"click on"+ postlist.get(position).getPost(),Toast.LENGTH_LONG).show();
                Intent intent=new Intent(context, Kuneyi.class);  //to be changed
                intent.putExtra("post",postlist.get(position).getPost());
                intent.putExtra("company_name",postlist.get(position).getCompanyName());
                intent.putExtra("image_url",postlist.get(position).getImg());
                intent.putExtra("created_at",postlist.get(position).getPostingDate());
                intent.putExtra("post_id",postlist.get(position).getPost_id());
                context.startActivity(intent);

            }
        });



    }

    @Override
    public int getItemCount() {
        return postlist.size();
    }

    public void setfilter(List<Posts> listitem){
        postlist=new ArrayList<>();
        postlist.addAll(listitem);
        notifyDataSetChanged();
    }

    class Holderview extends RecyclerView.ViewHolder{
        ImageView v_image;
        ImageView v_like_image;
        TextView v_name;
        TextView v_like;
        TextView v_comment;
        SimpleExoPlayerView simpleExoPlayerView;


        Holderview(View itemview){
          super(itemview);
          v_image=(ImageView) itemview.findViewById(R.id.product_image);
            v_like_image=(ImageView) itemview.findViewById(R.id.like_image);
            v_name=(TextView) itemview.findViewById(R.id.product_title);
            v_like=(TextView) itemview.findViewById(R.id.likes);
            v_comment=(TextView) itemview.findViewById(R.id.comments);

            simpleExoPlayerView = (SimpleExoPlayerView) itemview.findViewById(R.id.video_player);

        }

    }
}
