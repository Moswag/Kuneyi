package com.android.cytex.fragment;


import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.cytex.Interface.VolleySingleton;
import com.android.cytex.R;
import com.android.cytex.adapter.AutoPlayVideoAdapter;
import com.android.cytex.adapter.SelectedCategoryPostsAdapter;
import com.android.cytex.backgroundtask.DownloadPosts;
import com.android.cytex.db.DbContract;
import com.android.cytex.db.DbHelper;
import com.android.cytex.model.Posts;
import com.android.cytex.model.VideoModel;
import com.android.cytex.util.SelectedOptionSession;
import com.android.cytex.util.URLConstants;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.robert.autoplayvideo.CustomRecyclerView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCategoryPosts extends Fragment {
    SelectedOptionSession selectedOptionSession;
    private String category;

    @BindView(R.id.rv_home)
    CustomRecyclerView recyclerView;
    List<Posts> listPosts=new ArrayList<>();
    private final List<VideoModel> modelList = new ArrayList<>();
    private SwipeRefreshLayout refreshLayout;
    DownloadPosts downloadPosts;
    private static final String TAG = "FragmentCategoryPosts";

    public FragmentCategoryPosts() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_category_posts, container, false);

        selectedOptionSession=new SelectedOptionSession(getActivity());
        HashMap<String, String> selected = selectedOptionSession.getSelectedData();
        category = selected.get(SelectedOptionSession.KEY_CATEGORY);

        Toast.makeText(getActivity(),"from shared pref "+category,Toast.LENGTH_LONG).show();

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

        listPosts=dbHelper.getCategoryPosts(category);

        for(Posts posts: listPosts){
            if(posts.getContent_type().equals("video")){
                modelList.add(new VideoModel(posts.getImg(),posts.getImg(),posts.getCompanyName(),posts.getPost(),posts.getPostingDate(),posts.getPost_id(),posts.getComments()));

            }
            else{
                modelList.add(new VideoModel(posts.getImg(),posts.getCompanyName(),posts.getPost(),posts.getPostingDate(),posts.getPost_id(),posts.getComments()));

            }

        }



        //mAdapter.notifyDataSetChanged();

        dbHelper.close();



        // modelList.add(new VideoModel("http://192.168.43.59/ku/images/img.jpg", "Image21","watta","12/12/12","1"));

        //you can pass local file uri, but make sure it exists
//        modelList.add(new VideoModel("/storage/emulated/0/VideoPlay/myvideo.mp4","http://res.cloudinary.com/krupen/video/upload/w_300,h_150,c_crop,q_70,so_0/v1481795681/2_rp0zyy.jpg","video18"));

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
        Log.i(TAG, "onResume: executing");

        recyclerView.playAvailableVideos(0);
    }






}



