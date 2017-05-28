package com.hben.needo.feed.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hben.needo.R;
import com.hben.needo.comments.model.Comment;

import java.util.List;

/**
 * Created by Ben on 23/01/2017.
 */

public class FeedContentViewHolder extends RecyclerView.ViewHolder {

    public final TextView titleView;
    public final TextView contentView;
    public final ImageView userProfilePicture;

    public FeedContentViewHolder(View view) {
        super(view);
        titleView = (TextView) view.findViewById(R.id.title);
        contentView = (TextView) view.findViewById(R.id.content);
        userProfilePicture = (ImageView) view.findViewById(R.id.user_photo);

    }

}
