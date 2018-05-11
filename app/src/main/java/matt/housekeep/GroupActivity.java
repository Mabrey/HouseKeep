package matt.housekeep;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GroupActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private BottomNavigationView bottomNavigationView;
    private String groupname;
    private String username;
    private String groupKey;
    private ArrayList<String> taskNames;
    private ArrayList<String> choreNames;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getExtras();
        groupname = b.getString("GroupName");
        groupKey = b.getString("GroupKey");
        username = b.getString("UserName");

        //sets title in ActionBar to the groupname stored in the bundle
        setTitle(groupname);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.GroupBottomNavView);

        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        initChores();
        initTasks();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Intent intent;
            Bundle b = new Bundle();
            b.putString("GroupName", groupname);
            b.putString("GroupKey", groupKey);
            b.putBoolean("inGroup", true);

            switch (item.getItemId()) {
                case R.id.menu_home:
                    startActivity(new Intent(GroupActivity.this, HomeActivity.class));
                    return true;
                case R.id.menu_create_task:
                    intent = new Intent(GroupActivity.this, CreateTaskActivity.class);
                    intent.putExtras(b);
                    startActivity(intent);
                    return true;
                case R.id.menu_profile:
                    intent = new Intent(GroupActivity.this, UserProfileActivity.class);
                    intent.putExtras(b);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };

    private void initTasks(){

        final LinearLayout taskLL = (LinearLayout)findViewById(R.id.group_task_layout);

        final DatabaseReference myRef = database.getReference("Groups/" + groupKey + "/Tasks");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

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
                                                    database.getReference("Groups/" + groupKey + "/Tasks/" + taskName.getText()).setValue(null);

                                                    final DatabaseReference StatRef = database.getReference("Groups/" + groupKey + "/Statistics/Tasks Completed");
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
                                            });
                                        }
                                    };

                                    thread.start();



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

    private void initChores(){

        final LinearLayout choreLL = (LinearLayout)findViewById(R.id.group_chore_layout);

        DatabaseReference myRef = database.getReference("Groups/" + groupKey + "/Chores");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

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
                        final Button clickChore = choreButton.findViewById(R.id.chore_click_button);

                        choreName.setText(newSnap.getKey().toString());
                        profilePic.setImageResource(R.drawable.ic_profile);
                        //TODO make these real values
                        if (newSnap.child("Rotation/Next + 0").exists())
                            userResponsible.setText(newSnap.child("Rotation/Next + 0").getValue().toString());

                        else userResponsible.setText("No User In Rotation");

                        if (newSnap.child("Frequency").child("Due Date").exists())
                            dueDate.setText(newSnap.child("Frequency").child("Due Date").getValue().toString());

                                complete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                if(isChecked && username.equals(userResponsible.getText().toString())){

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
                                                    ArrayList<String> nextOrder = new ArrayList<String>();
                                                    ArrayList<String> nextOrderName = new ArrayList<String>();
                                                    for (DataSnapshot rotSnap: newSnap.child("Rotation").getChildren()){
                                                        nextOrder.add(rotSnap.getKey().toString());
                                                        nextOrderName.add(rotSnap.getValue().toString());
                                                    }
                                                    String[] next = new String[nextOrder.size()];
                                                    next = nextOrder.toArray(next);

                                                    String[] nextName = new String[nextOrderName.size()];
                                                    nextName = nextOrderName.toArray(nextName);

                                                    ChoreRotation choreRotate = new ChoreRotation(next, nextName);
                                                    ChoreRotation newRotate = Chore.rotateChore(choreRotate);
                                                    String newDueDate = Chore.updateDueDate(newSnap, newSnap.getKey().toString());


                                                    //choreLL.removeView(choreButton);
                                                    database.getReference("Groups/" + groupKey + "/Chores/" + choreName.getText().toString() + "/Frequency/Due Date").setValue(newDueDate);
                                                    for(int i = 0; i < nextName.length ; i++)
                                                    {
                                                        database.getReference("Groups/" + groupKey + "/Chores/" + choreName.getText().toString() + "/Rotation/Next + " + i).setValue(newRotate.nextOrderName[i]);
                                                    }

                                                    final DatabaseReference StatRef = database.getReference("Groups/" + groupKey + "/Statistics/Chores Completed");
                                                    StatRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                                            int choresCompleted = 0;
                                                            if(dataSnapshot.exists()){
                                                                choresCompleted = Integer.parseInt(dataSnapshot.getValue().toString());

                                                            }
                                                            choresCompleted++;
                                                            StatRef.setValue(choresCompleted);
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
                                else complete.setChecked(false);
                            }
                        });

                                clickChore.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Intent intent;
                                        Bundle b = new Bundle();
                                        b.putString("UserName", username);
                                        b.putString("GroupName", groupname);
                                        b.putString("GroupKey", groupKey);
                                        b.putBoolean("inGroup", true);
                                        b.putString("ChoreName", choreName.getText().toString());

                                        intent = new Intent(GroupActivity.this, ChoreActivity.class);
                                        intent.putExtras(b);
                                        startActivity(intent);

                                    }
                                });
                                choreButton.setLayoutParams(params);
                                choreLL.addView(choreButton);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void confirmLeave(){

        AlertDialog.Builder builder = new AlertDialog.Builder(GroupActivity.this);
        builder.setCancelable(true);
        builder.setTitle("Are you sure?");
        builder.setMessage("Warning: This cannot be undone");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        database.getReference("Groups/" + groupKey + "/Members/" + username).setValue(null);
                        database.getReference("Users/" + username + "/Groups/" + groupKey).setValue(null);

                        Intent intent;
                        Bundle b = new Bundle();
                        b.putString("GroupName", groupname);
                        b.putString("UserName", username);
                        b.putBoolean("inGroup", true);

                        intent = new Intent(GroupActivity.this, HomeActivity.class);
                        startActivity(intent);

                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu){

        this.menu = menu;
        getMenuInflater().inflate(R.menu.group_action_bar_buttons, menu);

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
        b.putString("GroupName", groupname);
        b.putString("UserName", username);
        b.putString("GroupKey", groupKey);
        b.putBoolean("inGroup", true);

        //noinspection SimplifiableIfStatement
        if(id == R.id.group_info) {
            intent = new Intent(GroupActivity.this, GroupInfoActivity.class);
            intent.putExtras(b);
            startActivity(intent);
            return true;
        }

        else if (id == R.id.invite_member) {
            intent = new Intent(GroupActivity.this, InviteMembersActivity.class);
            intent.putExtras(b);
            startActivity(intent);
            return true;
        }

        else if(id == R.id.leave_group){

            DatabaseReference userRef = database.getReference("Users/" + username + "/Groups");
            DatabaseReference groupsRef = database.getReference("Groups/" + groupKey);

            groupsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot.child("Owner").getKey() == username){
                        Toast.makeText(GroupActivity.this, "Must Transfer Ownership", Toast.LENGTH_LONG);
                    }

                    else {

                        confirmLeave();

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        return super.onOptionsItemSelected(item);
    }
}
