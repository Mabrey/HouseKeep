package matt.housekeep;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private SharedPreferences prefs;
    private String groupname;
    private boolean inGroup;
    private TextView name;
    private TextView username;
    private TextView choresCompleted;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = (TextView)findViewById(R.id.retrieved_name);
        username = (TextView)findViewById(R.id.retrieved_username);
        choresCompleted = (TextView)findViewById(R.id.retrieved_chores_completed);

        prefs = this.getSharedPreferences(getString(R.string.shared_prefs_key), Context.MODE_PRIVATE);
        username.setText(prefs.getString(getString(R.string.saved_username_key), ""));

        final DatabaseReference nameRef = database.getReference("Users/" + username.getText() + "/Name");
        nameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        Bundle b = getIntent().getExtras();
        inGroup = b.getBoolean("inGroup");
        groupname = b.getString("GroupName");


        Button signOut= (Button) findViewById(R.id.sign_out);
        signOut.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                prefs.edit().putString(getString(R.string.saved_username_key), "").apply();
                prefs.edit().putString(getString(R.string.loggedIn), "false").apply();
                Intent intent = new Intent(UserProfileActivity.this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView);


        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Intent intent;
            Bundle b = new Bundle();
            b.putString("GroupName", groupname);
            b.putBoolean("inGroup", inGroup);

            switch (item.getItemId()) {
                case R.id.menu_home:
                    startActivity(new Intent(UserProfileActivity.this, HomeActivity.class));
                    return true;
                case R.id.menu_create_task:
                    //Maybe contextually turn this into a different button/remove on this screen
                    intent = new Intent(UserProfileActivity.this, CreateTaskActivity.class);
                    intent.putExtras(b);
                    startActivity(intent);                    return true;
                case R.id.menu_profile:
                    //startActivity(new Intent(UserProfileActivity.this, UserProfileActivity.class));
                    return true;
            }
            return false;
        }
    };

}
