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

    public int registerUser() {
        int id = users.size();
        users.add(new User(id,"User: " + String.valueOf(id),String.valueOf(id) + "@auction.com",true));
        return id;
    }

    public User getUserById(int id) {
        User result = null;
        for (User user : users) {
            if (user.getId() == id) {
                result = user;
            }
        }
        return result;
    }
}
