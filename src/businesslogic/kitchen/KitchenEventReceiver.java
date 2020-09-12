package businesslogic.kitchen;

import businesslogic.shift.Shift;

public interface KitchenEventReceiver {
    void updateSummarySheetCreated(SummarySheet ss);

    void updateTaskRemoved(SummarySheet ss, Assignment task);

    void updateAssignmentModified(SummarySheet currentSumSheet, Assignment task);

    void updateTaskModified(Assignment task);

    void updateAssignmentDeleted(Assignment assignment);

    void updateTaskAdded(SummarySheet currentSumSheet, Assignment a);

    void updateAssignmentsRearranged(SummarySheet currentSumSheet);

    void updateTaskReady(Assignment task);

    void updateKitchenFull(Shift shift);
}
