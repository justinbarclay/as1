package es.surrealiti.habittracker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
/*  This is the entry point to the program.
* It controls the views for any list of habits, as well as, it marshals
* the creation of new habits or, editing/deleting of old habits.
 *
 */
public class MainView extends AppCompatActivity {
    private ArrayList<Habit> allHabits = new ArrayList<Habit>();
    private ListView habitsContainer;
    private Button newHabit;
    private ArrayAdapter<Habit> habitAdapter;
    private HabitsController mainController;
    private Boolean viewingAll = false;
    private TextView header;
    private Button viewAll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);
        mainController = new HabitsController(this);
        habitsContainer = (ListView) findViewById(R.id.list_habits);
        newHabit = (Button) findViewById(R.id.add_habit_button);
        viewAll = (Button) findViewById(R.id.allHabits);
        header = (TextView) findViewById(R.id.habit_header);


        // http://stackoverflow.com/a/30424932
        habitAdapter = new ArrayAdapter<Habit>(this, R.layout.single_habit, allHabits){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position, convertView, parent);

                long twentyFourAgo = new Date().getTime() - (24*60*60*1000);
                Habit currentHabit = allHabits.get(position);
                int size = currentHabit.getHistory().size();
                if(size > 0) {
                    long lastCompletionTime = currentHabit.getHistory().get(size - 1).getTime();
                    if (lastCompletionTime > twentyFourAgo) {
                        view.setBackgroundColor(Color.rgb(101, 207, 45));
                    } else{
                        view.setBackgroundColor(Color.rgb(255, 255, 255));
                    }
                } else {
                    view.setBackgroundColor(Color.rgb(255, 255, 255));
                }
                return view;
            }
        };
        habitsContainer.setAdapter(habitAdapter);

        habitsContainer.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setResult(RESULT_OK);
                editHabit(position);
            }
        });


        habitsContainer.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(habitsContainer.getChildCount());
                setResult(RESULT_OK);
                view.setBackgroundColor(Color.rgb(101, 207, 45));
                Snackbar snackbar = Snackbar.make(findViewById(R.id.habitTracker), R.string.completeHabit, Snackbar.LENGTH_SHORT);
                snackbar.show();
                allHabits.get(position).addCompletion();
                return true;
            }
        });

        newHabit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                setResult(RESULT_OK);
                addNewHabit();
            }
        });

        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeView();
            }
        });
    }

    protected void onStart() {
        super.onStart();
        habitAdapter.notifyDataSetChanged();
    }

    public void onResume(){
        super.onResume();
        update();

    }

    public void onPause(){
        super.onPause();
    }

    private void update(){
        if(viewingAll){
            this.allHabits = mainController.getAllHabits();
        } else{
            this.allHabits = mainController.getTodaysHabits();
        }

        // Each time you set a completely new array, you need to setup a new adapter
        habitAdapter.clear();
        habitAdapter.addAll(allHabits);
        habitAdapter.notifyDataSetChanged();
    }

    public void onStop(){
        super.onStop();
        mainController.saveInFile();
    }

    private void changeView(){
        viewingAll = !viewingAll;
        String headerText = viewingAll? "All Habits" : "Today's Habits";
        String buttonText = viewingAll? "View Today's Habits" : "View All Habits";
        header.setText(headerText);
        viewAll.setText(buttonText);
        update();

    }
    private void editHabit(int index){
        Intent intent = new Intent(this, EditHabit.class);
        intent.putExtra("Habit", allHabits.get(index));
        startActivityForResult(intent, 2);
    }
    private void addNewHabit() {
        Intent intent = new Intent(this, MakeHabit.class);
        startActivityForResult(intent, 1);
    }
    private void ViewAllHabits(int index){
        Intent intent = new Intent(this, EditHabit.class);
        intent.putExtra("Habit", mainController.getAllHabits());
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                Habit newHabit= (Habit) data.getParcelableExtra("newHabit");

                mainController.addHabit(newHabit);
                mainController.saveInFile();
                update();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                throw new RuntimeException();
            }
        } else if(requestCode == 2){
            if(resultCode == Activity.RESULT_OK){
                Habit habit= (Habit) data.getParcelableExtra("Habit");
                Boolean delete = data.getBooleanExtra("delete", false);
                System.out.println("Delete? " + delete);
                if(delete){
                    System.out.println("Calling maincontroller");
                    mainController.removeHabit(habit);
                } else {
                    mainController.updateHabitList(habit);
                }
                mainController.saveInFile();
                update();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                throw new RuntimeException();
            }
        }
    }
}
