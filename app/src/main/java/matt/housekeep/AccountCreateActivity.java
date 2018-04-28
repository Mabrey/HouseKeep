package matt.housekeep;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.SecureRandom;
import java.util.Random;

import org.mindrot.jbcrypt.BCrypt;


public class AccountCreateActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static final Random RANDOM = new SecureRandom();
    boolean validUsername = false;

    public static class hashPass<T> {
        private String hashedPassword;
        private String salt;

        private hashPass() {

        }

        protected hashPass(String hashedPassword, String salt) {
            this.hashedPassword = hashedPassword;
            this.salt = salt;
        }

        public String getHashedPassword() {
            return hashedPassword;
        }

        public void setHashedPassword(String hashedPassword) {
            this.hashedPassword = hashedPassword;
        }

        public String getSalt() {
            return salt;
        }

        public void setSalt(String salt) {
            this.salt = salt;
        }
    }


    boolean isPrivacyVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_create);

        Button privacyButton = findViewById(R.id.editProfileButton);
        final TextView privacyPolicy = findViewById(R.id.privacyPolicy);
        privacyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPrivacyVisible)
                    privacyPolicy.setVisibility(View.VISIBLE);

                else privacyPolicy.setVisibility(View.GONE);

                isPrivacyVisible = !isPrivacyVisible;
            }
        });


        Button SignUpButton = (Button) findViewById(R.id.create_account_sign_up);
        SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText nameText = findViewById(R.id.create_account_name);
                String name = nameText.getText().toString();
                EditText usernameText = findViewById(R.id.create_account_username);
                String username = usernameText.getText().toString();
                EditText passwordText = findViewById(R.id.create_account_password);
                String password = passwordText.getText().toString();
                EditText passwordReenterText = findViewById(R.id.create_account_reenter_password);
                String reenterPassword = passwordReenterText.getText().toString();

                checkCredentials(name, username, password, reenterPassword);
            }
        });
    }

    private void checkCredentials(final String name, final String username, final String password, final String reenterPassword) {

        DatabaseReference userRef = database.getReference();
        userRef.child("Users").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {
                //check if username exists.
                if (dataSnapshot.exists()) {
                    Toast.makeText(getApplicationContext(), "Username Exists", Toast.LENGTH_SHORT).show();
                    EditText usernameText = findViewById(R.id.create_account_username);
                    usernameText.requestFocus();
                } else { //username doesn't exist
                    if (isUsernameValid(username)) {
                        if (isPasswordValid(password)) {
                            if (isPasswordReEnterMatch(password, reenterPassword)) {
                                createAccount(name, username, password);
                                startActivity(new Intent(AccountCreateActivity.this, LoginActivity.class));
                            }
                            else Toast.makeText(getApplicationContext(), "Passwords Don't Match", Toast.LENGTH_SHORT).show();
                            EditText passwordReenterInput = findViewById(R.id.create_account_password);
                            passwordReenterInput.requestFocus();
                        }
                        else Toast.makeText(getApplicationContext(), "Invalid Password", Toast.LENGTH_SHORT).show();
                        EditText passwordInput = findViewById(R.id.create_account_password);
                        passwordInput.requestFocus();
                    }
                    else Toast.makeText(getApplicationContext(), "Username Invalid", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void createAccount(String name, String username, String password) {
        Log.d("Match?: ", "Yes");
        hashPass hashedPassword = hashPassword(password);
        DatabaseReference accountCreateDatabase;
        accountCreateDatabase = FirebaseDatabase.getInstance().getReference();

        accountCreateDatabase.child("Users").child(username).child("Username").setValue(username);
        accountCreateDatabase.child("Users").child(username).child("Name").setValue(name);
        accountCreateDatabase.child("Users").child(username).child("Password Info").setValue(hashedPassword);

        Toast.makeText(getApplicationContext(), "Account Created", Toast.LENGTH_SHORT).show();
    }

    protected boolean isUsernameValid(String username) {

        String pattern = "(?=\\S+$).{8,20}";
        return username.matches(pattern);
    }


    public static boolean isPasswordValid(String password) {
        Log.d("passsword: ", password);
        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{8,}";
        return password.matches(pattern);
    }

    private boolean isPasswordReEnterMatch(String password, String reenterPassword) {
        return password.equals(reenterPassword);
    }

    private hashPass hashPassword(String password) {
        String salt = BCrypt.gensalt();
        String hashedPassword = BCrypt.hashpw(password, salt);
        hashPass hashedPasswordAndSalt = new hashPass(hashedPassword, salt);
        return hashedPasswordAndSalt;
    }


}
