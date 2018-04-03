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


public class AccountCreateActivity extends AppCompatActivity {

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

    }

}
