package com.ritvi.kaajneeti.pojo.poll;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by sunil on 30-03-2018.
 */

public class PollAnsPOJO implements Serializable {

    @SerializedName("PollAnswerId")
    @Expose
    private String pollAnswerId;
    @SerializedName("PollId")
    @Expose
    private String pollId;
    @SerializedName("PollAnswer")
    @Expose
    private String pollAnswer;
    @SerializedName("PollAnswerImage")
    @Expose
    private String pollAnswerImage;
    @SerializedName("PollAnswerStatus")
    @Expose
    private String pollAnswerStatus;
    @SerializedName("TotalAnswerdMe")
    @Expose
    private Integer totalAnswerdMe;
    @SerializedName("MeAnsweredYesNo")
    @Expose
    private Integer meAnsweredYesNo;

    public String getPollAnswerId() {
        return pollAnswerId;
    }

    public void setPollAnswerId(String pollAnswerId) {
        this.pollAnswerId = pollAnswerId;
    }

    public String getPollId() {
        return pollId;
    }

    public void setPollId(String pollId) {
        this.pollId = pollId;
    }

    public String getPollAnswer() {
        return pollAnswer;
    }

    public void setPollAnswer(String pollAnswer) {
        this.pollAnswer = pollAnswer;
    }

    public String getPollAnswerImage() {
        return pollAnswerImage;
    }

    public void setPollAnswerImage(String pollAnswerImage) {
        this.pollAnswerImage = pollAnswerImage;
    }

    public String getPollAnswerStatus() {
        return pollAnswerStatus;
    }

    public void setPollAnswerStatus(String pollAnswerStatus) {
        this.pollAnswerStatus = pollAnswerStatus;
    }

    public Integer getTotalAnswerdMe() {
        return totalAnswerdMe;
    }

    public void setTotalAnswerdMe(Integer totalAnswerdMe) {
        this.totalAnswerdMe = totalAnswerdMe;
    }

    public Integer getMeAnsweredYesNo() {
        return meAnsweredYesNo;
    }

    public void setMeAnsweredYesNo(Integer meAnsweredYesNo) {
        this.meAnsweredYesNo = meAnsweredYesNo;
    }
}
