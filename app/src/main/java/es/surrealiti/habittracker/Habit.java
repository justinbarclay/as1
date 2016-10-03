package es.surrealiti.habittracker;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Justin on 2016-09-23.
 * This is the base class for which the rest of the program is made.
 * It contains models all the pertinent information to be able to easily track, update, and remove
 * habits.
 */

public class Habit implements Parcelable {
    private String name;
    private UUID id; // Using UUID so we have a very good chance of ID being unique
    private Date created;
    private HashMap<String, Boolean> days;
    private ArrayList<Date> history;

    public Habit(String name){
        this.name = name;
        this.id = UUID.randomUUID();
        this.created = new Date();
        this.days = instantiateDays();
        this.history = new ArrayList<Date>();
    }

    public Habit(String name, Date day){
        this.name = name;
        this.id = UUID.randomUUID();
        this.created = day;
        this.days = this.instantiateDays();
        this.history = new ArrayList<Date>();
    }

    // Change to String


    //Create our own method to cheque for object equality
    public boolean isEqual(Habit otherHabit){
        return this.id.equals(otherHabit.getID());
    }

    private HashMap<String, Boolean> instantiateDays(){
        HashMap<String, Boolean> map = new HashMap<String, Boolean>(7);
        map.put("Sunday", false);
        map.put("Monday", false);
        map.put("Tuesday", false);
        map.put("Wednesday", false);
        map.put("Thursday", false);
        map.put("Friday", false);
        map.put("Saturday", false);
        return map;
    }

    //Getters and Setters
    @Override
    public int describeContents(){
        return 0;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public Date createdOn(){
        return created;
    }
    public void setCreationDate(Date createdOn) {
        this.created = createdOn;
    }

    public ArrayList<Date> getHistory(){
        return this.history;
    }

    @Override
    public String toString(){
        return this.name;
    }

    public UUID getID(){
        return this.id;
    }

    public void addCompletion(){
        history.add(new Date());
    }
    public void deleteHistory(Date date){
        history.remove(date);
    }
    public void setHistory(ArrayList<Date> history) {
        this.history = history;
    }

    public void trackDay(String day, Boolean track){
        this.days.put(day, track);
    }
    public Map<String, Boolean> getDays(){
        return this.days;
    }

    //The following functions are needed to parcel Habits in between views
    protected Habit(Parcel in) {
        this.name = in.readString();
        this.id = UUID.fromString(in.readString());
        this.created = new Date(in.readLong());
        this.days = (HashMap<String, Boolean>) in.readBundle().getSerializable("days");
        this.history = (ArrayList<Date>) in.readBundle().getSerializable("history");
    }

    public static final Creator<Habit> CREATOR = new Creator<Habit>() {
        @Override
        public Habit createFromParcel(Parcel in) {
            return new Habit(in);
        }

        @Override
        public Habit[] newArray(int size) {
            return new Habit[size];
        }
    };

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeString(id.toString());
        out.writeLong(created.getTime());

        Bundle daysBundle = new Bundle();
        Bundle historyBundle = new Bundle();
        daysBundle.putSerializable("days", days);
        historyBundle.putSerializable("history", history);

        out.writeBundle(daysBundle);
        out.writeBundle(historyBundle);
    }
}
