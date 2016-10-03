package es.surrealiti.habittracker;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Justin Barclay 2016
 * The purpose of this activity is to manage and view a completion history for a particular habit.
 *   I needed a specific activity to manage history because have a scrollable view and a listview
 *   broke android.
 *   Areas for improvement:
 *   - Make it more apparent that a long click is needed to delete
 *   - Prompt to confirm delete
 *
 */
public class History extends AppCompatActivity {
        private ArrayList<Date> history;
        private ListView historyContainer;
        private ArrayAdapter<Date> historyAdapter;
        private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);

        save = (Button) findViewById(R.id.save);
        historyContainer = (ListView) findViewById(R.id.history);
        history = (ArrayList<Date>) getIntent().getSerializableExtra("history");

        historyAdapter = new ArrayAdapter<Date>(this, R.layout.history_item, history);
        historyContainer.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                setResult(RESULT_OK);

                historyAdapter.remove(history.get(position));
                Snackbar snackbar = Snackbar.make(findViewById(R.id.history), R.string.removeHistory, Snackbar.LENGTH_SHORT);
                snackbar.show();

                return true;
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
    }

    protected void onStart(){
        super.onStart();
        historyAdapter = new ArrayAdapter<Date>(this, R.layout.history_item, history);
        historyContainer.setAdapter(historyAdapter);
    }

    public void save(){
        Intent intent = new Intent();
        intent.putExtra("history", history);
        setResult(RESULT_OK, intent);
        finish();
    }
}
