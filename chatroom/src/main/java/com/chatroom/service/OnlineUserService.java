package com.chatroom.service;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
public class OnlineUserService {
    private final Set<String> connectedUsers = Collections.synchronizedSet(new HashSet<>());

    public void addUser(String username) {
        connectedUsers.add(username);
    }

    public void removeUser(String username) {
        connectedUsers.remove(username);
    }

    public int count() {
        return connectedUsers.size();
    }

    public Set<String> getUsers() {
        return connectedUsers;
    }
}
