package me.hackathon.techcrunch.event;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;
import me.hackathon.techcrunch.help.HelpRequest;
import me.hackathon.techcrunch.help.HelpResponse;
import me.hackathon.techcrunch.user.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by raviupreti85 on 18/10/2014.
 */

@Service
public class HelpEventManager {

    private static AtomicLong EVENT_ID_GENERATOR = new AtomicLong(1);
    private Map<String, Event> activeEvents = Maps.newHashMap();
    private Map<String, Event> cancelledEvents = Maps.newHashMap();
    private Map<String, Event> resolvedEvents = Maps.newHashMap();

    private Map<String, Event> userEvents = Maps.newHashMap();

    public String createUpdate(HelpRequest helpRequest) {
        if (userEvents.containsKey(helpRequest.getUserId())) {
            Event event = userEvents.get(helpRequest.getUserId());
            event.setLocation(helpRequest.getLocation());
            event.setHelpRequest(helpRequest);
            return event.getId();
        }
        String eventID = String.valueOf(EVENT_ID_GENERATOR.incrementAndGet());
        Event event = new Event(eventID, helpRequest);
        activeEvents.put(eventID, event);
        userEvents.put(helpRequest.getUserId(), event);
        return eventID;
    }

    public Optional<Event> getEvent(String eventId) {
        if (activeEvents.containsKey(eventId)) {
            return Optional.of(activeEvents.get(eventId));
        }

        if (cancelledEvents.containsKey(eventId)) {
            return Optional.of(cancelledEvents.get(eventId));
        }

        return Optional.fromNullable(resolvedEvents.get(eventId));
    }

    public List<Event> getAllActiveEvents() {
        return Lists.newArrayList(activeEvents.values());
    }

    public void cancelEventByUser(String id) {
        Event event = activeEvents.get(id);
        event.markCancelled();
        activeEvents.remove(id);
        cancelledEvents.put(id, event);
    }

    public void addHelper(User user, HelpResponse helpResponse) {
        Event event = activeEvents.get(helpResponse.getEventId());
        event.addHelper(user);
    }

    public void updateHelper(User user, HelpResponse helpResponse) {
        Event event = activeEvents.get(helpResponse.getEventId());
        event.addHelper(user);
    }

    public Optional<Event> search(Location location) {
        for (Event event : activeEvents.values()) {
            if (inDistance(location, event.getLocation())) {
                return Optional.of(event);
            }
        }

        return Optional.absent();
    }

    private boolean inDistance(Location l1, Location l2) {
        LatLng p1 = new LatLng(l1.getLatitude(), l1.getLongitude());
        LatLng p2 = new LatLng(l2.getLatitude(), l2.getLongitude());
        double distance = LatLngTool.distance(p1, p2, LengthUnit.KILOMETER);
        return distance < 2;
    }


}
