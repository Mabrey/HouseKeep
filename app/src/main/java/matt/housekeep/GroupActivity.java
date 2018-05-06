package matt.housekeep;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
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

        taskNames = new ArrayList<>();
        DatabaseReference myRef = database.getReference("Groups/" + groupKey + "/Tasks");
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
        DatabaseReference myRef = database.getReference("Groups/" + groupKey + "/Chores");
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

    private void makeChoreList(){

        LinearLayout layout = findViewById(R.id.group_chore_layout);
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

                    database.getReference().child("Groups").child(groupKey).child("Chores").child(choreNames.get(id_)).removeValue();
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
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Intent intent;
        Bundle b = new Bundle();
        b.putString("GroupName", groupname);
        b.putString("UserName", username);
        b.putBoolean("inGroup", true);

        //noinspection SimplifiableIfStatement
        if (id == R.id.invite_member) {
            intent = new Intent(GroupActivity.this, InviteMembersActivity.class);
            intent.putExtras(b);
            startActivity(intent);
            return true;
        }

        if(id == R.id.leave_group){

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
