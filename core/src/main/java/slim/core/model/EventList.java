package slim.core.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Container class to save all events in a list
 *
 * @author Robert Wolfinger
 */
@XmlRootElement(name = "events")
@XmlAccessorType(XmlAccessType.FIELD)
public class EventList {

    @XmlElement(name = "event")
    private List<Event> mEvents = new ArrayList<>();

    public List<Event> getEvents() {
        return mEvents;
    }

    public void setEvents(List<Event> events) {
        mEvents = events;
    }

    public void add(Event event) {
        mEvents.add(event);
    }

    public void remove(Event event) {
        mEvents.remove(event);
    }

    public boolean isEmpty() {
        return mEvents.isEmpty();
    }
}
