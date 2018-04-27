package matt.housekeep;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

public class CreateGroupActivity extends AppCompatActivity {
    private int addMemberCount = 0;
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                Toast.makeText(CreateGroupActivity.this, "Add Member Count:" + addMemberCount, Toast.LENGTH_LONG).show();
                inviteMembers.addView(createNewEditText(addMemberCount));
                if (addMemberCount > 0)
                    removeMember.setVisibility(View.VISIBLE);
            }
        });



        removeMember.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(addMemberCount > 0){

                    int removeId = R.id.member0 + addMemberCount;
                    //View remove = view.findViewById(removeId);
                    inviteMembers.removeViewAt(addMemberCount);
                    addMemberCount--;
                    if (addMemberCount < 1 )
                        removeMember.setVisibility(View.GONE);
                    Toast.makeText(CreateGroupActivity.this, "Add Member Count:" + addMemberCount, Toast.LENGTH_LONG).show();
                }

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
