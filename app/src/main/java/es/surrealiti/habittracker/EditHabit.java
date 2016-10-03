package es.surrealiti.habittracker;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Justin on 2016-09-30.
 * EditHabit controls the view and manipulation of local state for all activities relating to
 * editing a habit, except for editing histories. Editing history has been put out to it's own
 * view as having scrolling views and ListItems really messes with Android.
 */

public class EditHabit extends AppCompatActivity {
    private Habit habit;
    private EditText name;

    private Calendar myCalendar;
    private TextView createdOn;
    private TextView completions;
    //Weekdays!
    private int index;
    private Switch monday;
    private Switch tuesday;
    private Switch wednesday;
    private Switch thursday;
    private Switch friday;
    private Switch saturday;
    private Switch sunday;

    private Button historyButton;
    private Button doneButton;
    private Button deleteButton;

    private final int DELETE = 535;

    DatePickerDialog.OnDateSetListener date;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_habit);

        historyButton = (Button) findViewById(R.id.viewHistory);
        doneButton = (Button) findViewById(R.id.done);
        deleteButton = (Button) findViewById(R.id.delete);

        monday = (Switch) findViewById(R.id.Monday);
        tuesday = (Switch) findViewById(R.id.Tuesday);
        wednesday = (Switch) findViewById(R.id.Wednesday);
        thursday = (Switch) findViewById(R.id.Thursday);
        friday = (Switch) findViewById(R.id.Friday);
        saturday = (Switch) findViewById(R.id.Saturday);
        sunday = (Switch) findViewById(R.id.Sunday);

        completions = (TextView) findViewById(R.id.completions);
        name = (EditText) findViewById(R.id.habitName);

        // Grab passed in date from MainView
        habit =(Habit) getIntent().getParcelableExtra("Habit");

        createdOn = (TextView) findViewById(R.id.createdOn);
        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        createdOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(EditHabit.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });

    }

    private CompoundButton.OnCheckedChangeListener trackDay = new CompoundButton.OnCheckedChangeListener(){
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            habit.trackDay(buttonView.getText().toString(), isChecked);
        }
    };

    public void onResume(){
        super.onResume();
        myCalendar.setTime(habit.createdOn());

        // Update all switches based on current state
        sunday.setChecked(habit.getDays().get("Sunday"));
        monday.setChecked(habit.getDays().get("Monday"));
        tuesday.setChecked(habit.getDays().get("Tuesday"));
        wednesday.setChecked(habit.getDays().get("Wednesday"));
        thursday.setChecked(habit.getDays().get("Thursday"));
        friday.setChecked(habit.getDays().get("Friday"));
        saturday.setChecked(habit.getDays().get("Saturday"));

        createdOn.setText(formatDate(habit.createdOn()));

        completions.setText(setCompletionText());

    }
    public void onStart(){
        super.onStart();
        name.setText(habit.getName());

        //First time this starts, we should start listening for activities
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHistory();
            }
        });
        monday.setOnCheckedChangeListener(trackDay);
        tuesday.setOnCheckedChangeListener(trackDay);
        wednesday.setOnCheckedChangeListener(trackDay);
        thursday.setOnCheckedChangeListener(trackDay);
        friday.setOnCheckedChangeListener(trackDay);
        sunday.setOnCheckedChangeListener(trackDay);
        saturday.setOnCheckedChangeListener(trackDay);
    }

    public void save(){
        if (name.getText().toString().length() < 1){
            Snackbar snackbar = Snackbar.make(findViewById(R.id.edit_habit), R.string.noName, Snackbar.LENGTH_SHORT);
            snackbar.show();
        } else {
            Intent intent = new Intent();
            intent.putExtra("delete", false);
            intent.putExtra("Habit", habit);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    public void delete(){
        Intent intent = new Intent();
        intent.putExtra("delete", true);
        intent.putExtra("Habit", habit);
        setResult(RESULT_OK, intent);
        finish();
    }

    private String formatDate(Date date){
        String myFormat = "MMM dd, yyyy"; //In which you need put here
        SimpleDateFormat simpleFormat = new SimpleDateFormat(myFormat, Locale.US);
        return "Created on: " + simpleFormat.format(date);
    }

    private void updateLabel() {
        String myFormat = "MMM dd, yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Date newDate = myCalendar.getTime();
        habit.setCreationDate(newDate);
        createdOn.setText(formatDate(newDate));
    }

    private String setCompletionText(){
        int totalCompletions = habit.getHistory().size();
        if(totalCompletions<1){
            return "You have not completed this habit yet.";
        } else if(totalCompletions == 1) {
            return "You have completed this habit once";
        } else if(totalCompletions > 1){
            return "You have completed this habit " + totalCompletions + " times";
        }
        return "Error retrieving completions";
    }

    public void viewHistory(){
        Intent intent = new Intent(this, History.class);

        intent.putExtra("history", habit.getHistory());
        startActivityForResult(intent, 3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    // Method for getting history back from history view
        //Request Code = returning from history
         if (requestCode == 3) {
            if(resultCode == Activity.RESULT_OK){
                ArrayList<Date> history= (ArrayList<Date>) data.getSerializableExtra("history");
                habit.setHistory(history);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                //If canceled, thats fine the edit doesn't save
            }
        } else{
            throw new RuntimeException();
        }
    }
};

