package com.ajaysinghdewari.devd.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ajaysinghdewari.devd.R;
import com.ajaysinghdewari.devd.activities.SongPlayerActivity;
import com.ajaysinghdewari.devd.models.Song;
import com.ajaysinghdewari.devd.utils.CircleImageView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Ajay on 25-02-2017.
 */

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.ViewHolder>{

    Context context;
    ArrayList<Song> songArrayList;

    public SongsAdapter(Context context, ArrayList<Song> songArrayList) {
        this.context = context;
        this.songArrayList = songArrayList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.songs_item, parent, false);
        return new SongsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = songArrayList.get(position);
        holder.mSongTitleTV.setText(songArrayList.get(position).getTitle());
        holder.mArtistTV.setText(songArrayList.get(position).getArtist());
        final String thisArt = songArrayList.get(position).getImage();
        final long thisID = songArrayList.get(position).getId();
        Bitmap bitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 6;
            bitmap= BitmapFactory.decodeFile(thisArt, options);
        holder.mSongImageVew.setImageBitmap(bitmap);
        final String songname=songArrayList.get(position).getTitle();
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, songname+" is clicked", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        holder.mFavourateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mFavourateBtn.animate().rotation(360);
                holder.mFavourateBtn.setColorFilter(context.getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);
                Intent intent=new Intent(context, SongPlayerActivity.class);
                intent.putExtra(SongPlayerActivity.IMG_BG,thisArt);
                intent.putExtra(SongPlayerActivity.IMG_ID,thisID);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mSongTitleTV;
        public final TextView mArtistTV;
        public final CircleImageView mSongImageVew;
        public final ImageView mFavourateBtn;

        public Song mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mSongTitleTV = (TextView) view.findViewById(R.id.song_title);
            mArtistTV = (TextView) view.findViewById(R.id.song_artist);
            mSongImageVew = (CircleImageView) view.findViewById(R.id.song_img);
            mFavourateBtn= (ImageView) view.findViewById(R.id.favourateBtn);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mArtistTV.getText() + "'";
        }
    }

}
