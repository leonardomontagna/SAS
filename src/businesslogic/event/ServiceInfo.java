package businesslogic.event;

import businesslogic.menu.Menu;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

public class ServiceInfo implements EventItemInfo {
    private int id;
    private String name;
    private Date date;
    private Time timeStart;
    private Time timeEnd;
    private int participants;
    private Menu menu;
    int id_menu_approved;

    public ServiceInfo(String name) {
        this.name = name;
    }
    public ServiceInfo(int id) {
        this.id = id;
    }


    public String toString() {
        return name /*+ ": " + date + " (" + timeStart + "-" + timeEnd + "), " + participants + " pp."*/;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public int getId_menu_approved() {
        return id_menu_approved;
    }

    // STATIC METHODS FOR PERSISTENCE

    public static ObservableList<ServiceInfo> loadServiceInfoForEvent(int event_id) {
        ObservableList<ServiceInfo> result = FXCollections.observableArrayList();
        String query = "SELECT * " +
                "FROM Services WHERE event_id = " + event_id;
        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                String s = rs.getString("name");
                ServiceInfo serv = new ServiceInfo(s);
                serv.id = rs.getInt("id");
                serv.date = rs.getDate("service_date");
                serv.timeStart = rs.getTime("time_start");
                serv.timeEnd = rs.getTime("time_end");
                serv.participants = rs.getInt("expected_participants");
                serv.id_menu_approved = rs.getInt("approved_menu_id");
                result.add(serv);
            }
        });

        return result;
    }

    public static ServiceInfo loadServiceInfo(int id_service){
        ServiceInfo service = new ServiceInfo(id_service);

        String query = "SELECT * FROM Services WHERE id = " + id_service;

        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                service.name = rs.getString("name");
                service.date = rs.getDate("service_date");
                service.timeStart = rs.getTime("time_start");
                service.timeEnd = rs.getTime("time_end");
                service.participants = rs.getInt("expected_participants");
                service.id_menu_approved = rs.getInt("approved_menu_id");
            }
        });

        return service;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName(){
        return name;
    }
}
