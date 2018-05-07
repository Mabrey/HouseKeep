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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChoreActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private String groupname;
    private String username;
    private String groupKey;
    private String choreName;
    private TextView description;
    private ArrayList<String> members = new ArrayList<String>();

    //TODO retrieve description from chore in database and display in textview

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chore);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle b = getIntent().getExtras();
        groupname = b.getString("GroupName");
        groupKey = b.getString("GroupKey");
        username = b.getString("UserName");
        choreName = b.getString("ChoreName");

        setTitle(choreName);
        initMembers();
        joinRotationButton();
        getDescription();

    }

    private void getDescription(){

        description = findViewById(R.id.description_textbox);
        final TextView descriptionLabel = findViewById(R.id.description_label);
        final DatabaseReference choreRef = database.getReference("Groups/" + groupKey
                + "/Chores/" + choreName);
        choreRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String text;
                    if(dataSnapshot.child("Description").exists()){
                        text = dataSnapshot.child("Description").getValue().toString();
                        if(text.equals("")){
                            description.setVisibility(View.GONE);
                            descriptionLabel.setVisibility(View.GONE);
                        }
                        else {
                            description.setVisibility(View.VISIBLE);
                            descriptionLabel.setVisibility(View.VISIBLE);
                            description.setText(text);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void joinRotationButton(){

        Button join = findViewById(R.id.join_rotation_button);
        final DatabaseReference rotationRef = database.getReference("Groups/" + groupKey + "/Chores/Rotation");

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // database.getReference("Groups/" + groupKey + "/Chores/" + choreName
                       // + "/Rotation/" + username).setValue("true");
                if(!members.contains(username)) {
                    members.add(username);
                    updateRotation();
                }
            }
        });
    }

    private void updateRotation() {
        for (int i = 0; i < members.size(); i++)
        {
            database.getReference("Groups/" + groupKey + "/Chores/" + choreName
                     + "/Rotation/Next + " + i).setValue(members.get(i));
        }
    }

    private void initRotation(){

        DatabaseReference rotationRef = database.getReference("Groups/" + groupKey + "/Chores/Rotation");

        rotationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    for(DataSnapshot newSnap : dataSnapshot.getChildren()){
                        //TODO need to set key to next+X
                        members.add(newSnap.getValue().toString());
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void initMembers(){

        //members = new ArrayList<>();

        DatabaseReference memberRef = database.getReference("Groups/" + groupKey + "/Chores/"
                + choreName + "/Rotation");

        memberRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()) {
                    members = new ArrayList<>();

                    for (DataSnapshot newSnap : dataSnapshot.getChildren()) {
                        members.add(newSnap.getValue().toString());
                    }
                    Log.d("finding info", "Finding Member Info");

                    getMembers();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getMembers(){

        DatabaseReference userRef = database.getReference("Users");
        final LinearLayout rotationLL = findViewById(R.id.rotation_layout);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    Log.d("Member Info", "Snapshot exists");
                    rotationLL.removeAllViews();

                    for(int i = 0; i < members.size(); i++){

                        Log.d("Chore Username", members.get(i).toString());
                        DataSnapshot newSnap = dataSnapshot.child(members.get(i).toString());
                        final View memberButton = LayoutInflater.from(getApplicationContext())
                                .inflate(R.layout.user_profile_button, rotationLL, false);

                        final TextView personName = memberButton.findViewById(R.id.member_name);
                        final TextView memberUsername = memberButton.findViewById(R.id.username_textfield);



                        personName.setText(newSnap.child("Name").getValue().toString());
                        memberUsername.setText(members.get(i));
                        Log.d("Person Name", newSnap.child("Name").getValue().toString());
                        ImageView icon = memberButton.findViewById(R.id.profilePicture);
                        icon.setImageResource(R.drawable.ic_profile);

                        rotationLL.addView(memberButton);
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
            b.putString("GroupName", groupname);
            b.putString("GroupKey", groupKey);
            b.putBoolean("inGroup", true);

            switch (item.getItemId()) {
                case R.id.menu_home:
                    startActivity(new Intent(ChoreActivity.this, HomeActivity.class));
                    return true;
                case R.id.menu_create_task:
                    intent = new Intent(ChoreActivity.this, CreateTaskActivity.class);
                    intent.putExtras(b);
                    startActivity(intent);
                    return true;
                case R.id.menu_profile:
                    intent = new Intent(ChoreActivity.this, UserProfileActivity.class);
                    intent.putExtras(b);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };

}
