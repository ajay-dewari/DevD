package com.ajaysinghdewari.devd.activities;

import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.ajaysinghdewari.devd.R;
import com.ajaysinghdewari.devd.adapter.BannerPagerAdapter;
import com.ajaysinghdewari.devd.adapter.SongsAdapter;
import com.ajaysinghdewari.devd.models.Song;
import com.ajaysinghdewari.devd.utils.BlurBuilder;
import com.ajaysinghdewari.devd.utils.Utility;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /*================Banner Variables================*/
    private ViewPager mBannerViewPager;
    private BannerPagerAdapter mBannerPagerAdapter;
    private LinearLayout mBannerDotsLayout;
    private TypedArray mBannerArray;
    private int numberOfBannerImage;
    private View[] mBannerDotViews;

    private RecyclerView mSongsRecyclerView;
    private ArrayList<Song> songList;
    private SongsAdapter songsAdapter;
    View transparentBG;
    private static final int COLOUMNWIDTH=80;

    FloatingActionButton fab, fab1, fab2, fab3;
    boolean isFabOpen=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        final Window window=getWindow();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        transparentBG = findViewById(R.id.transparentView);
        transparentBG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFAB();
            }
        });

/*===========This below code is used to make the view with the transparent image applied over it=========================*/
        /*final View content = findViewById(R.id.transparentView);
        if (content.getWidth() > 0) {
            Bitmap image = BlurBuilder.blur(content);
            content.setBackgroundDrawable(new BitmapDrawable(getResources(), image));
        } else {
            content.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    Bitmap image = BlurBuilder.blur(content);
                    content.setBackgroundDrawable(new BitmapDrawable(getResources(), image));
                }
            });
        }*/

        mSongsRecyclerView= (RecyclerView) findViewById(R.id.music_recycler_view);
        songList = new ArrayList<Song>();
        getSongList();
        /*===================Below code used to sort the song alphabatically====================*/
/*        Collections.sort(songList, new Comparator<Song>(){
            public int compare(Song a, Song b){
                return a.getTitle().compareTo(b.getTitle());
            }
        });*/
        int numberOfColumns = Utility.calculateNoOfColumns(getApplicationContext(), COLOUMNWIDTH);
        mSongsRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        songsAdapter = new SongsAdapter(this, songList);
        mSongsRecyclerView.setAdapter(songsAdapter);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                if(isFabOpen){
                    closeFAB();
                }else{
                    openFAB();
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

      /*===================Inetelizing Banner Variables===============*/
        mBannerArray = getResources().obtainTypedArray(R.array.banner_img_array);
        numberOfBannerImage=mBannerArray.length();
        mBannerDotViews = new View[numberOfBannerImage]; // create an empty array;

        /*===================Banner Pager Configuration=================*/
        mBannerViewPager = (ViewPager) findViewById(R.id.bannerViewPager);
        mBannerDotsLayout= (LinearLayout) findViewById(R.id.bannerDotsLayout);
        mBannerPagerAdapter=new BannerPagerAdapter(HomeActivity.this, mBannerArray);
        mBannerViewPager.setAdapter(mBannerPagerAdapter);


        /*===========================START Banner Configuration Code ======================================*/

        for (int i = 0; i < numberOfBannerImage; i++) {
            // create a new textview
            final View bannerDotView = new View(this);
/*Creating the dynamic dots for banner*/
            LinearLayout.LayoutParams dotLayoutParm=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            dotLayoutParm.height = getResources().getDimensionPixelSize(R.dimen.standard_8);
            dotLayoutParm.width = getResources().getDimensionPixelSize(R.dimen.standard_8);
            dotLayoutParm.setMargins(getResources().getDimensionPixelSize(R.dimen.standard_5),0,0,0);
            bannerDotView.setLayoutParams(dotLayoutParm);
            bannerDotView.setBackground(getUnselectedDotShape());

            // add the textview to the linearlayout
            mBannerDotsLayout.addView(bannerDotView);


            // save a reference to the tview for later
            mBannerDotViews[i] = bannerDotView;
        }

        AutoSwipeBanner();
        mBannerViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeDotBG(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
/*===========================END Banner Configuration Code ======================================*/

    }

    public void getSongList() {
        //retrieve song info
//        final String orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC";
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns

            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            int albumColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ALBUM);
            int albumIDColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ALBUM_ID);

            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                String thisAlbum = musicCursor.getString(albumColumn);
                long thisAlbumID = musicCursor.getLong(albumIDColumn);
                Cursor cursor = musicResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                        MediaStore.Audio.Albums._ID + "=?",
                        new String[]{String.valueOf(thisAlbumID)},
                        null);
                String thisImage = "";
                if (cursor.moveToFirst()) {
                    thisImage = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                    // do whatever you need to do
                }
                songList.add(new Song(thisId, thisTitle, thisArtist, thisImage));
            }
            while (musicCursor.moveToNext());
        }
    }


    private void openFAB(){
        isFabOpen=true;
        transparentBG.setVisibility(View.VISIBLE);
        fab.animate().rotationBy(90);
        fab1.animate().translationY(170);
        fab2.animate().translationY(320);
        fab3.animate().translationY(470);
    }

    private void closeFAB(){
        isFabOpen=false;
        transparentBG.setVisibility(View.GONE);
        fab.animate().rotationBy(-90);
        fab1.animate().translationY(0);
        fab2.animate().translationY(0);
        fab3.animate().translationY(0);
    }

    /*======================================START Functions to configur Banner AutoSwip===========================================*/
    private GradientDrawable getUnselectedDotShape(){
        int backgroundColor = Color.TRANSPARENT;
        int strokeColor = Color.WHITE;
        int strokeSize = getResources().getDimensionPixelSize(R.dimen.standard_1);
        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{backgroundColor, backgroundColor});
        drawable.setShape(GradientDrawable.OVAL);
        drawable.setStroke(strokeSize, strokeColor);
        drawable.setCornerRadius(strokeSize);
        return drawable;
    }

    private GradientDrawable getSelectedDotShape(){
        int backgroundColor = Color.WHITE;
        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{backgroundColor, backgroundColor});
        drawable.setShape(GradientDrawable.OVAL);
        return drawable;
    }

    public void AutoSwipeBanner(){
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                int currentPage=mBannerViewPager.getCurrentItem();
                if (currentPage == numberOfBannerImage-1) {
                    currentPage = -1;
                }
                mBannerViewPager.setCurrentItem(currentPage+1, true);
            }
        };

        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(Update);
            }
        }, 500, 3000);

    }

    private void changeDotBG(int position){

        for(int i = 0; i < numberOfBannerImage; i++){
            if(position==i){
                GradientDrawable drawable = getSelectedDotShape();
                mBannerDotViews[i].setBackground(drawable);
            }else{
                GradientDrawable drawable = getUnselectedDotShape();
                mBannerDotViews[i].setBackground(drawable);
            }

        }
    }
/*======================================END Functions to configur Banner AutoSwip===========================================*/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(isFabOpen){
            closeFAB();
        }else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.home, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_search);
        SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) mSearchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setTextColor(Color.BLACK);

        ArrayList<String> itemArrayList=new ArrayList<>();
        itemArrayList.add("Ajay");
        itemArrayList.add("Ajit");
        itemArrayList.add("Aju");
        itemArrayList.add("Abhishik");
        itemArrayList.add("Abh");
        itemArrayList.add("Abhlash");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, itemArrayList);
        searchAutoComplete.setAdapter(adapter);
        SearchManager searchManager =
                (SearchManager) getSystemService(this.SEARCH_SERVICE);
        mSearchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
