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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
