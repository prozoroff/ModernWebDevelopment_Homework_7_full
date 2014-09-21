package com.farata.course.mwd.auction.service;

import com.farata.course.mwd.auction.data.DataEngine;
import com.farata.course.mwd.auction.entity.Bid;
import com.farata.course.mwd.auction.entity.BidDTO;
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

    private static final Logger log = Logger.getLogger(BidService.class.getName());

    @Inject
    public void setDataEngine(DataEngine dataEngine) {
        this.dataEngine = dataEngine;
    }

    // Set up all the default values
    private static final String DEFAULT_MESSAGE = "Hello, World!";
    private static final String DEFAULT_CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
    private static final String DEFAULT_DESTINATION = "jms/queue/test";
    private static final String DEFAULT_MESSAGE_COUNT = "57";
    private static final String DEFAULT_USERNAME = "quickstartUser";
    private static final String DEFAULT_PASSWORD = "quickstartPwd1!";
    private static final String INITIAL_CONTEXT_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";
    private static final String PROVIDER_URL = "http-remoting://127.0.0.1:8080";

    //@Resource(lookup ="java:/ConnectionFactory")
    //ConnectionFactory connectionFactory;
    //
    //@Resource(lookup = "queue/test")
    //Queue testQueue;


    @GET
    @Path("/{id}/")
    public Bid getBid(@PathParam("id") int id, @Context HttpHeaders headers) {
        return dataEngine.getBidRepository().getBid(id);
    }

    @POST
    @Consumes(value={"application/json"})
    public String placeBid(String input) {
        //{"productId":1,"amount":1,"desiredQuantity":1,"userId":1}
        BidDTO bid = new BidDTO();
        JSONParser parser = new JSONParser();

        Object obj = null;
        try {
            obj = parser.parse(input.toString());
            JSONObject jsonObj = (JSONObject) obj;
            bid.setProductId(Integer.parseInt(jsonObj.get("productId").toString()));
            bid.setUserId(Integer.parseInt(jsonObj.get("userId").toString()));
            bid.setDesiredQuantity(Integer.parseInt(jsonObj.get("desiredQuantity").toString()));
            bid.setAmount(BigDecimal.valueOf(Integer.parseInt(jsonObj.get("amount").toString())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        sendBidToQueue();
        String response =  dataEngine.getBidRepository().placeBid(bid.getProductId(),
                BigDecimal.valueOf(Integer.parseInt(bid.getAmount().toString())),
                bid.getDesiredQuantity(), bid.getUserId());

        return response;

    }

    @POST
    @Path("/{productId}/{amount}/{desiredQuantity}/{userId}/")
    public String placeBid(@PathParam("productId") int productId, @PathParam("amount") BigDecimal amount,
                           @PathParam("desiredQuantity") int desiredQuantity, @PathParam("userId") int userId) {

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
