package com.example.snowex01;

public class Friend {
    String id;
    Boolean show;

    public Friend(String id, Boolean show) {
        this.id = id;
        this.show = show;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getShow() {
        return show;
    }

    public void setShow(Boolean show) {
        this.show = show;
    }
}
