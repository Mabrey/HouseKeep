package matt.housekeep;

import android.app.Activity;
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
import android.view.LayoutInflater;
import android.view.MenuItem;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private SharedPreferences prefs;
    private ArrayList<InviteMessage> invites = new ArrayList<>();
    private String username;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Intent intent;
            Bundle b = new Bundle();
            b.putString("GroupName", "");
            b.putBoolean("inGroup", false);

            switch (item.getItemId()) {
                case R.id.menu_home:

                    startActivity(new Intent(NotificationActivity.this, HomeActivity.class));
                    return true;
                case R.id.menu_create_task:
                    intent = new Intent(NotificationActivity.this, CreateTaskActivity.class);
                    intent.putExtras(b);
                    startActivity(intent);
                    return true;
                case R.id.menu_profile:
                    intent = new Intent(NotificationActivity.this, UserProfileActivity.class);
                    intent.putExtras(b);
                    startActivity(intent);
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
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        ArrayAdapter<InviteMessage> adapter = new propertyArrayAdapter(this, 0, invites);
        ListView listView = (ListView) findViewById(R.id.customListView);
        listView.setAdapter(adapter);

        //initNotifs();
    }

    private void initNotifs(){

        prefs = this.getSharedPreferences(
                getString(R.string.shared_prefs_key), Context.MODE_PRIVATE);

        username = prefs.getString(getString(R.string.saved_username_key), "");
        DatabaseReference myRef = database.getReference("Users/" + username + "/Chores");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot newSnap: dataSnapshot.getChildren()){
                        invites.add(new InviteMessage(newSnap.getValue().toString(),newSnap.getKey().toString()));
                    }


                  //  makeInviteList();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Bundle b = new Bundle();
        b.putString("GroupName", "");
        b.putString("UserName", username);
        b.putBoolean("inGroup", false);
    }

    /*
    private void makeInviteList(){

        LinearLayout layout = findViewById(R.id.ScrollLin);

        for(int i = 0; i < invites.size(); i++){

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            Button btn = new Button(this);
            btn.setTextSize(18);
            btn.setId(i);
            final int id_ = btn.getId();
            btn.setText(invites.get(id_));
            layout.addView(btn, params);

            btn = (findViewById(id_));
            final Button finalBtn = btn;
            btn.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view) {
                    DatabaseReference myRef = database.getInstance().getReference();
                    //myRef.child("Groups").child().

                    Log.d("Key", finalBtn.getText().toString());
                }
            });

        }
    }
*/

    class propertyArrayAdapter extends ArrayAdapter<InviteMessage> {

        private Context context;
        private List<InviteMessage> inviteMessageList;

        //constructor, call on creation
        public propertyArrayAdapter(Context context, int resource, ArrayList<InviteMessage> objects) {
            super(context, resource, objects);

            this.context = context;
            this.inviteMessageList = objects;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            //get the property we are displaying
            InviteMessage invites = inviteMessageList.get(position);

            //get the inflater and inflate the XML layout for each item
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.invitation_button, null);

            TextView groupName = (TextView) view.findViewById(R.id.inviteButtonGroupName);
            groupName.setText(invites.getGroupName());

            return view;
        }
    }
}
