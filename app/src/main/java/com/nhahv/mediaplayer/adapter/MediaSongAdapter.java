package com.nhahv.mediaplayer.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.nhahv.mediaplayer.R;
import com.nhahv.mediaplayer.databinding.ItemMediaSongBinding;
import com.nhahv.mediaplayer.models.MediaSong;

import java.util.List;

/**
 * Created by Nhahv on 7/24/2016.
 * <></>
 */
public class MediaSongAdapter extends RecyclerView.Adapter<MediaSongAdapter.ViewHolder> {

    private Context mContext;
    private List<MediaSong> mListMediaSong;

    public MediaSongAdapter(Context mContext, List<MediaSong> mListMediaSong) {
        this.mContext = mContext;
        this.mListMediaSong = mListMediaSong;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater
                .from(mContext)
                .inflate(R.layout.item_media_song, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MediaSong item = mListMediaSong.get(position);
        holder.linearLayout.setBackgroundColor(
                mContext.getResources()
                        .getColor(android.R.color.transparent));

        holder.bind(item);
        if (item.isChose()) {
            holder.linearLayout
                    .setBackgroundColor(
                            mContext.getResources()
                                    .getColor(R.color.background));
        }

    }

    @Override
    public int getItemCount() {
        return mListMediaSong.size();
    }

    static final class ViewHolder extends RecyclerView.ViewHolder {

        private ItemMediaSongBinding binding;
        private LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linear_item);
        }


        public void bind(MediaSong mediaSong) {
            binding.setMedia(mediaSong);
        }
    }
}
