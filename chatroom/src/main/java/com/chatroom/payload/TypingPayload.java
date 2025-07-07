package com.chatroom.payload;

import java.util.Set;

public class TypingPayload {
    private Set<String> users;

    public TypingPayload() {
    }

    public TypingPayload(Set<String> users) {
        this.users = users;
    }

    public Set<String> getUsers() {
        return users;
    }

    public void setUsers(Set<String> users) {
        this.users = users;
    }
}
