package me.hackathon.techcrunch.help;

import me.hackathon.techcrunch.event.Location;

/**
 * Created by raviupreti85 on 18/10/2014.
 */
public class HelpResponse {

    private String userId;
    private String eventId;

    private Location location;

    public HelpResponse() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
