package businesslogic.kitchen;

import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.event.EventInfo;
import businesslogic.event.ServiceInfo;
import businesslogic.menu.Menu;
import businesslogic.recipe.Recipe;
import businesslogic.shift.Shift;
import businesslogic.shift.ShiftException;
import businesslogic.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.KitchenPersistence;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class KitchenManager {

    private SummarySheet currentSumSheet;

    private ArrayList<KitchenEventReceiver> eventReceivers;

    public KitchenManager() {
        eventReceivers = new ArrayList<>();
    }

    public SummarySheet getCurrentSumSheet(){
        return currentSumSheet;
    }


    public SummarySheet createSummarySheet(EventInfo ev, ServiceInfo ser) throws UseCaseLogicException, KitchenException {

        User user = CatERing.getInstance().getUserManager().getCurrentUser();

        if(!user.isChef()){
            throw new UseCaseLogicException();
        } else if(!ev.isInCharge(user) || !ev.isService(ser)){
            throw new KitchenException();
        } else {
            SummarySheet ss = new SummarySheet(ev, ser);
            ss.setOwner(user);
            this.setCurrentSummarySheet(ss);
            Menu menu = ser.getMenu();
            if(menu == null ){
                System.out.println("IL MENU è NULLO");
            }
            ObservableList<Recipe> recipes = menu.getRecipes();

            for (Recipe r : recipes){
                ss.addTask(r);
            }

            this.notifySummarySheetCreated(ss);
            return ss;
        }
    }

    public SummarySheet chooseSummarySheet(SummarySheet summarySheet) throws UseCaseLogicException, KitchenException {

        User user = CatERing.getInstance().getUserManager().getCurrentUser();

        if(!user.isChef()){
            throw new UseCaseLogicException();
        } else {
            setCurrentSummarySheet(summarySheet);
        }
        return summarySheet;
    }

    public Assignment addTask(Recipe recipe) throws UseCaseLogicException {
        if(currentSumSheet == null){
            throw new UseCaseLogicException();
        } else {
            Assignment a = currentSumSheet.addTask(recipe);
            this.notifyTaskAdded(a);
            return a;
        }
    }

    public boolean deleteTask(Assignment task) throws UseCaseLogicException{
        if(currentSumSheet == null || !currentSumSheet.hasAssignment(task)){
            throw new UseCaseLogicException();
        } else {
            boolean result = currentSumSheet.deleteTask(task);
            if(result == true){
                this.notifyTaskRemoved(task);
            }
            return result;
        }
    }

    private void notifyTaskRemoved(Assignment task) {
        for(KitchenEventReceiver ke : this.eventReceivers) {
            ke.updateTaskRemoved(currentSumSheet, task);
        }
    }

    public void sortRecipes(Assignment assignment, int position) throws UseCaseLogicException {
        if(currentSumSheet == null || !currentSumSheet.hasAssignment(assignment)){
            throw new UseCaseLogicException();
        } else if( position < 0 || position >= currentSumSheet.assignmentsSize()){
            throw new IllegalArgumentException();
        } else {
            currentSumSheet.getAssignments().remove(assignment);
            currentSumSheet.getAssignments().add(position,assignment);
            this.notifyAssignmentsRearranged();
        }
    }

    public Assignment setReady (Assignment task, boolean ready) throws UseCaseLogicException {
        if(currentSumSheet == null || !currentSumSheet.hasAssignment(task)){
            throw new UseCaseLogicException();
        }
        task.setReady(ready);
        notifyTaskReady(task);
        return task;
    }

    public void addInfo(Assignment task, int time, String portions) throws UseCaseLogicException {
        if(currentSumSheet == null || !currentSumSheet.hasAssignment(task)){
            throw new UseCaseLogicException();
        }
        task.setEstimatedTime(time);
        task.setPortions(portions);
        notifyAddedInfo(task);
    }

    public void setKitchenFull(Shift shift) throws ShiftException {
        shift.setFull(true);
        notifyKitchenFull(shift);
    }

    public void assignTask(Assignment task,Shift shift,User cook) throws UseCaseLogicException, KitchenException {
        Calendar c = Calendar.getInstance();
        Date now = c.getTime();

        if(currentSumSheet==null || !currentSumSheet.hasAssignment(task)){
            throw new UseCaseLogicException();
        }
        else
        if(shift.getDate().before(now) || (cook != null && !cook.avaible(shift))){
            throw new KitchenException();
        }
        else
        {
            task.setCook(cook);
            task.setShift(shift);
            notifyAssignmentModified(currentSumSheet,task);
        }
    }

    public void assignTask(Assignment task,Shift shift) throws UseCaseLogicException, KitchenException {
        Calendar c = Calendar.getInstance();
        Date now = c.getTime();

        if(currentSumSheet==null || !currentSumSheet.hasAssignment(task)){
            throw new UseCaseLogicException();
        }
        else
        if(shift.getDate().before(now)){
            throw new KitchenException();
        }
        else
        {
            task.setShift(shift);
            notifyAssignmentModified(currentSumSheet,task);
        }
    }

    public void modifyTask(Assignment task, User cook, Shift shift, int time, String portions) throws UseCaseLogicException {
        if(currentSumSheet == null || !currentSumSheet.hasAssignment(task)){
            throw new UseCaseLogicException();
        } else {
            task.setCook(cook);
            task.setShift(shift);
            task.setEstimatedTime(time);
            task.setPortions(portions);
            notifyTaskModified(task);
        }
    }

    public void modifyTask(Assignment task, int time) throws UseCaseLogicException {
        if(currentSumSheet == null || currentSumSheet.hasAssignment(task)){
            throw new UseCaseLogicException();
        } else {
            task.setEstimatedTime(time);
            notifyTaskModified(task);
        }
    }

    public void modifyTask(Assignment task, Shift shift) throws UseCaseLogicException {
        if(currentSumSheet == null || currentSumSheet.hasAssignment(task)){
            throw new UseCaseLogicException();
        } else {
            task.setShift(shift);
            notifyTaskModified(task);
        }
    }

    public void modifyTask(Assignment task, User cook) throws UseCaseLogicException {
        if(currentSumSheet == null || currentSumSheet.hasAssignment(task)){
            throw new UseCaseLogicException();
        } else {
            task.setCook(cook);
            notifyTaskModified(task);
        }
    }

    /*public void modifyTask(Assignment task, String portions) throws UseCaseLogicException {
        if(currentSumSheet == null || currentSumSheet.hasAssignment(task)){
            throw new UseCaseLogicException();
        } else {
            task.setPortions(portions);
            notifyTaskModified(task);
        }
    }*/

    public boolean deleteAssignment(Assignment assignment) throws UseCaseLogicException {
        if(currentSumSheet == null || !currentSumSheet.hasAssignment(assignment)){
            throw new UseCaseLogicException();
        }
        boolean result = assignment.deleteAssignment();

        if(result){
            notifyAssignmentDeleted(assignment);
        }
        return result;
    }

    private void notifyAssignmentDeleted(Assignment assignment) {
        for(KitchenEventReceiver ke : this.eventReceivers) {
            ke.updateAssignmentDeleted(assignment);
        }
    }

    private void notifyKitchenFull(Shift shift) {
        for(KitchenEventReceiver ke : this.eventReceivers) {
            ke.updateKitchenFull(shift);
        }
    }

    private void notifyAddedInfo(Assignment task) {
    }

    private void notifyTaskReady(Assignment task) {
        for(KitchenEventReceiver ke : this.eventReceivers) {
            ke.updateTaskReady(task);
        }
    }


    private void notifyAssignmentsRearranged() {
        for(KitchenEventReceiver ke : this.eventReceivers) {
            ke.updateAssignmentsRearranged(currentSumSheet);
        }
    }

    private void notifyTaskAdded(Assignment a) {
        for(KitchenEventReceiver ke : this.eventReceivers) {
            ke.updateTaskAdded(currentSumSheet,a);
        }
    }

    private void setCurrentSummarySheet(SummarySheet ss) {
        this.currentSumSheet = ss;
    }

    private void notifyAssignmentModified(SummarySheet currentSumSheet, Assignment task) {
        for(KitchenEventReceiver ke : this.eventReceivers) {
            ke.updateAssignmentModified(currentSumSheet,task);
        }
    }

    private void notifySummarySheetCreated(SummarySheet ss) {
        for(KitchenEventReceiver ke : this.eventReceivers) {
            ke.updateSummarySheetCreated(ss);
        }
    }

    private void notifyTaskModified(Assignment task) {
        for(KitchenEventReceiver ke : this.eventReceivers) {
            ke.updateTaskModified(task);
        }
    }

    //PERSISTENZA
    public ObservableList<SummarySheet> getAllSheets(){
        return SummarySheet.loadAllSheets();
    }

    public void addEventReceinver(KitchenPersistence kitchenPersistence) {
        this.eventReceivers.add(kitchenPersistence);
    }

    public ObservableList<Shift> getShiftTable() {
        return  CatERing.getInstance().getShiftManager().getShifts();
    }
}
