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
        System.out.println(history);

        historyAdapter = new ArrayAdapter<Date>(this, R.layout.history_item, history);
        historyContainer.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                setResult(RESULT_OK);

                System.out.println(history.get(position).toString());
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
