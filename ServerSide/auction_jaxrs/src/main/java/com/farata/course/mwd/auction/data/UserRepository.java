package com.farata.course.mwd.auction.data;

import com.farata.course.mwd.auction.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prozorov on 18/09/14.
 */
public class UserRepository {

    private List<User> users;

    public UserRepository()
    {
        users = new ArrayList<User>();
    }

    public void registerUser(String id) {

       users.add(new User(id,"User: " + id,id + "@auction.com",true));

    }

    public User getUserById(String id) {
        User result = null;
        for (User user : users) {
            if (user.getId().equals(id)) {
                result = user;
            }
        }
        return result;
    }
}
