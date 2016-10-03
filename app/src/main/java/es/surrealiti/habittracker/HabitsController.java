package es.surrealiti.habittracker;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by Justin on 2016-10-02.
 */

public class HabitsController {
    private static final String FILENAME = "file.sav";
    HabitList allHabits;
    Context context;

    public HabitsController(Context context){
        this.context = context;
        loadFromFile();
    }

    public HabitsController(Context context, HabitList habitList){
        this.context = context;
        allHabits = habitList;
    }

    public ArrayList<Habit> getAllHabits(){
        return allHabits.getAllHabits();
    }

    public ArrayList<Habit> getTodaysHabits(){
        return allHabits.getTodaysHabits();
    }

    public void updateHabitList(Habit habit){
        allHabits.update(habit);
    }

    public void removeHabit(Habit habit){
        allHabits.removeHabit(habit);
    }

    public void addHabit(Habit habit){
        allHabits.addHabit(habit);
    }

    public void loadFromFile() {
        try {
            FileInputStream fis = context.openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();

            // Code from http://stackoverflow.com/questions/12384064/gson-convert-from-json-to-a-typed-arraylistt
            Type listType = new TypeToken<HabitList>(){}.getType();

            allHabits = gson.fromJson(in,listType);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            allHabits = new HabitList();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }

    public void saveInFile() {
        try {
            FileOutputStream fos = context.openFileOutput(FILENAME,
                    0);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));

            Gson gson = new Gson();
            gson.toJson(allHabits, out);
            out.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }
}
