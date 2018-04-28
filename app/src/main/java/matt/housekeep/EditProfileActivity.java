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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Button changeName;
    private Button changeUsername;
    private Button confirmName;
    //private Button confirmUsername;
    //private Button changeProfilePic;
    private EditText nameInputBox;
    //private EditText usernameInputBox;
    private String username;
    private String name;
    private SharedPreferences prefs;
    FirebaseDatabase database = FirebaseDatabase.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Edit Profile");

        prefs = this.getSharedPreferences(
                getString(R.string.shared_prefs_key), Context.MODE_PRIVATE);

        username = prefs.getString(getString(R.string.saved_username_key), "");

        final DatabaseReference nameRef = database.getReference("Users/" + username + "/Name");
        nameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name = dataSnapshot.getValue().toString();
                nameInputBox.setText(name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        changeName = (Button)findViewById(R.id.change_name_button);
        //changeUsername = (Button)findViewById(R.id.change_username_button);
        confirmName = (Button)findViewById(R.id.name_confirm_button);
        //confirmUsername = (Button)findViewById(R.id.username_confirm_button);
        nameInputBox = (EditText)findViewById(R.id.name_input_box);
        //usernameInputBox = (EditText)findViewById(R.id.username_input_box);

        nameInputBox.setVisibility(View.GONE);
        //usernameInputBox.setVisibility(View.GONE);
        confirmName.setVisibility(View.GONE);
        //confirmUsername.setVisibility(View.GONE);

        //usernameInputBox.setText(username);

        changeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(nameInputBox.getVisibility() == View.VISIBLE) {

                    nameInputBox.setVisibility(View.GONE);
                    confirmName.setVisibility(View.GONE);
                }
                else {

                    nameInputBox.setVisibility(View.VISIBLE);
                    confirmName.setVisibility(View.VISIBLE);
                }

            }
        });

        /*
        changeUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(usernameInputBox.getVisibility() == View.VISIBLE) {

                    usernameInputBox.setVisibility(View.GONE);
                    confirmUsername.setVisibility(View.GONE);
                }
                else {

                    usernameInputBox.setVisibility(View.VISIBLE);
                    confirmUsername.setVisibility(View.VISIBLE);
                }
            }
        });
        */
        confirmName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nameRef.setValue(nameInputBox.getText().toString());
            }
        });

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Intent intent;

            switch (item.getItemId()) {
                case R.id.menu_home:
                    intent = new Intent(EditProfileActivity.this, HomeActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.menu_create_task:
                    intent = new Intent(EditProfileActivity.this, CreateTaskActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.menu_profile:
                    intent = new Intent(EditProfileActivity.this, UserProfileActivity.class);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };

}
