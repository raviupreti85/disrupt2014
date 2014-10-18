package me.hackathon.techcrunch;

import me.hackathon.techcrunch.event.Event;
import me.hackathon.techcrunch.event.HelpEventManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class EventWebSocketController {

    @Autowired
    private HelpEventManager helpEventManager;

    @MessageMapping("/websocket/events")
    @SendTo("/topic/events")
    public Event events() throws Exception {
        return helpEventManager.getAllActiveEvents().get(0);
    }

}
