package com.ritvi.kaajneeti.webservice;


import com.ritvi.kaajneeti.pojo.ResponseListPOJO;
import com.ritvi.kaajneeti.pojo.location.LocationResponseListPOJO;
import com.ritvi.kaajneeti.pojo.location.NewLocationPOJO;

import java.util.List;

/**
 * Created by sunil on 29-12-2016.
 */

public interface LocationResponseListCallback {
    public void onGetMsg(LocationResponseListPOJO locationResponseListPOJO);
}
