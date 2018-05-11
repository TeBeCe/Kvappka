package com.example.martin.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.transition.Slide;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.share.Share;

import org.w3c.dom.Text;

public class OnBoardingActivity extends AppCompatActivity implements View.OnClickListener{
    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;
    private SliderAdapter sliderAdapter;
    private TextView[] mDots;
    private Button mNextButton;
    private Button mPrevButton;
    private int mCurrentPage;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(final int position) {
            addDotsIndicator(position);
            mCurrentPage = position;
            if(position ==0){
                mNextButton.setEnabled(true);
                mPrevButton.setEnabled(false);
                mPrevButton.setVisibility(View.INVISIBLE);
                mNextButton.setText(R.string.next);
                mPrevButton.setText("");

                }

            else if (position == mDots.length - 1) {
                String[] permissions = new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION};
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(permissions, 123);
                }

                mNextButton.setEnabled(true);
                mPrevButton.setEnabled(true);
                mPrevButton.setVisibility(View.VISIBLE);
                mNextButton.setText(R.string.finish);
                mPrevButton.setText(R.string.previous);
                mNextButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mEditor.putBoolean("alreadyOnBoarded",true);
                        mEditor.apply();
                        if(position == mDots.length -1) {
                            Intent intent = new Intent(OnBoardingActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
            else {

                mNextButton.setEnabled(true);
                mPrevButton.setEnabled(true);
                mPrevButton.setVisibility(View.VISIBLE);
                mNextButton.setVisibility(View.VISIBLE);
                mNextButton.setText(R.string.next);
                mPrevButton.setText(R.string.previous);

            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);
        sliderAdapter = new SliderAdapter(this);
        mSlideViewPager = (ViewPager) findViewById(R.id.slideViewPager);
        mDotLayout = (LinearLayout) findViewById(R.id.dotsLayout);

        mSlideViewPager.setAdapter(sliderAdapter);

        addDotsIndicator(0);
        mSlideViewPager.addOnPageChangeListener(viewListener);

        mNextButton = (Button) findViewById(R.id.Next);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSlideViewPager.setCurrentItem(mCurrentPage+1);
            }
        });

        mPrevButton = (Button) findViewById(R.id.Prev);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSlideViewPager.setCurrentItem(mCurrentPage-1);
            }
        });
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPreferences.edit();
    }

    public void addDotsIndicator(int position){
        mDots = new TextView[3];
        mDotLayout.removeAllViews();
        for(int i=0; i < mDots.length;i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(50);
            mDots[i].setTextColor(getResources().getColor(R.color.mainRed));
            mDotLayout.addView(mDots[i]);
        }
        if(mDots.length > 0){
            mDots[position].setTextColor(getResources().getColor(R.color.black));
        }
    }

    @Override
    public void onClick(View view) {

    }
}
