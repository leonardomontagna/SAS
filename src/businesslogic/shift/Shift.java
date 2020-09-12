package businesslogic.shift;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.PersistenceManager;
import persistence.ResultHandler;
import ui.kitchen.ShiftTable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.*;

public class Shift {

    int id;
    Date date;
    Time inizio;
    Time fine;
    boolean full;
    int disponibile;
    Set<String> dispo;
    Set<String> compiti;

    private static Map<Integer, Shift> loadedShift = new HashMap<>();
    private static Map<Integer, Shift> shiftTable = new HashMap<>();

    public Shift(int id){
        this.id = id;
    }

    public Shift(){
        dispo = new HashSet<>();
        compiti = new HashSet<>();
    }

    public Date getDate(){
        return date;
    }

    public boolean getFull(){
        return full;
    }

    public boolean isFull() {
        return (full == true);
    }

    public void setFull(boolean full) {
        this.full = full;
    }

    public int getId() { return id; }

    public int getDisponibile(){ return disponibile; }

    public String toString(){
        return "data: " + date + " [ inizio: " + inizio + " fine: " + fine + " ]";
    }

    public String stampaTab(){
        return "• data: " + date + " [ inizio: " + inizio + " fine: " + fine + " ]" + "\nsaturo : " + isFull() + "\ncuochi disponibili nel turno : " + dispo + "\ncompiti svolti nel turno :" + compiti;
    }

    //PERSISTENZA
    public static Shift loadShiftById(int shift_id){
        String query = "SELECT * FROM shifttable WHERE id=" + shift_id;
        Shift shift = new Shift(shift_id);

        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                shift.date = rs.getDate("data");
                shift.inizio = rs.getTime("inizio");
                shift.fine = rs.getTime("fine");
                shift.full = rs.getBoolean("saturo");
            }
        });
        return shift;
    }

    public static ObservableList<Shift> loadShifts(){
        String query = "SELECT * FROM shifttable WHERE saturo = 0";

        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                int id = rs.getInt("id");
                if(!loadedShift.containsKey(id)){
                    Shift shift = new Shift();
                    shift.id = id;
                    shift.date = rs.getDate("data");
                    shift.inizio = rs.getTime("inizio");
                    shift.fine = rs.getTime("fine");
                    shift.full = rs.getBoolean("saturo");
                    loadedShift.put(shift.id, shift);
                }
            }

        });

        return FXCollections.observableArrayList(loadedShift.values());
    }

    public static ObservableList<Shift> getTable() {
        String query = "SELECT s.id, s.data, s.inizio, s.fine, s.saturo, r.name, u.username FROM shifttable s JOIN assignments a ON s.id = a.shift JOIN recipes r ON recipe = r.id JOIN disponibile d ON s.id = d.id_shift JOIN users u ON d.id_user = u.id WHERE a.terminato = false";
        ArrayList<Integer> ids = new ArrayList<Integer>();
        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                int id = rs.getInt("id");
                if(!shiftTable.containsKey(id)){
                    Shift shift = new Shift();
                    shift.id = id;
                    ids.add(id);
                    shift.date = rs.getDate("data");
                    shift.inizio = rs.getTime("inizio");
                    shift.fine = rs.getTime("fine");
                    shift.full = rs.getBoolean("saturo");
                    shift.compiti.add(rs.getString("name"));
                    shift.dispo.add(rs.getString("username"));
                    shiftTable.put(shift.id, shift);
                } else {
                    Shift shift = shiftTable.get(id);
                    shift.compiti.add(rs.getString("name"));
                    shift.dispo.add(rs.getString("username"));
                }
            }

        });

        return FXCollections.observableArrayList(shiftTable.values());
    }

    public static void setShiftFull(Shift shift) {
        String query = "UPDATE shifttable SET saturo = " + shift.getFull() + " WHERE id = " + shift.id;
        PersistenceManager.executeUpdate(query);
    }


}
