package businesslogic.event;

import javafx.collections.ObservableList;

public class EventManager {
    public ObservableList<EventInfo> getEventInfo() {
        return EventInfo.loadAllEventInfo();
    }

    public EventInfo fakeEvent(String name){
        return new EventInfo(name);
    }

    public ServiceInfo fakeService(String name){
        return new ServiceInfo(name);
    }
}
