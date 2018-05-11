package com.example.martin.login;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Martin on 9. 5. 2018.
 */

public class SliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;
    String str1,str2,str3;


    public SliderAdapter(Context context){
        this.context = context;
    }

    public int[] slide_imgs ={
      R.drawable.onboardpost,
      R.drawable.onboarddelete,
      R.drawable.onboardlocation
    };


    @Override
    public int getCount() {
        return slide_imgs.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater =(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout,container,false);
        ImageView slideImageView = (ImageView) view.findViewById(R.id.slideImage);
        TextView headingTextView = (TextView) view.findViewById(R.id.slideHeading);
        TextView descTextView = (TextView) view.findViewById(R.id.slideDesc);
        slideImageView.setImageResource(slide_imgs[position]);
        int[] slide_headings = {R.string.slide_head_post
                ,R.string.slide_head_list
                ,R.string.slide_head_gelocation};
        int[] slide_desc = {R.string.slide_desc_posts
                ,R.string.slide_desc_list
                ,R.string.slide_desc_gelocation};
        headingTextView.setText(context.getString(slide_headings[position]));
        descTextView.setText(slide_desc[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}
