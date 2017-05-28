package com.hben.needo.feed.control;

import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.hben.needo.R;
import com.hben.needo.comments.model.Comment;
import com.hben.needo.feed.model.FeedContent;
import com.hben.needo.feed.view.FeedContentViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ben on 23/01/2017.
 */

public class FeedContentAdapter extends RecyclerView.Adapter<FeedContentViewHolder> {


    private List<FeedContent> feedContents;
    private FeedClickListener feedClickListener;
    private List<Comment> comments;

    public FeedContentAdapter() {
        this(new ArrayList<FeedContent>());
    }

    public FeedContentAdapter(List<FeedContent> feedContents) {
        this.feedContents = feedContents;
        feedClickListener = null;
    }


    @Override
    public FeedContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_card_layout, parent, false);
        return new FeedContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FeedContentViewHolder holder, int position) {
        FeedContent feedContent = feedContents.get(position);
        holder.titleView.setText(feedContent.getTitle());
        holder.contentView.setText(feedContent.getContent());
//        holder.userProfilePicture.setImageURI(Uri.parse(feedContent.getUserProfilePicture()));

        Glide.with(holder.itemView.getContext())
                .load(feedContent.getUserProfilePicture())
                .asBitmap()
                .centerCrop()
                .override(200,200)
                .into(new BitmapImageViewTarget(holder.userProfilePicture) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(holder.itemView.getContext().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        holder.userProfilePicture.setImageDrawable(circularBitmapDrawable);
                    }
                });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (feedClickListener != null)
                    feedClickListener.onPostClicked(holder);
            }
        });

    }

    public FeedContent getItems(int position) {
        return feedContents.get(position);
    }

    public void setFeedClickListener(FeedClickListener feedClickListener) {
        this.feedClickListener = feedClickListener;
    }

    @Override
    public int getItemCount() {
        return feedContents.size();
    }

    public void insert(FeedContent feedContent) {
        feedContents.add(0, feedContent);
        notifyItemChanged(0);

    }

    public void clear() {
        feedContents.clear();
        notifyDataSetChanged();
    }

}
