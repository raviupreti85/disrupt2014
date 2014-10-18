package me.hackathon.techcrunch.user;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by raviupreti85 on 18/10/2014.
 */

@Service
public class UserService {

    private static Map<String, User> USERS = Maps.newHashMap();

    static {
        USERS.put("1", new User("1"));
        USERS.put("2", new User("2"));
        USERS.put("3", new User("3"));
        USERS.put("4", new User("4"));
    }

    public Optional<User> getUser(String id) {
        return Optional.fromNullable(USERS.get(id));
    }
}
