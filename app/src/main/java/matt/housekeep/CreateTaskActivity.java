package matt.housekeep;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class CreateTaskActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    private TextView freqLabel;

    private Spinner typeSpinner;
    private Spinner frequencySpinner;
    private String groupname;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Intent intent;
            Bundle b = new Bundle();
            b.putString("GroupName", "");
            b.putBoolean("inGroup", false);

            switch (item.getItemId()) {
                case R.id.menu_home:
                    intent = new Intent(CreateTaskActivity.this, HomeActivity.class);
                    intent.putExtras(b);
                    startActivity(intent);
                    return true;
                case R.id.menu_create_task:
                    //Maybe contextually turn this into a different button/remove on this screen
                    //startActivity(new Intent(CreateTaskActivity.this, CreateTaskActivity.class));
                    return true;
                case R.id.menu_profile:
                    intent = new Intent(CreateTaskActivity.this, UserProfileActivity.class);
                    intent.putExtras(b);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        typeSpinner = findViewById(R.id.typeSpinner);
        frequencySpinner = findViewById(R.id.frequencySpinner);

        Bundle b = getIntent().getExtras();
        groupname = b.getString("GroupName");
        //boolean inGroup = b.getBoolean("inGroup");

        String[] types = new String[]{"Task", "Chore", "Reminder"};
        String[] frequencies = new String[]{"Daily", "Weekly", "Monthly"};

        ArrayAdapter<String> typesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, types);
        ArrayAdapter<String> freqAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, frequencies);

        typeSpinner.setAdapter(typesAdapter);
        frequencySpinner.setAdapter(freqAdapter);

        freqLabel = (TextView)findViewById(R.id.frequencyTextView);

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i){
                    case 0:     //task
                    case 2:{    //reminder
                        freqLabel.setVisibility(View.GONE);
                        frequencySpinner.setVisibility(View.GONE);
                        break;
                    }
                    case 1: {
                        freqLabel.setVisibility(View.VISIBLE);
                        frequencySpinner.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

}
