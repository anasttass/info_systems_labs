package ru.stud.event;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import ru.stud.websocket.HumanBeingWebSocketHandler;

@Component
public class HumanBeingChangedListener {

    private final HumanBeingWebSocketHandler webSocketHandler;

    public HumanBeingChangedListener(
            HumanBeingWebSocketHandler webSocketHandler
    ) {
        this.webSocketHandler = webSocketHandler;
    }

    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handle(HumanBeingChangedEvent event) {
        webSocketHandler.notifyClients();
    }
}