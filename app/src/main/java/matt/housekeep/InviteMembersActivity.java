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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InviteMembersActivity extends AppCompatActivity {

    private String groupname;
    private String groupKey;
    private String username;
    private int addMemberCount = 0;
    private BottomNavigationView bottomNavigationView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_members);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Invite Members");

        Bundle b = getIntent().getExtras();
        groupname = b.getString("GroupName");
        groupKey = b.getString("GroupKey");
        username = b.getString("UserName");

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
                                Log.d("Invite User: ", invUser);
                                database.getReference().child("Groups").child(groupKey).child("Members").child(inviteUser.getText().toString()).setValue("Pending");
                                database.getReference().child("Users").child(invUser).child("Invites").child(groupKey).child("Name").setValue(groupname);
                                database.getReference().child("Users").child(invUser).child("Invites").child(groupKey).child("Status").setValue("Pending");
                                Toast.makeText(getApplicationContext(), "User(s) Invited" , Toast.LENGTH_SHORT).show();
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

    public EditText createNewEditText(int memberCount) {
        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final EditText newMem = new EditText(this);
        newMem.setLayoutParams(lparams);
        newMem.setHint("Username");
        newMem.setId(R.id.member0 + memberCount);
        return newMem;
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
                    startActivity(new Intent(InviteMembersActivity.this, HomeActivity.class));
                    return true;
                case R.id.menu_create_task:
                    intent = new Intent(InviteMembersActivity.this, CreateTaskActivity.class);
                    intent.putExtras(b);
                    startActivity(intent);
                    return true;
                case R.id.menu_profile:
                    intent = new Intent(InviteMembersActivity.this, UserProfileActivity.class);
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

            intent = new Intent(InviteMembersActivity.this, GroupActivity.class);
            intent.putExtras(b);
            startActivity(intent);

            return true;
        } else {
            // handle other items here
        }
        // return true if you handled the button click, otherwise return false.
        return true;
    }

}
