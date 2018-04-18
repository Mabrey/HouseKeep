package matt.housekeep;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.security.SecureRandom;
import java.util.Random;
import org.mindrot.jbcrypt.BCrypt;


public class AccountCreateActivity extends AppCompatActivity {

    private static final Random RANDOM = new SecureRandom();

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


        Button SignInButton = (Button) findViewById(R.id.create_account_sign_up);
        SignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = findViewById(R.id.create_account_name).toString();
                String username = findViewById(R.id.create_account_username).toString();
                String password = findViewById(R.id.create_account_password).toString();
                String reenterPassword = findViewById(R.id.create_account_reenter_password).toString();

                if (isUsernameValid(username))
                {

                }
                else //TODO username is not valid

                if (isPasswordValid(password))
                {

                }
                else //TODO password is not valid

                if (isPasswordReEnterMatch(password, reenterPassword))
                {

                }
                else //TODO passwords don't match
            }
        });
    }

    private boolean isUsernameValid(String username)
    {
        return username.length() > 4;//TODO check if username is okay
    }

    private boolean isPasswordValid(String password)
    {
        return password.length() > 4;//TODO check if password is okay
    }

    private boolean isPasswordReEnterMatch(String password, String reenterPassword)
    {
        return password.equals(reenterPassword);//TODO check if password matches
    }

    private String hashPass(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt());

    }



}
