package com.ritvi.kaajneeti.fragment.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.vision.text.Line;
import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.Util.SetViews;
import com.ritvi.kaajneeti.activity.home.HomeActivity;
import com.ritvi.kaajneeti.fragment.setting.SettingFragment;
import com.ritvi.kaajneeti.fragment.user.UserProfileFragment;
import com.ritvi.kaajneeti.fragment.wallet.ContributeFragment;
import com.ritvi.kaajneeti.fragment.wallet.WalletFragment;
import com.ritvi.kaajneeti.fragmentcontroller.FragmentController;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class AllMenuFragment extends FragmentController {

    @BindView(R.id.ll_logout)
    LinearLayout ll_logout;
    @BindView(R.id.ll_setting)
    LinearLayout ll_setting;
    @BindView(R.id.ll_contribute)
    LinearLayout ll_contribute;
    @BindView(R.id.ll_wallet)
    LinearLayout ll_wallet;
    @BindView(R.id.cv_profile_pic)
    CircleImageView cv_profile_pic;
    @BindView(R.id.tv_profile_name)
    TextView tv_profile_name;
    @BindView(R.id.ll_user_profile)
    LinearLayout ll_user_profile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_all_menu,container,false);
        setUpView(getActivity(),this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SetViews.setProfilePhoto(getActivity().getApplicationContext(), Constants.userProfilePOJO.getProfilePhotoPath(), cv_profile_pic);
        SetViews.setProfileName(Constants.userProfilePOJO.getFirstName() + " " + Constants.userProfilePOJO.getLastName(), tv_profile_name);
        ll_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity() instanceof HomeActivity){
                    HomeActivity homeActivity= (HomeActivity) getActivity();
                    homeActivity.showLogoutDialog();
                }
            }
        });

        ll_user_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProfile();
            }
        });

        ll_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity() instanceof HomeActivity){
                    HomeActivity homeActivity= (HomeActivity) getActivity();
                    homeActivity.replaceFragmentinFrameHome(new SettingFragment(),"SettingFragment");
                }
            }
        });

        ll_contribute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityManager.startFragment(R.id.frame_home,new ContributeFragment());
            }
        });
        ll_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityManager.startFragmentForResult(R.id.frame_home,AllMenuFragment.this,new WalletFragment(),0);
            }
        });
    }

    public void showProfile(){
        if(getActivity() instanceof HomeActivity){
            HomeActivity homeActivity= (HomeActivity) getActivity();
            UserProfileFragment userProfileFragment=new UserProfileFragment();
            Bundle bundle=new Bundle();
            bundle.putSerializable("userProfile",Constants.userProfilePOJO);
            userProfileFragment.setArguments(bundle);

            homeActivity.replaceFragmentinFrameHome(userProfileFragment,"UserProfileFragment");
        }
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {

    }
}
