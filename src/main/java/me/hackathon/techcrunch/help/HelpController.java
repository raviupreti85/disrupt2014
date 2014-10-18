package me.hackathon.techcrunch.help;

import com.google.common.base.Optional;
import me.hackathon.techcrunch.event.Event;
import me.hackathon.techcrunch.event.EventId;
import me.hackathon.techcrunch.event.HelpEventManager;
import me.hackathon.techcrunch.user.User;
import me.hackathon.techcrunch.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by raviupreti85 on 18/10/2014.
 */
@RestController
@RequestMapping("/help")
public class HelpController {

    private HelpEventManager helpEventManager;
    private UserService userService;

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    public HelpController(HelpEventManager helpEventManager, UserService userService) {
        this.helpEventManager = helpEventManager;
        this.userService = userService;
    }

    @RequestMapping(value = "/me", method = RequestMethod.POST, consumes = "application/json")
    public EventId helpMe(@RequestBody HelpRequest helpRequest) {
        Optional<User> helpee = userService.getUser(helpRequest.getUserId());

        String eventId = "1";

        if (helpee.isPresent()) {
            eventId = helpEventManager.createUpdate(helpRequest);
        }

        template.convertAndSend("/topic/events", helpEventManager.getAllActiveEvents());

        return new EventId(eventId);

    }

    @RequestMapping(value = "/me", method = RequestMethod.PUT, consumes = "application/json")
    public EventId helpMeUpdate(@RequestBody HelpRequest helpRequest) {
        Optional<User> helpee = userService.getUser(helpRequest.getUserId());

        if (helpee.isPresent()) {
            return new EventId(helpEventManager.createUpdate(helpRequest));
        }

        template.convertAndSend("/topic/events", helpEventManager.getAllActiveEvents());

        return null;
    }

    @RequestMapping(value = "/me", method = RequestMethod.DELETE)
    public void helpNotNeeded(EventId eventId) {
        helpEventManager.cancelEventByUser(eventId.getEventId());
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
    public EventId helpYouGet(@RequestBody HelpRequest helpRequest) {
        Optional<User> helpee = userService.getUser(helpRequest.getUserId());

        if (helpee.isPresent()) {
            Optional<Event> eventOptional = helpEventManager.search(helpRequest.getLocation());
            if (eventOptional.isPresent()) {
                return new EventId(eventOptional.get().getId());
            }
        }

        return new EventId("-1");

    }

    @RequestMapping(value = "/you", method = RequestMethod.POST, consumes = "application/json")
    public EventId helpYou(@RequestBody HelpResponse helpResponse) {
        Optional<User> helper = userService.getUser(helpResponse.getUserId());

        String eventId = "";

        if (helper.isPresent()) {
            helper.get().setLocation(helpResponse.getLocation());
            helpEventManager.addHelper(helper.get(), helpResponse);
        }

        template.convertAndSend("/topic/events", helpEventManager.getAllActiveEvents());

        return new EventId(eventId);

    }

    @RequestMapping(value = "/you", method = RequestMethod.PUT, consumes = "application/json")
    public Event helpYouUpdate(@RequestBody HelpResponse helpResponse) {
        Optional<User> helper = userService.getUser(helpResponse.getUserId());
        Optional<Event> eventOptional = Optional.absent();
        if (helper.isPresent()) {
            helper.get().setLocation(helpResponse.getLocation());
            helpEventManager.updateHelper(helper.get(), helpResponse);
            eventOptional = helpEventManager.getEvent(helpResponse.getEventId());
        }

        if (eventOptional.isPresent()) {
            template.convertAndSend("/topic/events", helpEventManager.getAllActiveEvents());
            return eventOptional.get();
        }

        return null;
    }


}
