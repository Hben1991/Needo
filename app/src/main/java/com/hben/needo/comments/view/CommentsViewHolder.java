package com.hben.needo.comments.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hben.needo.R;

/**
 * Created by Ben on 09/02/2017.
 */

public class CommentsViewHolder extends RecyclerView.ViewHolder {

    public TextView userNameTextView;
    public TextView commentTextView;


    public CommentsViewHolder(View itemView) {
        super(itemView);
        userNameTextView = (TextView) itemView.findViewById(R.id.userNameCommentTextView);
         commentTextView = (TextView) itemView.findViewById(R.id.commentContentTextView);
    }
}
