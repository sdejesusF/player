package com.centralway.player.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.centralway.player.R;
import com.centralway.player.data.Playlist;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by sergiodejesus on 6/25/17.
 */

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistHolder> {

    private PlaylistAdapter.PlaylistItemListener mItemListener;
    private List<Playlist> mItems;
    private int mLayout;

    public PlaylistAdapter(List<Playlist> items, PlaylistAdapter.PlaylistItemListener itemListener) {
        this(items, itemListener, R.layout.item_playlist);
    }

    public PlaylistAdapter(List<Playlist> items, PlaylistAdapter.PlaylistItemListener itemListener, int layout) {
        setList(items);
        mItemListener = itemListener;
        mLayout = layout;
    }

    public void replaceData(List<Playlist> playlist) {
        setList(playlist);
        notifyDataSetChanged();
    }

    private void setList(List<Playlist> playlist) {
        mItems = checkNotNull(playlist);
    }

    @Override
    public PlaylistAdapter.PlaylistHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PlaylistAdapter.PlaylistHolder vh;
        View v = LayoutInflater.from(parent.getContext())
                .inflate(mLayout, parent, false);
        vh = new PlaylistAdapter.PlaylistHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(PlaylistAdapter.PlaylistHolder holder, int position) {
        Playlist playlist = mItems.get(position);
        holder.bind(playlist, mItemListener);
        holder.mName.setText(playlist.getName());
  }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public interface PlaylistItemListener {

        void onPlaylistClick(Playlist clickedPlaylist);

        void onPlaylistPlay(Playlist clickedPlaylist);

        void onPlaylistDelete(Playlist clickedPlaylist);
    }

    public class PlaylistHolder extends RecyclerView.ViewHolder {

        public TextView mName;
        public View mWrapper;
        public AppCompatImageView mPlay;
        public AppCompatImageView mDelete;


        public PlaylistHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.item_playlist_name);
            mWrapper = itemView.findViewById(R.id.item_playlist_wrapper);
            mPlay = (AppCompatImageView) itemView.findViewById(R.id.item_playlist_play);
            mDelete = (AppCompatImageView) itemView.findViewById(R.id.item_playlist_delete);

        }

        public void bind(final Playlist item, final PlaylistAdapter.PlaylistItemListener itemListener) {
            itemView.setOnClickListener(v -> {itemListener.onPlaylistClick(item);});
            if(mPlay != null)
                mPlay.setOnClickListener(v -> {itemListener.onPlaylistPlay(item);});
            if(mDelete != null)
                mDelete.setOnClickListener(v -> {itemListener.onPlaylistDelete(item);});
        }
    }
}
