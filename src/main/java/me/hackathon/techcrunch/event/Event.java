package me.hackathon.techcrunch.event;

import com.google.common.collect.Sets;
import me.hackathon.techcrunch.help.HelpRequest;
import me.hackathon.techcrunch.user.User;

import java.util.Collection;
import java.util.Set;

/**
 * Created by raviupreti85 on 18/10/2014.
 */
public class Event {

    private String id;
    private HelpRequest helpRequest;
    private Set<User> helpers;
    private EventStatus status;
    private Location location;

    public Event() {
    }

    public Event(String id, HelpRequest helpRequest) {
        this.id = id;
        this.helpRequest = helpRequest;
        this.location = helpRequest.getLocation();
        this.helpers = Sets.newHashSet();
        this.status = EventStatus.ACTIVE;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public HelpRequest getHelpRequest() {
        return helpRequest;
    }

    public void setHelpRequest(HelpRequest helpRequest) {
        this.helpRequest = helpRequest;
    }

    public void addHelper(User helper) {
        helpers.add(helper);
    }

    public void removeHelper(User helper) {
        helpers.remove(helper);
    }

    public Collection<User> getHelpers() {
        return helpers;
    }

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void markResolved() {
        this.status = EventStatus.RESOLVED;
    }

    public void markCancelled() {
        this.status = EventStatus.CANCEL;
    }

    private enum EventStatus {
        ACTIVE, CANCEL, RESOLVED;
    }
}
