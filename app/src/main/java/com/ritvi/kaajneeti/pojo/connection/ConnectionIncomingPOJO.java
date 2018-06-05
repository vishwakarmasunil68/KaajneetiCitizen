package com.ritvi.kaajneeti.pojo.connection;

import com.google.gson.annotations.SerializedName;
import com.ritvi.kaajneeti.pojo.allfeeds.FeedPOJO;
import com.ritvi.kaajneeti.pojo.user.UserProfilePOJO;

import java.io.Serializable;
import java.util.List;

public class ConnectionIncomingPOJO implements Serializable{
    @SerializedName("RequestConnection")
    List<FeedPOJO> requestConnectionPOJOS;
    @SerializedName("TotalRequest")
    int TotalRequest;
    @SerializedName("Connection")
    List<FeedPOJO> Connection;
    @SerializedName("TotalConnection")
    int TotalConnection;

    public List<FeedPOJO> getRequestConnectionPOJOS() {
        return requestConnectionPOJOS;
    }

    public void setRequestConnectionPOJOS(List<FeedPOJO> requestConnectionPOJOS) {
        this.requestConnectionPOJOS = requestConnectionPOJOS;
    }

    public int getTotalRequest() {
        return TotalRequest;
    }

    public void setTotalRequest(int totalRequest) {
        TotalRequest = totalRequest;
    }

    public List<FeedPOJO> getConnection() {
        return Connection;
    }

    public void setConnection(List<FeedPOJO> connection) {
        Connection = connection;
    }

    public int getTotalConnection() {
        return TotalConnection;
    }

    public void setTotalConnection(int totalConnection) {
        TotalConnection = totalConnection;
    }
}
