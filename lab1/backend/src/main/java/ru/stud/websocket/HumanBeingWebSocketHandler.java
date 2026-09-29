package ru.stud.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class HumanBeingWebSocketHandler
        extends TextWebSocketHandler {

    private final Set<WebSocketSession> sessions =
            ConcurrentHashMap.newKeySet();

    @Override
    public void afterConnectionEstablished(
            WebSocketSession session
    ) {
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(
            WebSocketSession session,
            org.springframework.web.socket.CloseStatus status
    ) {
        sessions.remove(session);
    }

    public void notifyClients() {
        TextMessage message =
                new TextMessage("HUMAN_BEINGS_CHANGED");

        sessions.removeIf(session -> {
            if (!session.isOpen()) {
                return true;
            }

            try {
                session.sendMessage(message);
                return false;
            } catch (IOException e) {
                return true;
            }
        });
    }
}