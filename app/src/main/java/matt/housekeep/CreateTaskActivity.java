package matt.housekeep;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateTaskActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private SharedPreferences prefs;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
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
        prefs = this.getSharedPreferences(
                getString(R.string.shared_prefs_key), Context.MODE_PRIVATE);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        typeSpinner = findViewById(R.id.typeSpinner);
        frequencySpinner = findViewById(R.id.frequencySpinner);

        final Bundle b = getIntent().getExtras();
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
                        findViewById(R.id.sunButton).setVisibility(View.GONE);
                        findViewById(R.id.monButton).setVisibility(View.GONE);
                        findViewById(R.id.tuesButton).setVisibility(View.GONE);
                        findViewById(R.id.wedButton).setVisibility(View.GONE);
                        findViewById(R.id.thursButton).setVisibility(View.GONE);
                        findViewById(R.id.fridayButton).setVisibility(View.GONE);
                        findViewById(R.id.satButton).setVisibility(View.GONE);
                        break;
                    }
                    case 1: {
                        freqLabel.setVisibility(View.VISIBLE);
                        frequencySpinner.setVisibility(View.VISIBLE);
                        findViewById(R.id.sunButton).setVisibility(View.VISIBLE);
                        findViewById(R.id.monButton).setVisibility(View.VISIBLE);
                        findViewById(R.id.tuesButton).setVisibility(View.VISIBLE);
                        findViewById(R.id.wedButton).setVisibility(View.VISIBLE);
                        findViewById(R.id.thursButton).setVisibility(View.VISIBLE);
                        findViewById(R.id.fridayButton).setVisibility(View.VISIBLE);
                        findViewById(R.id.satButton).setVisibility(View.VISIBLE);

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final ToggleButton sunday = findViewById(R.id.sunButton);
        sunday.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (sunday.isChecked())
                    sunday.setBackground(getResources().getDrawable(R.drawable.round_button));
                else sunday.setBackground(getResources().getDrawable(R.drawable.false_round_button));
            }
        });

        final ToggleButton monday = findViewById(R.id.monButton);
        monday.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (monday.isChecked())
                    monday.setBackground(getResources().getDrawable(R.drawable.round_button));
                else monday.setBackground(getResources().getDrawable(R.drawable.false_round_button));
            }
        });

        final ToggleButton tuesday = findViewById(R.id.tuesButton);
        tuesday.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (tuesday.isChecked())
                    tuesday.setBackground(getResources().getDrawable(R.drawable.round_button));
                else tuesday.setBackground(getResources().getDrawable(R.drawable.false_round_button));
            }
        });

        final ToggleButton wednesday = findViewById(R.id.wedButton);
        wednesday.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (wednesday.isChecked())
                    wednesday.setBackground(getResources().getDrawable(R.drawable.round_button));
                else wednesday.setBackground(getResources().getDrawable(R.drawable.false_round_button));
            }
        });

        final ToggleButton thursday = findViewById(R.id.thursButton);
        thursday.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (thursday.isChecked())
                    thursday.setBackground(getResources().getDrawable(R.drawable.round_button));
                else thursday.setBackground(getResources().getDrawable(R.drawable.false_round_button));
            }
        });

        final ToggleButton friday = findViewById(R.id.fridayButton);
        friday.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (friday.isChecked())
                    friday.setBackground(getResources().getDrawable(R.drawable.round_button));
                else friday.setBackground(getResources().getDrawable(R.drawable.false_round_button));
            }
        });

        final ToggleButton saturday = findViewById(R.id.satButton);
        saturday.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (saturday.isChecked())
                    saturday.setBackground(getResources().getDrawable(R.drawable.round_button));
                else saturday.setBackground(getResources().getDrawable(R.drawable.false_round_button));
            }
        });

        Button createTask = findViewById(R.id.CreateTaskButton);
        createTask.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final DatabaseReference createTaskDatabase;
                createTaskDatabase = FirebaseDatabase.getInstance().getReference();
                EditText taskName = findViewById(R.id.createTaskName);
                Spinner typeSpin = findViewById(R.id.typeSpinner);
                Spinner freqSpin = findViewById(R.id.frequencySpinner);
                EditText description = findViewById(R.id.createTaskDescription);

                boolean inGroup = b.getBoolean("inGroup");
                Log.d("In Group:", Boolean.toString(inGroup));
                String destination;
                if (inGroup)
                    destination = b.getString("GroupKey");
                else
                    destination = "userHome";



                //TODO First check for what type of task you are creating
                switch (typeSpin.getSelectedItem().toString()){

                    case "Chore":

                        if (inGroup) {
                            createTaskDatabase.child("Groups").child(destination).child("Chores").child(taskName.getText().toString()).child("Frequency").setValue(freqSpin.getSelectedItem().toString());
                            createTaskDatabase.child("Groups").child(destination).child("Chores").child(taskName.getText().toString()).child("Description").setValue(description.getText().toString());
                        }
                        else {
                            String user = prefs.getString(getString(R.string.saved_username_key), "");
                            createTaskDatabase.child("Users").child(user).child("Chores").child(taskName.getText().toString()).child("Frequency").setValue(freqSpin.getSelectedItem().toString());
                            createTaskDatabase.child("Users").child(user).child("Chores").child(taskName.getText().toString()).child("Description").setValue(description.getText().toString());
                        }
                        break;

                    case "Task":
                        if (inGroup) {
                            createTaskDatabase.child("Groups").child(destination).child("Tasks").child(taskName.getText().toString()).child("Description").setValue(description.getText().toString());
                        }
                        else {
                            String user = prefs.getString(getString(R.string.saved_username_key), "");
                            createTaskDatabase.child("Users").child(user).child("Tasks").child(taskName.getText().toString()).child("Description").setValue(description.getText().toString());
                        }
                        break;

                    default:
                        break;

                }


                finish();
            }
        });
    }

}
