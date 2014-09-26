package com.farata.course.mwd.auction.service;

/**
 * Created by prozorov on 26/09/14.
 */

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

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) throws IOException {
        session.getBasicRemote().sendText(session.getId());
        bidService.subscribe(session.getId(), (productId, price) -> sendBidUpdate(productId, price, session));
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        bidService.unsubscribe(session.getId());
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

