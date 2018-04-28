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
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private SharedPreferences prefs;
    private ArrayList<String> notifButtons;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_home:
                    startActivity(new Intent(NotificationActivity.this, HomeActivity.class));
                    return true;
                case R.id.menu_create_task:
                    startActivity(new Intent(NotificationActivity.this, CreateTaskActivity.class));
                    return true;
                case R.id.menu_profile:
                    startActivity(new Intent(NotificationActivity.this, UserProfileActivity.class));
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //checkInvites();

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView);

        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void setnotifs(ArrayList<String> notifs){
        this.notifButtons = notifs;
    }
/*
    private void init(){

        //create/get shared prefs file
        prefs = this.getSharedPreferences(
                getString(R.string.shared_prefs_key), Context.MODE_PRIVATE);

        DatabaseReference myRef = database.getReference("Users/" + username + "/Groups");

        final ArrayList<String> groups = new ArrayList<>();
        final ArrayList<String> groupKeys = new ArrayList<>();


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

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

    private void checkInvites() {
        prefs = this.getSharedPreferences(
                getString(R.string.shared_prefs_key), Context.MODE_PRIVATE);
        String username = prefs.getString(getString(R.string.saved_username_key), "");
        DatabaseReference inviteRef = database.getReference("Users/" + username);
        final ScrollView notifScroll = findViewById(R.id.notifScroll);
        inviteRef.child("Invites").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {
                //check if username exists.
                if (dataSnapshot.exists()) {
                    int count = (int)dataSnapshot.getChildrenCount();
                    int notifBut = 200;
                    int id = notifBut;
                    int i = 0;
                    for(DataSnapshot snap : dataSnapshot.getChildren()){
                    notifScroll.addView(createNewNotifButton(i));
                    i++;
                    }
                } else { //username doesn't exist

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private View createNotifButton() {
        LinearLayout ll = findViewById(R.id.GroupLayout);

        for(int i = 0; i < groups.size(); i++){


            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            Button btn = new Button(this);
            btn.setTextSize(18);
            btn.setId(i);
            final int id_ = btn.getId();
            btn.setText(groups.get(id_));
            ll.addView(btn, params);

            btn = (findViewById(id_));
            btn.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view) {

                    //Need to dynamically create a new activity with groups[id_]'s info

                    Intent intent = new Intent(HomeActivity.this, GroupActivity.class);
                    Bundle b = new Bundle();
                    b.putString("GroupName", groups.get(id_));
                    b.putString("GroupKey", groupKeys.get(id_));
                    b.putString("UserName", username);
                    intent.putExtras(b);
                    startActivity(intent);

                    Toast.makeText(view.getContext(), "Button clicked index: " +
                            id_, Toast.LENGTH_SHORT).show();
                    Log.d("Key", b.getString("GroupKey"));
                }
            });


        }
    }
*/
}
 