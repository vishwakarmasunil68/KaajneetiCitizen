package com.ritvi.kaajneeti.Util;


import com.ritvi.kaajneeti.pojo.user.UserProfilePOJO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunil on 16-11-2017.
 */

public class Constants {

    //start activity constants...
    public static final int ACTIVITY_TAG_PEOPLE = 101;
    public static final int ACTIVITY_LOCATION= 102;


    // Access permission
    public static final int ACCESS_CAMERA=2001;
    public static final int READ_CONTACTS=2002;
    public static final int ACCESS_STORAGE=2003;
    public static final int ACCESS_LOCATION=2004;
    public static final int READ_SMS=2005;


    public static final String LOGIN_TYPE="1";

    public static final String ENTER_MOBILE_REGISTRATION_TYPE="registration_type";
    public static final String ENTER_MOBILE_LOGIN_WITH_OTP_TYPE="login_with_otp";

    public static final int GENDER_DEFAULT=0;
    public static final int GENDER_MALE=1;
    public static final int GENDER_FEMALE=2;
    public static final int GENDER_OTHER=3;

    public static final int COMPLAINT_SELF=1;
    public static final int COMPLAINT_GROUP=2;
    public static final int COMPLAINT_OTHER=3;

    public static final int USER_TYPE_NONE=0;

    public static UserProfilePOJO userProfilePOJO=null;

    //event attachments
    public static final String EVENT_IMAGE_ATTACH="event_image_attach";
    public static final String EVENT_VIDEO_ATTACH="event_video_attach";

    //api constants
    public static final String API_SUCCESS = "success";
    public static final String API_STATUS = "status";
    public static final String API_FAILED = "failed";

    //Login screen constants
    public static final String CALL_LOGIN_API = "call_login_api";
    public static final String CALL_UPLOAD_SOCIAL_DATA = "call_upload_social_data";

    //Login with otp screen contants
    public static final String CALL_LOGIN_OTP = "call_login_otp";

    //MpinActivity
    public static final String CALL_MPIN_SET = "call_mpin_set";

    //RegistrationActivity
    public static final String CALL_REGISTER_API = "call_register_api";

    //OTPVerificationActivity
    public static final String CALL_OTP_VERIFIED = "call_otp_verified";

    public static final String PAYMENT_FEED_POINT="point";
    public static final String PAYMENT_FEED_MONEY="payment";

    //getstate list array
    public static List<String> setStateList() {
        List<String> stateList=new ArrayList<>();
        stateList.add("Andaman and Nicobar Islands");
        stateList.add("Andhra Pradesh");
        stateList.add("Arunachal Pradesh");
        stateList.add("Assam");
        stateList.add("Bihar");
        stateList.add("Chandigarh");
        stateList.add("Chhattisgarh");
        stateList.add("Dadra and Nagar Haveli");
        stateList.add("Daman and Diu");
        stateList.add("National Capital Territory of Delhi");
        stateList.add("Goa");
        stateList.add("Gujarat");
        stateList.add("Haryana");
        stateList.add("Himachal Pradesh");
        stateList.add("Jammu and Kashmir");
        stateList.add("Jharkhand");
        stateList.add("Karnataka");
        stateList.add("Kerala");
        stateList.add("Lakshadweep");
        stateList.add("Madhya Pradesh");
        stateList.add("Maharashtra");
        stateList.add("Manipur");
        stateList.add("Meghalaya");
        stateList.add("Mizoram");
        stateList.add("Nagaland");
        stateList.add("Odisha");
        stateList.add("Puducherry");
        stateList.add("Punjab");
        stateList.add("Rajasthan");
        stateList.add("Sikkim");
        stateList.add("Tamil Nadu");
        stateList.add("Telangana");
        stateList.add("Tripura");
        stateList.add("Uttar Pradesh");
        stateList.add("Uttarakhand");
        stateList.add("West Bengal");

        return stateList;
    }


    public static final String SEARCH_IN_POST="post";
    public static final String SEARCH_IN_PEOPLE="people";
    public static final String SEARCH_IN_EVENT="event";
    public static final String SEARCH_IN_POLL="poll";
    public static final String SEARCH_IN_COMPLAINT="complaint";
    public static final String SEARCH_IN_SUGGESTION="suggestion";


    public static final String SEARCH_LOCATION="search_location";
    public static final String SEARCH_WORK="search_work";
    public static final String SEARCH_QUALIFICATION="search_qualification";


}
