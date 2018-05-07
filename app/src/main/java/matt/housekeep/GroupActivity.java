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

                        complete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if(isChecked){
                                    taskLL.removeView(taskButton);
                                    database.getReference("Groups/" + groupKey + "/Tasks/" + taskName.getText()).setValue(null);
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
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    choreLL.removeAllViews();
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0,10, 0,10);


                    for(DataSnapshot newSnap: dataSnapshot.getChildren()){

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
                        userResponsible.setText("No User In Rotation");
                        if (newSnap.child("Frequency").child("Due Date").exists())
                            dueDate.setText(newSnap.child("Frequency").child("Due Date").getValue().toString());

                                complete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if(isChecked){
                                    //choreLL.removeView(choreButton);
                                    //database.getReference("Groups/" + groupKey + "/Chores/" + choreName.getText()).setValue(null);

                                }
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

    public void makeTaskList(){

        LinearLayout layout = findViewById(R.id.group_task_layout);

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

                    database.getReference().child("Groups").child(groupKey).child("Tasks").child(taskNames.get(id_)).removeValue();
                    Toast.makeText(view.getContext(), "Completed " + taskNames.get(id_),
                            Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(getIntent());
                    // Log.d("Key", b.getString("GroupKey"));
                }
            });

        }
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
