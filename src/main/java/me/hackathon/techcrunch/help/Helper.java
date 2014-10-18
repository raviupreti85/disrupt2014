package me.hackathon.techcrunch.help;

import me.hackathon.techcrunch.user.User;

/**
 * Created by raviupreti85 on 18/10/2014.
 */
public class Helper {
    private User user;

    public Helper(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
