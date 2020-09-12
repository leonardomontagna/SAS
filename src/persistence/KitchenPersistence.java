package persistence;

import businesslogic.kitchen.Assignment;
import businesslogic.kitchen.KitchenEventReceiver;
import businesslogic.kitchen.SummarySheet;
import businesslogic.shift.Shift;

public class KitchenPersistence implements KitchenEventReceiver {

    @Override
    public void updateSummarySheetCreated(SummarySheet ss){
        SummarySheet.saveNewSheet(ss);
    }

    @Override
    public void updateTaskAdded(SummarySheet sheet, Assignment a) {
        int sheet_id = sheet.getId();
        int pos = sheet.getAssignmentPosition(a);
        int recipe_id = a.getRecipe().getId();
        Assignment.saveNewAssignment(sheet_id, recipe_id, a, pos);
    }

    @Override
    public void updateAssignmentsRearranged(SummarySheet currentSumSheet) {
        SummarySheet.saveTaskOrder(currentSumSheet);
    }

    @Override
    public void updateTaskReady(Assignment task) {
        Assignment.setTaskReady(task.getId(), task.isReady());
    }

    @Override
    public void updateKitchenFull(Shift sheet) {
        Shift.setShiftFull(sheet);
    }

    @Override
    public void updateTaskRemoved(SummarySheet ss, Assignment task) {
        Assignment.removeTask(task);
        SummarySheet.saveTaskOrder(ss);
    }

    @Override
    public void updateAssignmentModified(SummarySheet currentSumSheet, Assignment task) {

    }

    @Override
    public void updateTaskModified(Assignment task) {
        Assignment.modifyTask(task);
    }

    @Override
    public void updateAssignmentDeleted(Assignment assignment) {
        Assignment.removeAssignment(assignment);
    }


}
