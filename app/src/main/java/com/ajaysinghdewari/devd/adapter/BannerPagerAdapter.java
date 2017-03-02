package com.ajaysinghdewari.devd.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.ajaysinghdewari.devd.R;


/**
 * Created by ajay singh dewari on 11/11/16.
 */

public class BannerPagerAdapter extends PagerAdapter {

    private Context mContext;
    TypedArray bannerArray;

    public BannerPagerAdapter(Context context, TypedArray bannerArray){
        mContext=context;
        this.bannerArray = bannerArray;
    }

    @Override
    public int getCount() {
        return bannerArray.length();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
//        return super.instantiateItem(container, position);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ImageView bannerLayout = (ImageView) inflater.inflate(R.layout.banner_pager_layout, container, false);
//        bannerLayout.setBackgroundResource(bannerArray.getResourceId(position, 0));
        bannerLayout.setImageResource(bannerArray.getResourceId(position, 0));
        bannerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext,"swipe clicked"+position, Toast.LENGTH_LONG).show();
            }
        });

        container.addView(bannerLayout);
        return bannerLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
        container.removeView((View) object);

    }
}
