package com.ritvi.kaajneeti.activity.home;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.activity.express.ExpressActivity;
import com.ritvi.kaajneeti.adapter.ViewPagerAdapter;
import com.ritvi.kaajneeti.fragment.home.AnalyzeFragment;
import com.ritvi.kaajneeti.fragment.home.ConnectFragment;
import com.ritvi.kaajneeti.fragment.home.FavoriteFragment;
import com.ritvi.kaajneeti.fragment.home.HomeFragment;
import com.ritvi.kaajneeti.view.CustomViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.viewPager)
    CustomViewPager viewPager;
    @BindView(R.id.iv_home)
    ImageView iv_home;
    @BindView(R.id.iv_connect)
    ImageView iv_connect;
    @BindView(R.id.iv_analyze)
    ImageView iv_analyze;
    @BindView(R.id.iv_favorite)
    ImageView iv_favorite;
    @BindView(R.id.cv_express)
    ImageView cv_express;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        setupViewPager(viewPager);
        viewPager.setPagingEnabled(false);

        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });
        iv_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });
        iv_analyze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
            }
        });
        iv_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(3);
            }
        });

        cv_express.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ExpressActivity.class));
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {

        HomeFragment homeFragment = new HomeFragment();
        ConnectFragment connectFragment = new ConnectFragment();
        AnalyzeFragment analyzeFragment = new AnalyzeFragment();
        FavoriteFragment favoriteFragment = new FavoriteFragment();

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(homeFragment, "Rural");
        adapter.addFrag(connectFragment, "Urban");
        adapter.addFrag(analyzeFragment, "Urban");
        adapter.addFrag(favoriteFragment, "Urban");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setFooterTint(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    public void setFooterTint(int position) {
        iv_home.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.default_tint_color), android.graphics.PorterDuff.Mode.MULTIPLY);
        iv_connect.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.default_tint_color), android.graphics.PorterDuff.Mode.MULTIPLY);
        iv_analyze.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.default_tint_color), android.graphics.PorterDuff.Mode.MULTIPLY);
        iv_favorite.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.default_tint_color), android.graphics.PorterDuff.Mode.MULTIPLY);

        switch (position) {
            case 0:
                iv_home.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
                break;
            case 1:
                iv_connect.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
                break;
            case 2:
                iv_analyze.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
                break;
            case 3:
                iv_favorite.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
                break;
        }

    }
}
