package com.hben.needo.comments.control;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hben.needo.R;
import com.hben.needo.comments.model.Comment;
import com.hben.needo.comments.view.CommentsViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ben on 09/02/2017.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsViewHolder> {

    private List<Comment> comments;

    public CommentsAdapter(List<Comment> comments) {
        this.comments = comments;
    }

    public CommentsAdapter() {
        this(new ArrayList<Comment>());
    }

    @Override
    public CommentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comments_layout, parent, false);
        return new CommentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentsViewHolder holder, int position) {
        Comment comment =  comments.get(position);
        holder.userNameTextView.setText(comment.getUserName());
        holder.commentTextView.setText(comment.getComment());

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void insert(Comment comment){
        comments.add(0,comment);
        notifyItemInserted(0);
    }
}
