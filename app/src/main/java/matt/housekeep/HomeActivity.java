package matt.housekeep;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//public class HomeActivity extends AppCompatActivity
public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private ScrollView HomeScroll;
    private SharedPreferences prefs;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private ArrayList<String> groups;
    private ArrayList<String> groupKeys;
    private ArrayList<String> taskNames;
    private ArrayList<String> choreNames;
    private String username;

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
                    HomeScroll = (ScrollView) findViewById(R.id.HomeScroll);
                    HomeScroll.fullScroll(ScrollView.FOCUS_UP);
                    return true;
                case R.id.menu_create_task:
                    intent = new Intent(HomeActivity.this, CreateTaskActivity.class);
                    intent.putExtras(b);
                    startActivity(intent);
                    return true;
                case R.id.menu_profile:
                    intent = new Intent(HomeActivity.this, UserProfileActivity.class);
                    intent.putExtras(b);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };

    private void setGroups(ArrayList<String> groups, ArrayList<String> groupKeys){
        this.groups = groups;
        this.groupKeys = groupKeys;
    }

    //retrieve basic userdata from database to make buttons for groups and list
    //user's chores and available tasks
    private void init(){

        //create/get shared prefs file
        prefs = this.getSharedPreferences(
                getString(R.string.shared_prefs_key), Context.MODE_PRIVATE);

        username = prefs.getString(getString(R.string.saved_username_key), "");

        DatabaseReference myRef = database.getReference("Users/" + username + "/Groups");

        final ArrayList<String> groups = new ArrayList<>();
        final ArrayList<String> groupKeys = new ArrayList<>();


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot newSnap: dataSnapshot.getChildren()){

                    //Store names of groups
                    groups.add((String) newSnap.getValue());
                    groupKeys.add(newSnap.getKey());
                    Log.d("Keys", newSnap.getKey());
                }

                setGroups(groups, groupKeys);

                for(String group: groups){
                    Log.d("GROUPS", group);
                }

                makeGroupsButtons();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //Log.d("FETCHED", username);

    }

    private void initTasks(){

        taskNames = new ArrayList<>();
        DatabaseReference myRef = database.getReference("Users/" + username + "/Tasks");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot newSnap: dataSnapshot.getChildren()){
                        taskNames.add(newSnap.getKey());
                    }
                    makeTaskList();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void initChores(){

        choreNames = new ArrayList<>();
        DatabaseReference myRef = database.getReference("Users/" + username + "/Chores");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot newSnap: dataSnapshot.getChildren()){
                        choreNames.add(newSnap.getKey());
                    }
                    for(String name : choreNames)
                        Log.d("CHORES", name);

                    makeChoreList();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void makeGroupsButtons(){

        LinearLayout ll = findViewById(R.id.GroupLayout);
        final int offset = 500;

        for(int i = 0; i < groups.size(); i++){


            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            Button btn = new Button(this);
            btn.setTextSize(18);
            btn.setId(i+500);
            final int id_ = btn.getId();
            btn.setText(groups.get(id_-500));
            ll.addView(btn, params);

            btn = (findViewById(id_));
            btn.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view) {

                    //Need to dynamically create a new activity with groups[id_]'s info

                    Intent intent = new Intent(HomeActivity.this, GroupActivity.class);
                    Bundle b = new Bundle();
                    b.putString("GroupName", groups.get(id_-500));
                    b.putString("GroupKey", groupKeys.get(id_-500));
                    b.putString("UserName", username);
                    intent.putExtras(b);
                    startActivity(intent);

                    Toast.makeText(view.getContext(), "Button clicked index: " +
                            (id_-500), Toast.LENGTH_SHORT).show();
                    Log.d("Key", b.getString("GroupKey"));
                }
            });


        }
    }

    private void makeChoreList(){

        LinearLayout layout = findViewById(R.id.chore_layout);
        //DatabaseReference myRef = database.getReference("Users/" + username + "/Chores");


        for(int i = 0; i < choreNames.size(); i++){

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            Button btn = new Button(this);
            btn.setTextSize(18);
            btn.setId(i);
            final int id_ = btn.getId();
            btn.setText(choreNames.get(id_));
            layout.addView(btn, params);

            btn = (findViewById(id_));
            btn.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view) {

                    database.getReference().child("Users").child(username).child("Chores").child(choreNames.get(id_)).removeValue();
                    finish();
                    startActivity(getIntent());
                    Toast.makeText(view.getContext(), "Completed " + choreNames.get(id_),
                            Toast.LENGTH_SHORT).show();
                   // Log.d("Key", b.getString("GroupKey"));
                }
            });

        }
    }

    public void makeTaskList(){

        LinearLayout layout = findViewById(R.id.task_layout);

        for(int i = 0; i < taskNames.size(); i++){

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            Button btn = new Button(this);
            btn.setTextSize(18);
            btn.setId(i);
            final int id_ = btn.getId();
            btn.setText(taskNames.get(id_));
            layout.addView(btn, params);

            btn = (findViewById(id_));
            btn.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view) {

                    database.getReference().child("Users").child(username).child("Tasks").child(taskNames.get(id_)).removeValue();
                    Toast.makeText(view.getContext(), "Completed " + taskNames.get(id_),
                            Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(getIntent());
                    // Log.d("Key", b.getString("GroupKey"));
                }
            });

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.homeLayout);
        layout.requestFocus();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView);


        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        init();
        initTasks();
        initChores();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.action_bar_buttons, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Intent intent;
        Bundle b = new Bundle();
        b.putString("GroupName", "");
        b.putString("UserName", username);
        b.putBoolean("inGroup", false);

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_notification) {
            Toast.makeText(HomeActivity.this, "Action clicked", Toast.LENGTH_LONG).show();
            intent = new Intent(HomeActivity.this, NotificationActivity.class);
            intent.putExtras(b);
            startActivity(intent);
            return true;
        }

        if(id == R.id.create_group){
            intent = new Intent(HomeActivity.this, CreateGroupActivity.class);
            intent.putExtras(b);
            startActivity(intent);
        }

        if(id == R.id.settings){
            intent = new Intent(HomeActivity.this, SettingsActivity.class);
            intent.putExtras(b);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


}
