package com.farata.course.mwd.auction.service;

import com.farata.course.mwd.auction.data.DataEngine;
import com.farata.course.mwd.auction.entity.Bid;
import com.farata.course.mwd.auction.entity.BidDTO;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import java.math.BigDecimal;
import java.util.logging.Logger;

@Path("bid")
@Produces("application/json")
public class BidService {

    DataEngine dataEngine;
    NotificationService notificationService;

    private static final Logger log = Logger.getLogger(BidService.class.getName());

    @Inject
    public void setData(DataEngine dataEngine, NotificationService notificationService)
    {
        this.dataEngine = dataEngine;
        this.notificationService = notificationService;
    }

    @GET
    @Path("/{id}/")
    public Bid getBid(@PathParam("id") int id, @Context HttpHeaders headers) {
        return dataEngine.getBidRepository().getBid(id);
    }

    @POST
    @Consumes(value={"application/json"})
    public String placeBid(String input) {

        BidDTO bid = new BidDTO();
        JSONParser parser = new JSONParser();

        Object obj = null;
        try {
            obj = parser.parse(input.toString());
            JSONObject jsonObj = (JSONObject) obj;
            bid.setProductId(Integer.parseInt(jsonObj.get("productId").toString()));
            bid.setUserId(jsonObj.get("userId").toString());
            bid.setDesiredQuantity(Integer.parseInt(jsonObj.get("desiredQuantity").toString()));
            bid.setAmount(BigDecimal.valueOf(Integer.parseInt(jsonObj.get("amount").toString())));

            notificationService.addUserToProduct(bid);
            log.info("User " + bid.getUserId() + " placed a bid " + bid.getProductId());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        sendBidToQueue();
        String response =  dataEngine.getBidRepository().placeBid(bid.getProductId(),
                BigDecimal.valueOf(Integer.parseInt(bid.getAmount().toString())),
                bid.getDesiredQuantity(), bid.getUserId());

        try {
            Object resp = parser.parse(response.toString());
            if(((JSONObject)(((JSONArray)resp).get(0))).get("status").toString().equals("renew"))
                notificationService.notifySubscribers(bid.getProductId(),bid.getAmount());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return response;

    }

    @POST
    @Path("/{productId}/{amount}/{desiredQuantity}/{userId}/")
    public String placeBid(@PathParam("productId") int productId, @PathParam("amount") BigDecimal amount,
                           @PathParam("desiredQuantity") int desiredQuantity, @PathParam("userId") String userId) {

        sendBidToQueue();
        return dataEngine.getBidRepository().placeBid(productId, amount, desiredQuantity, userId);

    }



    private void sendBidToQueue(){

        //try (JMSContext context =
        //       connectionFactory.createContext(DEFAULT_USERNAME, DEFAULT_PASSWORD)) {
        //    log.info("\n Sending Hello Bid message from BidService to the queue");
        //
        //    JMSProducer producer = context.createProducer();
        //
        //    producer.send(testQueue, "Hello Bid!");
        //}
    }
}
