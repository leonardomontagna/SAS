package businesslogic.kitchen;

import businesslogic.event.EventInfo;
import businesslogic.event.ServiceInfo;
import businesslogic.menu.Menu;
import businesslogic.recipe.Recipe;
import businesslogic.shift.Shift;
import businesslogic.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.BatchUpdateHandler;
import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class SummarySheet {
    
    private EventInfo currentEvent;
    private ServiceInfo currentService;
    private User owner;
    private int id;
    private ObservableList<Assignment> assignments;
    private ArrayList<Shift> shiftItems;
    private static Map<Integer, SummarySheet> loadedSheets = FXCollections.observableHashMap();
    static int i = 4;


    public SummarySheet(EventInfo event, ServiceInfo service){
        this.currentEvent = event;
        this.currentService = service;
        this.assignments = FXCollections.observableArrayList();
    }

    public static void saveNewSheet(SummarySheet ss) {
        String sheetInsert = "INSERT INTO catering.Sumsheet (service, event, owner) VALUES (?, ?, ?);";

        int[] result = PersistenceManager.executeBatchUpdate(sheetInsert, 1, new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1, ss.currentService.getId());
                ps.setInt(2, ss.currentEvent.getId());
                ps.setInt(3, ss.owner.getId());
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                if (count == 0) {
                    ss.id = rs.getInt(1);
                }
            }
        });
    }

    public static void saveTaskOrder(SummarySheet ss) {
        String upd = "UPDATE Assignments SET position = ? WHERE id = ?";
        PersistenceManager.executeBatchUpdate(upd, ss.assignments.size(), new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1, batchCount);
                ps.setInt(2, ss.assignments.get(batchCount).getId());
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                // no generated ids to handle
            }
        });
    }

    public boolean isOwner(User user) {
        return user.getId() == this.owner.getId();
    }

    public Assignment addTask(Recipe recipe) {
        Assignment a = new Assignment(recipe);
        assignments.add(a);
        return a;
    }

    public String toString(){
        return "id: " + getId() + "\nevento: " + getCurrentEvent().toString() + "\nservizio: " + getCurrentService().toString() + "\nproprietario: " + getOwnerName();
    }

    //DA IMPLEMENTARE!!!
    public boolean hasAssignment(Assignment assignment){
        return true;
    }

    public int assignmentsSize(){
        return assignments.size();
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setAssignments(ObservableList<Assignment> assignments) {
        this.assignments = assignments;
    }

    public ObservableList<Assignment> getAssignments() {
        return this.assignments;
    }

    public ServiceInfo getCurrentService() {
        return currentService;
    }

    public void setCurrentService(ServiceInfo currentService) {
        this.currentService = currentService;
    }

    public EventInfo getCurrentEvent() {
        return currentEvent;
    }

    public void setCurrentEvent(EventInfo currentEvent) {
        this.currentEvent = currentEvent;
    }

    public int getAssignmentPosition(Assignment a){
        return assignments.indexOf(a);
    }

    public boolean deleteTask(Assignment task){
        for(int i= 0; i<assignmentsSize(); i++){
            if(assignments.get(i).getRecipe().equals(task.getRecipe())){
                assignments.remove(i);
                return true;
            }
        }
        return false;
    }

    //PERSISTENZA
    public static ObservableList<SummarySheet> loadAllSheets() {

        String query = "SELECT * FROM sumsheet WHERE " + true;
        ArrayList<SummarySheet> newSheets = new ArrayList<>();
        ArrayList<Integer> newSids = new ArrayList<>();
        ArrayList<SummarySheet> oldSheets = new ArrayList<>();
        ArrayList<Integer> oldSids = new ArrayList<>();

        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                int id = rs.getInt("id");
                int service_id = rs.getInt("service");
                int event_id = rs.getInt("event");
                if (loadedSheets.containsKey(id)) {
                    SummarySheet sheet = loadedSheets.get(id);
                    oldSids.add(rs.getInt("owner"));
                    oldSheets.add(sheet);
                } else {
                    ServiceInfo service = new ServiceInfo(service_id);
                    EventInfo event = new EventInfo(event_id);
                    SummarySheet sheet = new SummarySheet(event,service);
                    sheet.id = id;
                    newSids.add(rs.getInt("owner"));
                    newSheets.add(sheet);
                }
            }
        });

        for (int i = 0; i < newSheets.size(); i++) {
            SummarySheet sheet = newSheets.get(i);
            sheet.owner = User.loadUserById(newSids.get(i));

            // load service info
            sheet.currentService = ServiceInfo.loadServiceInfo(sheet.currentService.getId());
            System.out.println("MENU" + sheet.currentService.getId_menu_approved());
            // load event info
            sheet.currentEvent = EventInfo.loadEventInfo(sheet.currentEvent.getId());

            //load menu by id
            sheet.currentService.setMenu(Menu.loadMenuById(sheet.currentService.getId_menu_approved()));
            System.out.println(sheet.currentService.getMenu());

            //load assignments
            sheet.assignments = Assignment.loadTasks(sheet.id);
        }

        for (SummarySheet sheet: newSheets){
            loadedSheets.put(sheet.id, sheet);
        }

        return FXCollections.observableArrayList(loadedSheets.values());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getOwner(){
        return this.owner;
    }

    public String getOwnerName(){
        return owner.getUserName();
    }
}
