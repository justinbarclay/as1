package es.surrealiti.habittracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.ToggleButton;

/**
 * Created by Justin Barclay 2016
 * This activity is meant for initializing new habits.
 * This view is distinct from EditHabit as it's requirements are much simple and does not need as
 * many UI elements as EditHabit.
 *
 */

public class MakeHabit extends AppCompatActivity {
    private Button doneButton;
    private EditText name;
    private Switch monday;
    private Switch tuesday;
    private Switch wednesday;
    private Switch thursday;
    private Switch friday;
    private Switch saturday;
    private Switch sunday;

    Habit habit = new Habit("");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_habit);
        doneButton = (Button) findViewById(R.id.done);

        monday = (Switch) findViewById(R.id.Monday);
        tuesday = (Switch) findViewById(R.id.Tuesday);
        wednesday = (Switch) findViewById(R.id.Wednesday);
        thursday = (Switch) findViewById(R.id.Thursday);
        friday = (Switch) findViewById(R.id.Friday);
        saturday = (Switch) findViewById(R.id.Saturday);
        sunday = (Switch) findViewById(R.id.Sunday);

        name = (EditText) findViewById(R.id.habitName);

        monday.setOnCheckedChangeListener(trackDay);
        tuesday.setOnCheckedChangeListener(trackDay);
        wednesday.setOnCheckedChangeListener(trackDay);
        thursday.setOnCheckedChangeListener(trackDay);
        friday.setOnCheckedChangeListener(trackDay);
        sunday.setOnCheckedChangeListener(trackDay);
        saturday.setOnCheckedChangeListener(trackDay);

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                habit.setName(s.toString());
            }
        });
        doneButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                done();

            }
        });
    }

    private CompoundButton.OnCheckedChangeListener trackDay = new CompoundButton.OnCheckedChangeListener(){
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            habit.trackDay(buttonView.getText().toString(), isChecked);
        }
    };

    private void done() {
        if (name.getText().toString().length() < 1){
            Snackbar snackbar = Snackbar.make(findViewById(R.id.make_habit), R.string.noName, Snackbar.LENGTH_SHORT);
            snackbar.show();
        } else {
            Intent intent = new Intent();
            intent.putExtra("newHabit", habit);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
