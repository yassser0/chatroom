package com.chatroom.controller;

import com.chatroom.model.Message;
import com.chatroom.payload.TypingPayload;
import com.chatroom.payload.UserCountPayload;
import com.chatroom.repository.MessageRepository;
import com.chatroom.service.OnlineUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.Instant;
import java.util.Map;

@Controller
public class ChatController {

    @Autowired
    private MessageRepository messageRepo;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private OnlineUserService onlineUserService;

    // Affiche la page HTML via Thymeleaf (chatroom.html dans templates/)
    @GetMapping("/chatroom")
    public String chatfront() {
        return "chatroom";
    }

    // Envoie d’un message à tous les utilisateurs
    @MessageMapping("/chat.send")
    public void sendMessage(@Payload Message message) {
        message.setType("MESSAGE");
        message.setTimestamp(Instant.now());

        messageRepo.save(message);
        messagingTemplate.convertAndSend("/topic/messages", message);
    }

    // Lorsqu’un utilisateur rejoint le chat
    @MessageMapping("/user.join")
    public void join(@Payload Message message) {
        message.setType("JOIN");
        message.setTimestamp(Instant.now());

        onlineUserService.addUser(message.getUsername());

        messageRepo.save(message);
        messagingTemplate.convertAndSend("/topic/messages", message);
        broadcastOnlineCount();
    }

    // Lorsqu’un utilisateur quitte le chat
    @MessageMapping("/user.leave")
    public void leave(@Payload Message message) {
        message.setType("LEAVE");
        message.setTimestamp(Instant.now());

        onlineUserService.removeUser(message.getUsername());

        messageRepo.save(message);
        messagingTemplate.convertAndSend("/topic/messages", message);
        broadcastOnlineCount();
    }

    // Indicateur de frappe en temps réel
    @MessageMapping("/typing")
    public void typing(@Payload Map<String, Object> typingData) {
        messagingTemplate.convertAndSend(
                "/topic/typing",
                new TypingPayload(onlineUserService.getUsers())
        );
    }

    // Met à jour le nombre d’utilisateurs en ligne
    private void broadcastOnlineCount() {
        int count = onlineUserService.count();
        UserCountPayload payload = new UserCountPayload(count);
        messagingTemplate.convertAndSend("/topic/users", payload);
    }
}
