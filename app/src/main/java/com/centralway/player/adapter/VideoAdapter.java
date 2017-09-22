package com.centralway.player.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.centralway.player.R;
import com.centralway.player.data.Video;
import com.centralway.player.views.Checkbox;

import java.util.List;

import static com.centralway.player.utils.TimeUtils.getTimeFormatted;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by sergiodejesus on 6/24/17.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder> {

    private VideoItemListener mItemListener;
    private List<Video> mItems;
    private Context mContext;
    private int mLayout;

    public VideoAdapter(List<Video> items, VideoItemListener itemListener, Context context, int layout) {
        setList(items);
        mItemListener = itemListener;
        mContext = context;
        mLayout = layout;
    }
    public VideoAdapter(List<Video> items, VideoItemListener itemListener, Context context){
        this(items, itemListener, context, R.layout.item_video);
    }
    public void replaceData(List<Video> videos) {
        setList(videos);
        notifyDataSetChanged();
    }

    private void setList(List<Video> videos) {
        mItems = checkNotNull(videos);
    }

    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        VideoHolder vh;
        View v = LayoutInflater.from(parent.getContext())
                .inflate(mLayout, parent, false);
        vh = new VideoHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(VideoHolder holder, int position) {
        VideoHolder videoHolder = (VideoHolder) holder;
        Video video = mItems.get(position);
        videoHolder.bind(video, mItemListener);
        videoHolder.mDuration.setText(getTimeFormatted(video.getLength()));
        Glide.with(mContext)
                .load(video.getUri())
                .into(videoHolder.mThumbnail);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public interface VideoItemListener {

        void onVideoClick(Video clickedVideo);

        void onVideoLongClick(Video longClickedVideo);

        void onItemSelected(Video selectedVideo);

        void onItemUnSelected(Video selectedVideo);

    }

    public class VideoHolder extends RecyclerView.ViewHolder {

        public AppCompatImageView mThumbnail;
        public TextView mDuration;
        public CheckBox mSelected;

        public VideoHolder(View itemView) {
            super(itemView);
            mThumbnail = (AppCompatImageView) itemView.findViewById(R.id.item_video_thumbnail);
            mDuration = (TextView) itemView.findViewById(R.id.item_video_duration);
            mSelected = (Checkbox) itemView.findViewById(R.id.item_video_check);
        }

        public void bind(final Video item, final VideoItemListener itemListener) {
            itemView.setOnClickListener(v -> {
                        itemListener.onVideoClick(item);
                    }
            );
            itemView.setOnLongClickListener(v -> {
                itemListener.onVideoLongClick(item);
                return true;
            });
            if(mSelected != null){
                mSelected.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if(isChecked)
                        itemListener.onItemSelected(item);
                    else
                        itemListener.onItemUnSelected(item);
                });
            }
        }
    }
}
