package matt.housekeep;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

    private class hashPass<T> {
        private String hashedPassword;
        private String salt;

        private hashPass(){

        }

        private hashPass(String hashedPassword, String salt) {
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

        Button privacyButton = findViewById(R.id.privacyPolicyButton);
        final TextView privacyPolicy = findViewById(R.id.privacyPolicy);
        privacyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPrivacyVisible)
                    privacyPolicy.setVisibility(View.VISIBLE);

                else privacyPolicy.setVisibility(View.INVISIBLE);

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

                if (isUsernameValid(username))
                {
                    if (isPasswordValid(password))
                    {
                        Log.d("Password Valid?:", "Yes");
                        if (isPasswordReEnterMatch(password, reenterPassword))
                        {
                            Log.d("Match?: ", "Yes");
                            hashPass hashedPassword = hashPassword(password);
                            DatabaseReference accountCreateDatabase;
                            accountCreateDatabase = FirebaseDatabase.getInstance().getReference();

                            accountCreateDatabase.child("Users").child(username).child("username").setValue(username);
                            accountCreateDatabase.child("Users").child(username).child("name").setValue(name);
                            accountCreateDatabase.child("Users").child(username).child("password").setValue(hashedPassword);

                            Toast.makeText(getApplicationContext(),"Congrats",Toast.LENGTH_SHORT).show(); //TODO passwords don't match

                            //TODO create an account for the user
                        }
                        else Toast.makeText(getApplicationContext(),"Password Doesn't match",Toast.LENGTH_SHORT).show(); //TODO passwords don't match
                    }
                    else Log.d("Password Invalid", "yes");
                     //TODO password is not valid
                }
                 //TODO username is not valid




            }
        });
    }
    private boolean validUsername;
    private boolean usernameCheck;
    private boolean isUsernameValid(String username)
    {
        validUsername = false;
        usernameCheck = false;
        if (username.length() <= 4)
        {
            Toast.makeText(getApplicationContext(),"Username Too Short",Toast.LENGTH_SHORT).show();
            return false;
        }

        if (username.length() > 20) {

            Toast.makeText(getApplicationContext(), "Username Too Long", Toast.LENGTH_SHORT).show();
            return false;
        }

            DatabaseReference userRef = database.getReference();

            userRef.child("Users").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override

        public void onDataChange(DataSnapshot dataSnapshot) {
               if(dataSnapshot.exists())
               {
                   Toast.makeText(getApplicationContext(),"Username Exists",Toast.LENGTH_SHORT).show();
                   EditText usernameText = findViewById(R.id.create_account_username);
                   usernameText.requestFocus();
               }
               else validUsername = true;
               usernameCheck = true;
                Log.d("username Valid 1: ", Boolean.toString(validUsername) );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Log.d("username Valid 2: ", Boolean.toString(validUsername) );
        return validUsername;
    }


    private boolean isPasswordValid(String password)
    {
        Log.d("passsword: ", password);
        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
        return password.matches(pattern);
    }

    private boolean isPasswordReEnterMatch(String password, String reenterPassword)
    {


        return password.equals(reenterPassword);
    }

    private hashPass hashPassword(String password){
        String salt = BCrypt.gensalt();
        String hashedPassword = BCrypt.hashpw(password, salt );
        hashPass hashedPasswordAndSalt = new hashPass(password, salt);
        return hashedPasswordAndSalt;

    }



}
