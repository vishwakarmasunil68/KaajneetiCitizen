package com.ritvi.kaajneeti.pojo.analyze;

import java.io.Serializable;

public class AnalyzeFeedType implements Serializable{
    String feed_type;
    String feed_count;
    int feed_drawable;

    public String getFeed_type() {
        return feed_type;
    }

    public void setFeed_type(String feed_type) {
        this.feed_type = feed_type;
    }

    public String getFeed_count() {
        return feed_count;
    }

    public void setFeed_count(String feed_count) {
        this.feed_count = feed_count;
    }

    public int getFeed_drawable() {
        return feed_drawable;
    }

    public void setFeed_drawable(int feed_drawable) {
        this.feed_drawable = feed_drawable;
    }
}
