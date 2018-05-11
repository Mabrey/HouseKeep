package matt.housekeep;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

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
    private Menu menu;

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

        groups = new ArrayList<>();
        final ArrayList<String> groupKeys = new ArrayList<>();


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                groups.clear();
                groupKeys.clear();
                LinearLayout ll = findViewById(R.id.GroupLayout);
                ll.removeAllViews();

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


        final LinearLayout taskLL = (LinearLayout)findViewById(R.id.task_layout);
        DatabaseReference myRef = database.getReference("Users/" + username + "/Tasks");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    taskLL.removeAllViews();

                    for(DataSnapshot newSnap: dataSnapshot.getChildren()){

                        final View taskButton = LayoutInflater.from(getApplicationContext())
                                .inflate(R.layout.task_button, taskLL, false);

                        final TextView taskName = taskButton.findViewById(R.id.task_name);
                        final TextView createdBy = taskButton.findViewById(R.id.created_by_username);
                        final ImageView profilePic = taskButton.findViewById(R.id.profilePicture);
                        final TextView creationDate = taskButton.findViewById(R.id.creation_date);
                        final CheckBox complete = taskButton.findViewById(R.id.checkBox);

                        taskName.setText(newSnap.getKey());
                        profilePic.setImageResource(R.drawable.ic_profile);
                        //TODO make these real values
                        if(newSnap.child("Created By").exists())
                            createdBy.setText(newSnap.child("Created By").getValue().toString());
                        if(newSnap.child("Creation Date").exists())
                            creationDate.setText(newSnap.child("Creation Date").getValue().toString());
                        Log.d("Task Name", taskName.getText().toString());
                        // Log.d("Created by", (newSnap.child(taskname).child("Created By").getValue().toString()));
                        //Log.d("Creation Date", (newSnap.child(taskname).child("Creation Date").getValue().toString()));

                        //setupCheckbox(complete, false);
                        complete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if(isChecked){

                                    Thread thread = new Thread(){
                                        @Override
                                        public void run(){

                                            try{
                                                Thread.sleep(1000);
                                            }catch(InterruptedException e)
                                            {

                                            }

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    taskLL.removeView(taskButton);
                                                    database.getReference("Users/" + username + "/Tasks/" + taskName.getText()).setValue(null);
                                                }
                                            });
                                        }
                                    };

                                    thread.start();


                                    final DatabaseReference StatRef = database.getReference("Users/" + username + "/Statistics/Tasks Completed");
                                    StatRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            int tasksCompleted = 0;
                                            if(dataSnapshot.exists()){
                                                tasksCompleted = Integer.parseInt(dataSnapshot.getValue().toString());

                                            }
                                            tasksCompleted++;
                                            StatRef.setValue(tasksCompleted);
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }
                        });
                        taskLL.addView(taskButton);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    String newDueDate;
    String oldDueDate;
    private void initChores(){


        final LinearLayout choreLL = (LinearLayout)findViewById(R.id.chore_layout);
        DatabaseReference myRef = database.getReference("Users/" + username + "/Chores");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    choreLL.removeAllViews();
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0,10, 0,10);


                    for(final DataSnapshot newSnap: dataSnapshot.getChildren()){

                        final View choreButton = LayoutInflater.from(getApplicationContext())
                                .inflate(R.layout.chore_button, choreLL, false);

                        final TextView choreName = choreButton.findViewById(R.id.member_name);
                        final TextView userResponsible = choreButton.findViewById(R.id.username_textfield);
                        final ImageView profilePic = choreButton.findViewById(R.id.profilePicture);
                        final TextView dueDate = choreButton.findViewById(R.id.choreCompleteTime);
                        final CheckBox complete = choreButton.findViewById(R.id.checkBox);

                        choreName.setText(newSnap.getKey().toString());
                        profilePic.setImageResource(R.drawable.ic_profile);
                        //TODO make these real values
                        userResponsible.setText(username);
                        if (newSnap.child("Frequency").child("Due Date").exists())
                            dueDate.setText("Due By: " + newSnap.child("Frequency").child("Due Date").getValue().toString());

                        //setupCheckbox(complete, true);
                        choreButton.setLayoutParams(params);
                        choreLL.addView(choreButton);


                        complete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if(isChecked){

                                    Thread thread = new Thread(){
                                        @Override
                                        public void run(){

                                            try{
                                                Thread.sleep(1000);
                                            }catch(InterruptedException e)
                                            {

                                            }

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    complete.setChecked(false);
                                                    oldDueDate = newSnap.child("Frequency").child("Due Date").getValue().toString();
                                                    newDueDate = Chore.updateDueDate(newSnap, newSnap.getKey().toString());
                                                    if(!oldDueDate.equals(newDueDate)) {
                                                        database.getReference("Users/" + username + "/Chores/" + choreName.getText().toString() + "/Frequency/Due Date").setValue(newDueDate);

                                                    }

                                                    final DatabaseReference StatRef = database.getReference("Users/" + username + "/Statistics/Chores Completed");
                                                    StatRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                                            int choresCompleted = 0;
                                                            if(dataSnapshot.exists()){
                                                                choresCompleted = Integer.parseInt(dataSnapshot.getValue().toString());

                                                            }
                                                            if(!oldDueDate.equals(newDueDate)) {
                                                                Log.d("Old due", oldDueDate);
                                                                Log.d("New due", newDueDate);

                                                                choresCompleted++;
                                                                StatRef.setValue(choresCompleted);
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });

                                                }
                                            });
                                        }
                                    };

                                    thread.start();




                                }

                            }
                        });


                    }
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

                   // Toast.makeText(view.getContext(), "Button clicked index: " +
                     //       (id_-500), Toast.LENGTH_SHORT).show();
                    Log.d("Key", b.getString("GroupKey"));
                }
            });


        }
    }

    private void makeChoreList(){

        final LinearLayout layout = findViewById(R.id.chore_layout);
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
                public void onClick(final View view) {

                    Thread thread = new Thread(){
                        @Override
                        public void run(){

                            try{
                                Thread.sleep(500);
                            }catch(InterruptedException e)
                            {

                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    database.getReference().child("Users").child(username).child("Chores").child(choreNames.get(id_)).removeValue();
                                    layout.removeView((View) findViewById(id_));

                                    Toast.makeText(view.getContext(), "Completed " + choreNames.get(id_),
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    };

                    thread.start();
                   // Log.d("Key", b.getString("GroupKey"));
                }
            });

        }
    }



    public void makeTaskList(){

        final LinearLayout layout = findViewById(R.id.task_layout);

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
                public void onClick(final View view) {


                    Thread thread = new Thread(){
                        @Override
                        public void run(){

                            try{
                                Thread.sleep(500);
                            }catch(InterruptedException e)
                            {

                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    database.getReference().child("Users").child(username).child("Tasks").child(taskNames.get(id_)).removeValue();
                                    layout.removeView((View) findViewById(id_));
                                    Toast.makeText(view.getContext(), "Completed " + taskNames.get(id_),
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    };

                    thread.start();



                }
            });

        }
    }



    @Override
    public boolean onCreateOptionsMenu(final Menu menu){

        this.menu = menu;

        getMenuInflater().inflate(R.menu.action_bar_buttons, menu);

        DatabaseReference notifRef = database.getReference("Users/" + username + "/Invites");

        notifRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MenuItem item = menu.findItem(R.id.action_notification);
                if(dataSnapshot.exists()){

                    item.setIcon(R.drawable.ic_notification_bell_ring);
                }
                else {

                    item.setIcon(R.drawable.ic_notification_bell);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the matt.housekeep.Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Intent intent;
        Bundle b = new Bundle();
        b.putString("GroupName", "");
        b.putString("UserName", username);
        b.putBoolean("inGroup", false);

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_notification) {
           // Toast.makeText(HomeActivity.this, "Action clicked", Toast.LENGTH_LONG).show();
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
