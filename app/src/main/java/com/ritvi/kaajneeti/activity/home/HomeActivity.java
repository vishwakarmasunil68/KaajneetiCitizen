package com.ritvi.kaajneeti.activity.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import com.bumptech.glide.Glide;
import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.Util.Pref;
import com.ritvi.kaajneeti.Util.StringUtils;
import com.ritvi.kaajneeti.activity.SplashActivity;
import com.ritvi.kaajneeti.activity.express.ExpressActivity;
import com.ritvi.kaajneeti.activity.loginregistration.LoginActivity;
import com.ritvi.kaajneeti.adapter.ViewPagerAdapter;
import com.ritvi.kaajneeti.fragment.home.AllMenuFragment;
import com.ritvi.kaajneeti.fragment.home.AnalyzeFragment;
import com.ritvi.kaajneeti.fragment.home.ConnectFragment;
import com.ritvi.kaajneeti.fragment.home.FavoriteFragment;
import com.ritvi.kaajneeti.fragment.home.HomeFragment;
import com.ritvi.kaajneeti.fragment.home.MyConnectionFragment;
import com.ritvi.kaajneeti.fragment.search.SearchFragment;
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
    @BindView(R.id.iv_ham)
    ImageView iv_ham;
    @BindView(R.id.ll_sub_menu)
    LinearLayout ll_sub_menu;
    @BindView(R.id.cv_express)
    ImageView cv_express;
    @BindView(R.id.ll_search)
    LinearLayout ll_search;

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
        ll_sub_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(4);
            }
        });

        cv_express.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ExpressActivity.class));
            }
        });
        ll_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragmentinFrameHome(new SearchFragment(), "SearchFragment");
            }
        });

//        ll_sub_menu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final PopupMenu menu = new PopupMenu(HomeActivity.this, v);
//
//                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem menuitem) {
//                        switch (menuitem.getItemId()) {
//                            case R.id.popup_logout:
//                                    showLogoutDialog();
//                                break;
//                        }
//                        return false;
//                    }
//                });
//                menu.inflate(R.menu.home_sub_menu);
//                menu.show();
//            }
//        });
    }

    public void showLogoutDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Logout");
        alertDialog.setMessage("Do you want to logout?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Pref.SetBooleanPref(getApplicationContext(), StringUtils.IS_LOGIN, false);
                Pref.SetBooleanPref(getApplicationContext(), StringUtils.IS_PROFILE_COMPLETED, false);
                Pref.SetBooleanPref(getApplicationContext(), StringUtils.IS_PROFILE_SKIPPED, false);
                Pref.SetIntPref(getApplicationContext(), StringUtils.USER_TYPE, Constants.USER_TYPE_NONE);
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                finishAffinity();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void setupViewPager(ViewPager viewPager) {

        HomeFragment homeFragment = new HomeFragment();
        final MyConnectionFragment connectFragment = new MyConnectionFragment();
        final AnalyzeFragment analyzeFragment = new AnalyzeFragment();
        FavoriteFragment favoriteFragment = new FavoriteFragment();
        AllMenuFragment allMenuFragment = new AllMenuFragment();

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(homeFragment, "Rural");
        adapter.addFrag(connectFragment, "Urban");
        adapter.addFrag(analyzeFragment, "Urban");
        adapter.addFrag(favoriteFragment, "Urban");
        adapter.addFrag(allMenuFragment, "allmenu");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setFooterTint(position);
                if (position == 1) {
                    connectFragment.initializeFragment();
                } else if (position == 2) {
                    analyzeFragment.initializeFragment();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    public void setFooterTint(int position) {

        iv_home.setImageResource(R.drawable.ic_home);
        iv_connect.setImageResource(R.drawable.ic_connect_people);
        iv_analyze.setImageResource(R.drawable.ic_analyze);
        iv_favorite.setImageResource(R.drawable.ic_favorite);
        iv_ham.setImageResource(R.drawable.ic_ham);
        switch (position) {
            case 0:
                iv_home.setImageResource(R.drawable.ic_home_filled);
                break;
            case 1:
                iv_connect.setImageResource(R.drawable.ic_connect_people_filled);
                break;
            case 2:
                iv_analyze.setImageResource(R.drawable.ic_analyze_filled);
                break;
            case 3:
                iv_favorite.setImageResource(R.drawable.ic_favorite_filled);
                break;
            case 4:
                iv_ham.setImageResource(R.drawable.ic_ham_filled);
                break;
        }
    }

    public void replaceFragmentinFrameHome(Fragment fragment, String fragment_name) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_home, fragment)
                .addToBackStack(fragment_name)
                .commit();
    }

    public void addFragmentinFrameHome(Fragment fragment, String fragment_name) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame_home, fragment)
                .addToBackStack(fragment_name)
                .commit();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

}
