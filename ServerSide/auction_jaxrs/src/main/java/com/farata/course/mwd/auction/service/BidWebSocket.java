package com.farata.course.mwd.auction.service;

/**
 * Created by prozorov on 26/09/14.
 */

import com.farata.course.mwd.auction.data.DataEngine;

import javax.inject.Inject;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.logging.Logger;


@ServerEndpoint("/ws")
public class BidWebSocket {
    private static final Logger log = Logger.getLogger(BidWebSocket.class.getName());

    @Inject
    private BidService bidService;

    DataEngine dataEngine;
    NotificationService notificationService;

    @Inject
    public void setDataEngine(DataEngine dataEngine,NotificationService notificationService) {
        this.dataEngine = dataEngine;
        this.notificationService = notificationService;
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) throws IOException {
        session.getBasicRemote().sendText(session.getId());
        dataEngine.getUserRepository().registerUser(session.getId());
        try {
            notificationService.subscribe(session.getId(), (productId, price) -> sendBidUpdate(productId, price, session));
        }
        catch (Exception ex) {
            String test = ex.getMessage();
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        notificationService.unsubscribe(session.getId());
    }

    void sendBidUpdate(Integer productId, BigDecimal price, Session session) {
        try {
            session.getBasicRemote().sendText(productId + ":" + price.setScale(2, BigDecimal.ROUND_HALF_UP));
            log.info("User " + session.getId() + " notified about " + productId + " changes");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

