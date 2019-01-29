package com.android.cytex.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.cytex.R;
import com.android.cytex.acivities.ViewPost;
import com.android.cytex.constants.KuneyiConstants;
import com.android.cytex.model.Comment;
import com.android.cytex.util.UserSessionManager;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.Holderview> {
 private List<Comment> commentlist;
 private Context context;
    RequestOptions option;
    UserSessionManager session;
    String user_id;

    public CommentAdapter(List<Comment> commentlist, Context context) {
        this.commentlist = commentlist;
        this.context = context;
        session=new UserSessionManager(context);
        HashMap<String,String> user=session.getUserDetails();
        user_id=user.get(UserSessionManager.KEY_User);


    }

    @NonNull
    @Override
    public Holderview onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       View layout= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat,viewGroup,false);

        return new Holderview(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull Holderview holderview, final int position) {

        if(commentlist.get(position).getUser_id().equals(user_id)){
            holderview.v_name.setText(KuneyiConstants.YOU);
        }
        else{
            holderview.v_name.setText(commentlist.get(position).getUser_name());
        }
        holderview.v_content.setText(commentlist.get(position).getComment());
        holderview.v_datetime.setText(commentlist.get(position).getDate());


    }

    @Override
    public int getItemCount() {
        return commentlist.size();
    }

    public void setfilter(List<Comment> listcomment){
        commentlist=new ArrayList<>();
        commentlist.addAll(listcomment);
        notifyDataSetChanged();
    }

    class Holderview extends RecyclerView.ViewHolder{

        TextView v_name;
        TextView v_content;
        TextView v_datetime;

        Holderview(View itemview){
          super(itemview);

            v_name=(TextView) itemview.findViewById(R.id.name);
            v_content=(TextView) itemview.findViewById(R.id.content);
            v_datetime=(TextView) itemview.findViewById(R.id.datetime);
        }

    }
}
