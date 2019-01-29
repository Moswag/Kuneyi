package com.android.cytex.fragment;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.cytex.R;
import com.android.cytex.adapter.AutoPlayVideoAdapter;
import com.android.cytex.backgroundtask.DownloadPosts;
import com.android.cytex.constants.KuneyiConstants;
import com.android.cytex.db.DbContract;
import com.android.cytex.db.DbHelper;
import com.android.cytex.model.Posts;
import com.android.cytex.model.VideoModel;
import com.android.cytex.util.URLConstants;
import com.android.volley.toolbox.StringRequest;
import com.robert.autoplayvideo.CustomRecyclerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewCompanyPostsFragment extends Fragment {
    @BindView(R.id.rv_home)
    CustomRecyclerView recyclerView;
    private final List<VideoModel> modelList = new ArrayList<>();
    private SwipeRefreshLayout refreshLayout;
    DownloadPosts downloadPosts;
    private String companyName;

    @SuppressLint("ValidFragment")
    public ViewCompanyPostsFragment(String companyName) {
        this.companyName=companyName;
    }

    public ViewCompanyPostsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_view_company_posts, container, false);
        refreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                downloadPosts=new DownloadPosts(getActivity());
                downloadPosts.getJSON();
                refreshLayout.setRefreshing(false);
            }
        });

        modelList.clear();
        ButterKnife.bind(getActivity());
        Picasso p = Picasso.with(getActivity());

        DbHelper dbHelper=new DbHelper(getActivity());
        SQLiteDatabase database=dbHelper.getReadableDatabase();

        Cursor cursor=dbHelper.readPostsFromLocalDatabase(database);
        List<Posts> getPosts=dbHelper.getCompanyPosts(companyName);

        for(Posts posts: getPosts){
            String comments=dbHelper.countComments(posts.getPost_id());

            if(posts.getContent_type().equals(KuneyiConstants.VIDEO_CONTENT)){
                modelList.add(new VideoModel(posts.getImg(),posts.getImg(),posts.getCompanyName(),posts.getPost(),posts.getPostingDate(),posts.getPost_id(),comments));
                Toast.makeText(getActivity(),"Downloaded "+posts.getImg(),Toast.LENGTH_SHORT).show();
            }

            else{
                modelList.add(new VideoModel(posts.getImg(),posts.getCompanyName(),posts.getPost(),posts.getPostingDate(),posts.getPost_id(),comments));
                Toast.makeText(getActivity(),"Downloaded "+posts.getImg(),Toast.LENGTH_SHORT).show();
            }
        }



        //mAdapter.notifyDataSetChanged();
        cursor.close();
        dbHelper.close();

        AutoPlayVideoAdapter mAdapter = new AutoPlayVideoAdapter(modelList, p);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView=(CustomRecyclerView) view.findViewById(R.id.rv_home);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //todo before setAdapter
        recyclerView.setActivity(getActivity());

        //optional - to play only first visible video
        recyclerView.setPlayOnlyFirstVideo(true); // false by default

        //optional - by default we check if url ends with ".mp4". If your urls do not end with mp4, you can set this param to false and implement your own check to see if video points to url
        recyclerView.setCheckForMp4(false); //true by default

        //optional - download videos to local storage (requires "android.permission.WRITE_EXTERNAL_STORAGE" in manifest or ask in runtime)
        recyclerView.setDownloadPath(Environment.getExternalStorageDirectory() + "/MyVideo"); // (Environment.getExternalStorageDirectory() + "/Video") by default

        recyclerView.setDownloadVideos(true); // false by default

        //extra - start downloading all videos in background before loading RecyclerView
        List<String> urls = new ArrayList<>();
        for (VideoModel object : modelList) {
            if (object.getVideo_url() != null && object.getVideo_url().contains("http"))
                urls.add(object.getVideo_url());
        }
        recyclerView.preDownload(urls);

        recyclerView.setAdapter(mAdapter);
        //call this function when u want to start autoplay on loading async lists (eg firebase)
//        recyclerView.playAvailableVideos(0);


        return view;
    }



    @Override
    public void onStop() {
        super.onStop();
        //add this code to pause videos (when app is minimised or paused)
        recyclerView.stopVideos();
    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerView.playAvailableVideos(0);
    }



}

