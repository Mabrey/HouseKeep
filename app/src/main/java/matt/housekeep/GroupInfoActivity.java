package matt.housekeep;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GroupInfoActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private String username;
    private String groupname;
    private String groupKey;
    private ArrayList<String> members;
    private String owner;
    private TextView numberOfMembers;
    private TextView numOfChores;
    private int chores;
    private TextView numOfChoresCompleted;
    private String choresCompleted;
    private TextView numOfTasks;
    private int tasks;
    private TextView numOfTasksCompleted;
    private String tasksCompleted;
    private LinearLayout ownerLL;
    private LinearLayout memberLL;

    private void findMemberInfo(){

        ownerLL = (LinearLayout)findViewById(R.id.ownerLLayout);
        memberLL = (LinearLayout)findViewById(R.id.memberLLayout);

        DatabaseReference userRef = database.getReference("Users");

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    Log.d("Member Info", "Snapshot exists");
                    ownerLL.removeAllViews();
                    memberLL.removeAllViews();

                    for(int i = 0; i < members.size(); i++){

                        DataSnapshot newSnap = dataSnapshot.child(members.get(i));
                        final View memberButton = LayoutInflater.from(getApplicationContext()).inflate(R.layout.user_profile_button, memberLL, false);

                        final TextView personName = memberButton.findViewById(R.id.member_name);
                        final TextView memberUsername = memberButton.findViewById(R.id.username_textfield);

                        personName.setText(newSnap.child("Name").getValue().toString());
                        memberUsername.setText(members.get(i));
                        Log.d("Person Name", newSnap.child("Name").getValue().toString());
                        ImageView icon = memberButton.findViewById(R.id.profilePicture);
                        icon.setImageResource(R.drawable.ic_profile);
                        Log.d("Member Count", String.valueOf(members.size()));
                        if(memberLL.getChildCount() < members.size())
                            memberLL.addView(memberButton);
                    }

                    final View ownerButton = LayoutInflater.from(getApplicationContext()).inflate(R.layout.user_profile_button, memberLL, false);
                    DataSnapshot newSnap = dataSnapshot.child(owner);

                    final TextView personName = ownerButton.findViewById(R.id.member_name);
                    final TextView ownerUsername = ownerButton.findViewById(R.id.username_textfield);

                    personName.setText(newSnap.child("Name").getValue().toString());
                    ownerUsername.setText(owner);
                    ImageView icon = ownerButton.findViewById(R.id.profilePicture);
                    icon.setImageResource(R.drawable.ic_profile);

                    ownerLL.addView(ownerButton);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void initMembers(){

        numberOfMembers = findViewById(R.id.number_of_members);
        //members = new ArrayList<>();

        DatabaseReference memberRef = database.getReference("Groups/" + groupKey);
        DatabaseReference membersREf = database.getReference("Groups/" + groupKey + "/Members");

        memberRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()) {
                    members = new ArrayList<>();

                    for (DataSnapshot newSnap : dataSnapshot.child("Members").getChildren()) {
                        members.add((String) newSnap.getKey());
                    }
                    for (DataSnapshot newSnap : dataSnapshot.child("Owner").getChildren()) {
                        owner = newSnap.getKey();
                    }
                    Log.d("finding info", "Finding Member Info");
                    findMemberInfo();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        membersREf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {

                    numberOfMembers.setText(String.valueOf(members.size()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void initStatistics(){

        numOfChores = findViewById(R.id.number_of_active_chores);
        numOfTasks = findViewById(R.id.number_of_active_tasks);
        numOfChoresCompleted = findViewById(R.id.number_of_chores_completed);
        numOfTasksCompleted = findViewById(R.id.num_of_tasks_completed);

        DatabaseReference myRef = database.getReference("Groups/" + groupKey + "/Chores");
        DatabaseReference taskRef = database.getReference("Groups/" + groupKey + "/Tasks");
        DatabaseReference statsRef = database.getReference("Groups/" + groupKey + "/Statistics");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {

                    chores = 0;
                    for (DataSnapshot newSnap : dataSnapshot.getChildren()) {
                        chores++;
                    }
                    numOfChores.setText(String.valueOf(chores));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        taskRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {

                    tasks = 0;
                    for (DataSnapshot newSnap : dataSnapshot.getChildren()) {
                        tasks++;
                    }
                    numOfTasks.setText(String.valueOf(tasks));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        statsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.child("Tasks Completed").exists()){
                        tasksCompleted = dataSnapshot.child("Tasks Completed").getValue().toString();
                        numOfTasksCompleted.setText(tasksCompleted);
                    }
                    else {
                        database.getReference("Groups/" + groupKey + "/Statistics/Tasks Completed").setValue("0");
                        numOfTasksCompleted.setText("0");
                    }

                    if(dataSnapshot.child("Chores Completed").exists()){
                        choresCompleted = dataSnapshot.child("Chores Completed").getValue().toString();
                        numOfChoresCompleted.setText(choresCompleted);
                    }
                    else {
                        database.getReference("Groups/" + groupKey + "/Statistics/Chores Completed").setValue("0");
                        numOfChoresCompleted.setText("0");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Intent intent;
            Bundle b = new Bundle();
            b.putString("Username", username);
            b.putString("GroupName", groupname);
            b.putString("GroupKey", groupKey);
            b.putBoolean("inGroup", true);

            switch (item.getItemId()) {
                case R.id.menu_home:
                    startActivity(new Intent(GroupInfoActivity.this, HomeActivity.class));
                    return true;
                case R.id.menu_create_task:
                    intent = new Intent(GroupInfoActivity.this, CreateTaskActivity.class);
                    intent.putExtras(b);
                    startActivity(intent);
                    return true;
                case R.id.menu_profile:
                    intent = new Intent(GroupInfoActivity.this, UserProfileActivity.class);
                    intent.putExtras(b);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent;
        Bundle b = new Bundle();
        b.putString("GroupName", groupname);
        b.putString("UserName", username);
        b.putString("GroupKey", groupKey);
        b.putBoolean("inGroup", true);

        if (item.getItemId() == android.R.id.home) {
            // Handle "up" button behavior here.

            intent = new Intent(GroupInfoActivity.this, GroupActivity.class);
            intent.putExtras(b);
            startActivity(intent);

            return true;
        } else {
            // handle other items here
        }
        // return true if you handled the button click, otherwise return false.
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //bottomNavigationView = (BottomNavigationView) findViewById(R.id.GroupBottomNavView);

        //bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Bundle b = getIntent().getExtras();
        groupname = b.getString("GroupName");
        groupKey = b.getString("GroupKey");
        username = b.getString("UserName");

        setTitle(groupname + "'s Info");

        initMembers();
        initStatistics();

    }


}
