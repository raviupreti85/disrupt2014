package me.hackathon.techcrunch.event;

import com.google.common.base.Optional;
import me.hackathon.techcrunch.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by raviupreti85 on 18/10/2014.
 */
@RestController
@RequestMapping("/event")
public class EventController {

    private HelpEventManager helpEventManager;
    private UserService userService;

    @Autowired
    public EventController(HelpEventManager helpEventManager, UserService userService) {
        this.helpEventManager = helpEventManager;
        this.userService = userService;
    }

    @RequestMapping(value = "/{eventId}", method = RequestMethod.GET)
    public Event getEventById(@PathVariable String eventId) {
        Optional<Event> event = helpEventManager.getEvent(eventId);

        if (event.isPresent()) {
            return event.get();
        }

        return null;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Event> events() throws Exception {
        return helpEventManager.getAllActiveEvents();
    }

}
