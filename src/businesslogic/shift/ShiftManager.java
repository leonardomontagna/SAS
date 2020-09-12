package businesslogic.shift;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class ShiftManager {
    private ArrayList<Shift> shifts;

    public ObservableList<Shift> getShiftsNonSaturi() {
        return FXCollections.unmodifiableObservableList(Shift.loadShifts());
    }
    public ObservableList<Shift> getShifts(){
        return FXCollections.unmodifiableObservableList(Shift.getTable());
    }
}
