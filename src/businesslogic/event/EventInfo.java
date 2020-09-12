package businesslogic.event;

import businesslogic.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EventInfo implements EventItemInfo {
    private int id;
    private String name;
    private Date dateStart;
    private Date dateEnd;
    private int participants;
    private User organizer;

    private ObservableList<ServiceInfo> services;

    public EventInfo(String name) {
        this.name = name;
        id = 0;
    }
    public EventInfo(int id) {
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

    public String getName(){
        return name;
    }

    public ObservableList<ServiceInfo> getServices() {
        return FXCollections.unmodifiableObservableList(this.services);
    }

    public String toString() {
        return name /*+ ": " + dateStart + "-" + dateEnd + ", " + participants + " pp. (" + organizer.getUserName() + ")"*/;
    }

    public boolean isInCharge(User user) {
        return true;  //DA SISTEMARE
    }

    public boolean isService(ServiceInfo ser) {
        return true; //DA SISTEMARE
    }



    // STATIC METHODS FOR PERSISTENCE

    public static ObservableList<EventInfo> loadAllEventInfo() {
        ObservableList<EventInfo> all = FXCollections.observableArrayList();
        String query = "SELECT * FROM Events WHERE true";
        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                String n = rs.getString("name");
                EventInfo e = new EventInfo(n);
                e.id = rs.getInt("id");
                e.dateStart = rs.getDate("date_start");
                e.dateEnd = rs.getDate("date_end");
                e.participants = rs.getInt("expected_participants");
                int org = rs.getInt("organizer_id");
                e.organizer = User.loadUserById(org);
                all.add(e);
            }
        });

        for (EventInfo e : all) {
            e.services = ServiceInfo.loadServiceInfoForEvent(e.id);
        }
        return all;
    }

    public static EventInfo loadEventInfo(int id_event){
        EventInfo event = new EventInfo(id_event);

        String query = "SELECT * FROM events WHERE id =" +id_event;
        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                event.name = rs.getString("name");
                event.id = rs.getInt("id");
                event.dateStart = rs.getDate("date_start");
                event.dateEnd = rs.getDate("date_end");
                event.participants = rs.getInt("expected_participants");
                int org = rs.getInt("organizer_id");
                event.organizer = User.loadUserById(org);
            }
        });

        return event;
    }
}
