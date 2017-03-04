package com.ajaysinghdewari.devd.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.ajaysinghdewari.devd.R;
import com.ajaysinghdewari.devd.utils.BlurBuilder;

public class SongPlayerActivity extends AppCompatActivity {
    public static final String IMG_BG="inage_bg";
    String mBgUri="";
    ImageView mBtnPrevious, mBtnPlay,mBtnNext, mBtnRepeat,mBtnShuffle,mBtnFavourate, mBtnPlaylistAdd, mBtnQueueMusic, mBtnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_player);

        mBtnPrevious= (ImageView) findViewById(R.id.btn_previous);
        mBtnPlay= (ImageView) findViewById(R.id.btn_play);
        mBtnNext= (ImageView) findViewById(R.id.btn_next);
        mBtnRepeat= (ImageView) findViewById(R.id.btn_repeat);
        mBtnShuffle= (ImageView) findViewById(R.id.btn_shuffle);
        mBtnFavourate= (ImageView) findViewById(R.id.btn_favourate);
        mBtnPlaylistAdd= (ImageView) findViewById(R.id.btn_playlist_add);
        mBtnQueueMusic= (ImageView) findViewById(R.id.btn_queue_music);
        mBtnBack= (ImageView) findViewById(R.id.btn_back);




        if(getIntent()!=null){
            mBgUri=getIntent().getStringExtra(IMG_BG);
        }
        if(!mBgUri.isEmpty()){
            final View content = findViewById(android.R.id.content).getRootView();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
//            bitmap= BitmapFactory.decodeFile(mBgUri, options);
            Bitmap bmImg = BitmapFactory.decodeFile(mBgUri, options);
            BitmapDrawable background = new BitmapDrawable(bmImg);
            content.setBackgroundDrawable(background);


        if (content.getWidth() > 0) {
            Bitmap image = BlurBuilder.blur(content);
            content.setBackgroundDrawable(new BitmapDrawable(getResources(), image));
        } else {
            content.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    Bitmap blrImage = BlurBuilder.blur(content);
                    content.setBackgroundDrawable(new BitmapDrawable(getResources(), blrImage));
                }
            });
        }

            mBtnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }
}
