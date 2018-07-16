package com.ritvi.kaajneeti.fragment.home;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.Util.ToastClass;
import com.ritvi.kaajneeti.fragment.wallet.ContributeFragment;
import com.ritvi.kaajneeti.fragment.wallet.SelectUserForContributionFragment;
import com.ritvi.kaajneeti.fragmentcontroller.FragmentController;
import com.ritvi.kaajneeti.pojo.ResponsePOJO;
import com.ritvi.kaajneeti.pojo.payment.TotalAmountPOJO;
import com.ritvi.kaajneeti.webservice.ResponseCallBack;
import com.ritvi.kaajneeti.webservice.WebServiceBase;
import com.ritvi.kaajneeti.webservice.WebServiceBaseResponse;
import com.ritvi.kaajneeti.webservice.WebServicesCallBack;
import com.ritvi.kaajneeti.webservice.WebServicesUrls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;

public class FavoriteFragment extends FragmentController {

    @BindView(R.id.ll_add_point)
    LinearLayout ll_add_point;
    @BindView(R.id.ll_send_points)
    LinearLayout ll_send_points;
    @BindView(R.id.tv_points)
    TextView tv_points;
    TotalAmountPOJO totalAmountPOJO;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_rewards, container, false);
        setUpView(getActivity(), this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ll_add_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPointsLogs(true);
            }
        });

        ll_send_points.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityManager.startFragmentForResult(R.id.frame_home, FavoriteFragment.this, new SelectUserForContributionFragment(), 101);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPointsLogs(false);
            }
        });

    }

    boolean is_initialize=false;
    public void initializeFragment(){
        if(!is_initialize) {
            getPointsLogs(false);
            is_initialize=true;
        }
    }

    public void addPoints(final double wallet_amount) {
        final Dialog dialog1 = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog);
        dialog1.setCancelable(true);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.dialog_add_points);
        dialog1.setTitle("Convert Points");
        dialog1.show();
        dialog1.setCancelable(true);
        Window window = dialog1.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        Button btn_convert = dialog1.findViewById(R.id.btn_convert);
        final EditText et_wallet_amount = dialog1.findViewById(R.id.et_wallet_amount);
        TextView tv_wallet_amount = dialog1.findViewById(R.id.tv_wallet_amount);
        ImageView iv_close = dialog1.findViewById(R.id.iv_close);

        tv_wallet_amount.setText("Rs. " + wallet_amount);

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });

        btn_convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_wallet_amount.getText().toString().length() > 0) {
                    try {
                        double balance = Double.parseDouble(et_wallet_amount.getText().toString());
                        double remaining_balance = wallet_amount;
                        if (balance >= remaining_balance) {
                            ToastClass.showShortToast(getActivity().getApplicationContext(), "Entered Amount should not be greater than balance Amount");
                        } else {
                            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                            nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
                            nameValuePairs.add(new BasicNameValuePair("rupee", String.valueOf(balance)));
                            new WebServiceBase(nameValuePairs, getActivity(), new WebServicesCallBack() {
                                @Override
                                public void onGetMsg(String apicall, String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        if (jsonObject.optString("status").equals("success")) {
                                            ToastClass.showShortToast(getActivity().getApplicationContext(), "Rupees Converted");
                                            dialog1.dismiss();
                                            getPointsLogs(false);
                                        } else {
                                            ToastClass.showShortToast(getActivity().getApplicationContext(), jsonObject.optString("message"));
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, "CONVERT_POINT_TO_RUPEE", true).execute(WebServicesUrls.CONVERT_RUPEES_TO_POINTS);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    public void getPointsLogs(final boolean for_transfer) {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("user_profile_id", Constants.userProfilePOJO.getUserProfileId()));
        new WebServiceBaseResponse<TotalAmountPOJO>(nameValuePairs, getActivity(), new ResponseCallBack<TotalAmountPOJO>() {
            @Override
            public void onGetMsg(ResponsePOJO<TotalAmountPOJO> responsePOJO) {
                swipeRefreshLayout.setRefreshing(false);
                try {
                    if (responsePOJO.isSuccess()) {
                        totalAmountPOJO = responsePOJO.getResult();
                        tv_points.setText(String.valueOf(responsePOJO.getResult().getMyPointBalance()));
                        if(for_transfer){
                            try {
                                if (totalAmountPOJO != null) {
                                    addPoints(Double.parseDouble(totalAmountPOJO.getMyWalletBalance()));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, TotalAmountPOJO.class, "GET_TOTAL_AMOUNT", true).execute(WebServicesUrls.GET_MY_TOTAL_AMOUNT);
    }
}
