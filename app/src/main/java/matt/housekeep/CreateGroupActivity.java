package matt.housekeep;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreateGroupActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private int addMemberCount = 0;
    private SharedPreferences prefs;

    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = this.getSharedPreferences(
                getString(R.string.shared_prefs_key), Context.MODE_PRIVATE);

        setContentView(R.layout.activity_create_group);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final LinearLayout inviteMembers = findViewById(R.id.inviteLinear);

        ImageButton addMember = findViewById(R.id.addMembers);
        final ImageButton removeMember = findViewById(R.id.removeMember);
        removeMember.setVisibility(View.GONE);

        addMember.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                addMemberCount++;
                inviteMembers.addView(createNewEditText(addMemberCount));
                if (addMemberCount > 0)
                    removeMember.setVisibility(View.VISIBLE);
            }
        });

        removeMember.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(addMemberCount > 0){
                    inviteMembers.removeViewAt(addMemberCount);
                    addMemberCount--;
                    if (addMemberCount < 1 )
                        removeMember.setVisibility(View.GONE);

                }

            }
        });

        Button createGroup = findViewById(R.id.CreateGroupButton);
        createGroup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final DatabaseReference createGroupDatabase;
                createGroupDatabase = FirebaseDatabase.getInstance().getReference();
                final EditText groupName = findViewById(R.id.GroupName);

                final String groupID = createGroupDatabase.child("Groups").push().getKey();
                createGroupDatabase.child("Groups").child(groupID).child("Name").setValue(groupName.getText().toString()); //this is causing the crash

                String ownerOfGroup = prefs.getString(getString(R.string.saved_username_key), "");
                createGroupDatabase.child("Groups").child(groupID).child("Owner").child(ownerOfGroup).setValue("Owner");
                createGroupDatabase.child("Users").child(ownerOfGroup).child("Groups").child(groupID).setValue(groupName.getText().toString());


                for(int i = 0; i <= addMemberCount; i++)
                {
                    final EditText inviteUser = (EditText) inviteMembers.getChildAt(i);

                    DatabaseReference userRef = database.getReference();
                    userRef.child("Users").child(inviteUser.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override

                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //check if username exists.
                            if (dataSnapshot.exists()) {
                                String invUser = inviteUser.getText().toString();
                                createGroupDatabase.child("Groups").child(groupID).child("Members").child(inviteUser.getText().toString()).setValue("Pending");
                                createGroupDatabase.child("Users").child(invUser).child("Invites").child(groupID).child("Name").setValue(groupName.getText().toString());
                                createGroupDatabase.child("Users").child(invUser).child("Invites").child(groupID).child("Status").setValue("Pending");
                                Toast.makeText(getApplicationContext(), "Group Created" , Toast.LENGTH_SHORT).show();
                            } else { //username doesn't exist
                                Toast.makeText(getApplicationContext(), "Invalid Username Entered" , Toast.LENGTH_SHORT).show();
                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
                finish();
            }
        });

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_home:
                    startActivity(new Intent(CreateGroupActivity.this, HomeActivity.class));
                    return true;
                case R.id.menu_create_task:
                    //Maybe contextually turn this into a different button/remove on this screen
                    //startActivity(new Intent(CreateTaskActivity.this, CreateTaskActivity.class));
                    return true;
                case R.id.menu_profile:
                    startActivity(new Intent(CreateGroupActivity.this, UserProfileActivity.class));
                    return true;
            }
            return false;
        }
    };

    private void addMembers() {
        final LinearLayout inviteMembers = findViewById(R.id.inviteLinear);

        ImageButton addMember = findViewById(R.id.addMembers);
        addMember.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                addMemberCount++;
                Toast.makeText(CreateGroupActivity.this, "Action clicked", Toast.LENGTH_LONG).show();
                inviteMembers.addView(createNewEditText(addMemberCount));
            }
        });
    }

    private EditText createNewEditText(int memberCount) {
        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final EditText newMem = new EditText(this);
        newMem.setLayoutParams(lparams);
        newMem.setHint("Username");
        newMem.setId(R.id.member0 + memberCount);
        return newMem;
    }
}
