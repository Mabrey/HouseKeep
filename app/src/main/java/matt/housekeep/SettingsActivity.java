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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingsActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Switch notificationSwitch;
    private EditText currentPassword;
    private EditText newPassword;
    private EditText confirmPassword;
    private Button confirmButton;
    private Button changePassLabel;
    private Button privacyPolicy;
    private TextView privacyPolicyView;
    private boolean success;
    private String username;
    private SharedPreferences prefs;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //initialize
        success = false;

        prefs = this.getSharedPreferences(
                getString(R.string.shared_prefs_key), Context.MODE_PRIVATE);

        username = prefs.getString(getString(R.string.saved_username_key), "");

        //nav bar
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //notification switch
        notificationSwitch = (Switch)findViewById(R.id.notificationsSwitch);
        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //allow notifications somehow
                }
                else {
                    //deny notification permissions somehow
                }
            }
        });

        //current password input field
        currentPassword = (EditText)findViewById(R.id.currentPassword);

        //new password input field
        newPassword = (EditText)findViewById(R.id.newPassword);

        //confirm password input field
        confirmPassword = (EditText)findViewById(R.id.confirm_password);
        Log.d("variable_confirm",confirmPassword.getText().toString());
        if(confirmPassword == null)
            Log.d("confirm_pass_null", "confirm password is null");

        //confirm button
        confirmButton = (Button)findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                checkPassword(currentPassword.getText().toString());

            }
        });

        Button editProfile = (Button)findViewById(R.id.editProfileButton);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Bundle b = new Bundle();
                b.putString("GroupName", "");
                b.putString("UserName", username);
                b.putBoolean("inGroup", false);
                Intent intent = new Intent(SettingsActivity.this, EditProfileActivity.class);
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        privacyPolicyView = (TextView)findViewById(R.id.privacy_policy_information);

        currentPassword.setVisibility(View.GONE);
        newPassword.setVisibility(View.GONE);
        confirmPassword.setVisibility(View.GONE);
        confirmButton.setVisibility(View.GONE);
        privacyPolicyView.setVisibility(View.GONE);

        changePassLabel = (Button)findViewById(R.id.change_pass_label_button);
        changePassLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(confirmButton.getVisibility() == View.GONE){

                    currentPassword.setVisibility(View.VISIBLE);
                    newPassword.setVisibility(View.VISIBLE);
                    confirmPassword.setVisibility(View.VISIBLE);
                    confirmButton.setVisibility(View.VISIBLE);
                }
                else {

                    currentPassword.setVisibility(View.GONE);
                    newPassword.setVisibility(View.GONE);
                    confirmPassword.setVisibility(View.GONE);
                    confirmButton.setVisibility(View.GONE);
                }

            }
        });

        privacyPolicy = (Button)findViewById(R.id.privacyPolicyButton);
        privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(privacyPolicyView.getVisibility() == View.GONE){

                    privacyPolicyView.setVisibility(View.VISIBLE);
                }
                else {

                    privacyPolicyView.setVisibility(View.GONE);
                }

            }
        });
    }

    private void checkPassword(final String currentPassword){
        DatabaseReference userRef = database.getReference("Users");
        Log.d("SETTINGS_NAME", username);
        userRef.child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Log.d("SNAPSHOT", "Exists");
                    final String hashedPassword = (String) dataSnapshot.child
                            ("Password Info").child("hashedPassword").getValue();

                    final String salt = (String) dataSnapshot.child
                            ("Password Info").child("salt").getValue();

                    if (hashedPassword.equals(org.mindrot.jbcrypt.BCrypt.hashpw(currentPassword, salt))){

                        confirmPassword(newPassword.getText().toString(),
                                confirmPassword.getText().toString());
                        Log.d("correct_passwords", "passwords match");

                    }
                    else {

                        Toast.makeText(SettingsActivity.this, "Incorrect Password", Toast.LENGTH_LONG).show();
                        Log.d("wrong_passwords", hashedPassword + ", " +
                                org.mindrot.jbcrypt.BCrypt.hashpw(currentPassword, salt));
                    }


                }
                else {
                    Log.d("SNAPSHOT", "Doesnt Exist");
                    success = false;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void confirmPassword(String pass1, String pass2){

        boolean isValid = AccountCreateActivity.isPasswordValid(pass1);
        if(pass1.equals(pass2) && isValid){
            changePassword(confirmPassword.getText().toString());
            Toast.makeText(SettingsActivity.this, "Changed Password", Toast.LENGTH_LONG).show();
        }
        else {

            Toast.makeText(SettingsActivity.this, "Passwords Do Not Match or Are Invalid",
                    Toast.LENGTH_LONG).show();
        }


    }

    private void changePassword(String newPassword){

        AccountCreateActivity.hashPass hashedPassword = hashPassword(newPassword);
        DatabaseReference myRef;
        myRef = FirebaseDatabase.getInstance().getReference();

        myRef.child("Users").child(username).child("Password Info").setValue(hashedPassword);

    }



    private AccountCreateActivity.hashPass hashPassword(String password) {
        String salt = org.mindrot.jbcrypt.BCrypt.gensalt();
        String hashedPassword = org.mindrot.jbcrypt.BCrypt.hashpw(password, salt);
        AccountCreateActivity.hashPass hashedPasswordAndSalt = new AccountCreateActivity.hashPass(hashedPassword, salt);
        return hashedPasswordAndSalt;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Intent intent;
            Bundle b = new Bundle();
            b.putString("GroupName", "");
            b.putString("UserName", username);
            b.putBoolean("inGroup", false);

            switch (item.getItemId()) {
                case R.id.menu_home:
                    intent = new Intent(SettingsActivity.this, HomeActivity.class);
                    intent.putExtras(b);
                    startActivity(intent);
                    return true;
                case R.id.menu_create_task:
                    intent = new Intent(SettingsActivity.this, CreateTaskActivity.class);
                    intent.putExtras(b);
                    startActivity(intent);
                    return true;
                case R.id.menu_profile:
                    intent = new Intent(SettingsActivity.this, UserProfileActivity.class);
                    intent.putExtras(b);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };

}
