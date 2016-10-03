package es.surrealiti.habittracker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.provider.Telephony.Mms.Part.FILENAME;

/**
 * Created by Justin on 2016-10-02.
 */

public class HabitList {
    private ArrayList<Habit> weeklyHabits;

    public HabitList(){
        weeklyHabits = new ArrayList<Habit>();
    }
    public HabitList(ArrayList<Habit> habits){
        weeklyHabits = habits;
    }

    public Habit getHabit(int index){
        return weeklyHabits.get(index);
    }
    public ArrayList<Habit> getAllHabits(){
        return weeklyHabits;
    }
    public ArrayList<Habit> getTodaysHabits() {
        ArrayList<Habit> todaysHabits = new ArrayList<Habit>();
        String today = getToday();
        for(int i =0; i<weeklyHabits.size(); ++i){
            if(weeklyHabits.get(i).getDays().get(today)){
                todaysHabits.add(weeklyHabits.get(i));
            }
        }
        return todaysHabits;
    }

    public void removeHabit(Habit habit){
        for(int i =0; i< weeklyHabits.size(); ++i){
            if(habit.isEqual(weeklyHabits.get(i))){
                weeklyHabits.remove(i);
            }

        }
    }
    public int size(){
        return weeklyHabits.size();
    }
    public void addHabit(Habit habit){
        weeklyHabits.add(habit);
    }

    // http://stackoverflow.com/a/21553431
    private String getToday(){
        //Get string date to search in hash
        Date date= new Date();
        SimpleDateFormat formatToday = new SimpleDateFormat("EEEE");
        String today = formatToday.format(date);
        return today;
    }

    public void update(Habit habit){
        for(int i=0; i<weeklyHabits.size(); ++i){
            if (habit.isEqual(weeklyHabits.get(i))){
                weeklyHabits.remove(i);
                weeklyHabits.add(i, habit);
            }
        }
    }
}
