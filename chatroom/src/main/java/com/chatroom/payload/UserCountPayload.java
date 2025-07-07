package com.chatroom.payload;

public class UserCountPayload {
    private int count;

    public UserCountPayload() {}

    public UserCountPayload(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
