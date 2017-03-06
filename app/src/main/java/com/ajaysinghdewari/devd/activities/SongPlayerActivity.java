package com.ajaysinghdewari.devd.activities;

import android.content.ContentUris;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ajaysinghdewari.devd.R;
import com.ajaysinghdewari.devd.utils.BlurBuilder;
import com.ajaysinghdewari.devd.utils.CircleImageView;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class SongPlayerActivity extends AppCompatActivity {
    public static final String IMG_BG="image_bg";
    public static final String IMG_ID="image_id";
    String mBgUri="";
    long songID;
    SeekBar seekbar;
    ImageView mBtnPrevious, mBtnPlay, mBtnPause, mBtnNext, mBtnRepeat, mBtnShuffle, mBtnFavourate, mBtnPlaylistAdd, mBtnQueueMusic, mBtnBack;
    TextView mRemainTime, mTime,mSongTitle, mSongArtist;
    CircleImageView mSongImgView;
    private MediaPlayer mediaPlayer;
    private double startTime = 0;
    private double finalTime = 0;
    public static int oneTimeOnly = 0;
    Uri songUri;
    private Handler myHandler = new Handler();;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_player);
        mRemainTime= (TextView) findViewById(R.id.remaintime);
        mTime= (TextView) findViewById(R.id.time);
        mSongTitle= (TextView) findViewById(R.id.song_title);
        mSongArtist= (TextView) findViewById(R.id.song_artist);
        mBtnPrevious= (ImageView) findViewById(R.id.btn_previous);
        mBtnPlay= (ImageView) findViewById(R.id.btn_play);
        mBtnPause= (ImageView) findViewById(R.id.btn_pause);
        mBtnNext= (ImageView) findViewById(R.id.btn_next);
        mBtnRepeat= (ImageView) findViewById(R.id.btn_repeat);
        mBtnShuffle= (ImageView) findViewById(R.id.btn_shuffle);
        mBtnFavourate= (ImageView) findViewById(R.id.btn_favourate);
        mBtnPlaylistAdd= (ImageView) findViewById(R.id.btn_playlist_add);
        mBtnQueueMusic= (ImageView) findViewById(R.id.btn_queue_music);
        mBtnBack= (ImageView) findViewById(R.id.btn_back);
        mSongImgView= (CircleImageView) findViewById(R.id.song_img);
        seekbar = (SeekBar)findViewById(R.id.seekBar);


        if(getIntent()!=null){
            mBgUri=getIntent().getStringExtra(IMG_BG);
            songID=getIntent().getLongExtra(IMG_ID,0L);
        }
        songUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, songID);
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(this,songUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(!mBgUri.isEmpty()){
            final View content = findViewById(android.R.id.content).getRootView();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
//            bitmap= BitmapFactory.decodeFile(mBgUri, options);
            Bitmap bmImg = BitmapFactory.decodeFile(mBgUri);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bmImg);
            mSongImgView.setBackgroundDrawable(bitmapDrawable);
            bmImg = BitmapFactory.decodeFile(mBgUri, options);
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
            mBtnPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mediaPlayer.start();

                    finalTime = mediaPlayer.getDuration();
                    startTime = mediaPlayer.getCurrentPosition();

                    if (oneTimeOnly == 0) {
                        seekbar.setMax((int) finalTime);
                        oneTimeOnly = 1;
                    }

                    mRemainTime.setText(String.format("%d min, %d sec",
                            TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                            finalTime)))
                    );

                    mTime.setText(String.format("%d min, %d sec",
                            TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                            startTime)))
                    );

                    seekbar.setProgress((int)startTime);
                    myHandler.postDelayed(UpdateSongTime,100);
                    mBtnPause.setEnabled(true);
                    mBtnPlay.setEnabled(false);
                    mBtnPlay.setVisibility(View.GONE);
                    mBtnPause.setVisibility(View.VISIBLE);
                }
            });

            mBtnPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mediaPlayer.pause();
                    mBtnPlay.setVisibility(View.VISIBLE);
                    mBtnPause.setVisibility(View.GONE);
                    mBtnPause.setEnabled(false);
                    mBtnPlay.setEnabled(true);
                }
            });
        }
    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            mTime.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime)))
            );
            seekbar.setProgress((int)startTime);
            myHandler.postDelayed(this, 100);
        }
    };
}
