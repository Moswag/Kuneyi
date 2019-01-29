package com.android.cytex.acivities;

import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.cytex.Interface.DoneOnEditorActionListener;
import com.android.cytex.Interface.VolleySingleton;
import com.android.cytex.R;
import com.android.cytex.adapter.CommentAdapter;
import com.android.cytex.db.DbHelper;
import com.android.cytex.model.Comment;
import com.android.cytex.util.URLConstants;
import com.android.cytex.util.UserSessionManager;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewPost extends AppCompatActivity {
    LinearLayout mContainer;
    EditText mEditText;
    FloatingActionButton send;

    private List<Comment> lstComments;
    private RecyclerView recyclerView;
    private StringRequest request;

    UserSessionManager session;
    String user_name;
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);

        session=new UserSessionManager(ViewPost.this);
        HashMap<String,String> user=session.getUserDetails();
        user_name=user.get(UserSessionManager.KEY_Name);
       //user_id=user.get(UserSessionManager.KEY_User);
        user_id="wmoswa";



        // Request option for glide
        RequestOptions requestOptions=new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);




        String company_name=getIntent().getExtras().getString("company_name");
        String post=getIntent().getExtras().getString("post");
        String image_url=getIntent().getExtras().getString("image_url");
        String created_at=getIntent().getExtras().getString("created_at");
        final  String post_id=getIntent().getExtras().getString("post_id");



        ImageView img=(ImageView)findViewById(R.id.thumbnail);
        TextView post_content=(TextView)findViewById(R.id.post_desc);
        TextView post_date=(TextView)findViewById(R.id.post_time);

        //fro previous fragment
        post_content.setText(post);
        post_date.setText(created_at);
        //glide

        Toast.makeText(getApplicationContext(),image_url,Toast.LENGTH_LONG).show();

        Glide.with(this).load(image_url).into(img);

        lstComments=new ArrayList<>();

        DbHelper dbHelper=new DbHelper(ViewPost.this);
        SQLiteDatabase database=dbHelper.getReadableDatabase();
        lstComments=dbHelper.getPostsComments(post_id);

        mContainer = (LinearLayout) findViewById(R.id.container);
        mEditText = (EditText) findViewById(R.id.whatsapp_edit_view);
        recyclerView=(RecyclerView)findViewById(R.id.comment_recycler);
        send=(FloatingActionButton) findViewById(R.id.send_button);
      //  mEditText.setOnEditorActionListener(new DoneOnEditorActionListener());


        CommentAdapter myAdapter=new CommentAdapter(lstComments, getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(myAdapter);


       // jsonrequest("",post_id);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.clearFocus();
                if(mEditText.getText().toString()!=""){
                    lstComments.clear();
                    jsonrequest(mEditText.getText().toString(),post_id);
                    mEditText.setText("");
                }
                else{
                    Toast.makeText(getApplicationContext(),"Please fill in the the comment to send",Toast.LENGTH_LONG).show();
                }

            }
        });
    }


    private void jsonrequest(final String comment, final String post_id) {
        request = new StringRequest(Request.Method.POST, URLConstants.COMMENT_GET_URL, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try {

                    JSONArray jsonArry = new JSONArray(response);
                    for(int i=0 ; i< jsonArry.length();i++) {

                        JSONObject jsonObject = (JSONObject) jsonArry.get(i);
                        Comment comment=new Comment();
                        comment.setUser_name(jsonObject.getString("user_name"));
                        comment.setComment(jsonObject.getString("comment"));
                        comment.setDate(jsonObject.getString("created_at"));
                        comment.setUser_id(jsonObject.getString("user_id"));

                        lstComments.add(comment);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setuprecyclerview(lstComments);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Failed to connect to database",Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<>();
                params.put("post_id",post_id);
                params.put("comment",comment);
                params.put("user_name",user_name);
                params.put("user_id",user_id);
                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    private void setuprecyclerview(List<Comment> lstComment) {
        CommentAdapter myAdapter=new CommentAdapter(lstComment, getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(myAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        Toast.makeText(getApplicationContext(),"Onresume executed",Toast.LENGTH_LONG).show();
    }


}

