package com.farata.course.mwd.auction.service;

import com.farata.course.mwd.auction.data.DataEngine;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Created by prozorov on 18/09/14.
 */

@Path("user")
@Produces("application/json")
public class UserService {

    DataEngine dataEngine;

    @Inject
    public void setDataEngine(DataEngine dataEngine) {
        this.dataEngine = dataEngine;
    }

    @GET
    public String getMyUserId() {
        return "[{\"userId\": " + dataEngine.getUserRepository().registerUser() + "}]";
    }
}
