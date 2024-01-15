package com.example.weebturkishdep;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private Button btnLogin, btnSignup;
    private TextView welcomeMessage;

    // Handler to update UI from the background thread
    private Handler uiHandler = new Handler(msg -> {
        // Handle UI updates here
        if (msg.obj != null) {
            // If the message object is not null, display a Toast with the message
            showToast(msg.obj.toString());
            // Move to the next activity after successful login
            moveToNextActivity();
        } else {
            // If the message object is null, show a Toast indicating incorrect credentials
            showToast("Incorrect username or password");
        }
        return true;
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Reference UI elements
        editTextUsername = findViewById(R.id.NameSpace);
        editTextPassword = findViewById(R.id.PwSpace);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);
        welcomeMessage = findViewById(R.id.WelcomeMessage);

        // Set welcome message
        welcomeMessage.setText("Welcome to WeepDeb");

        Intent userIntent = getIntent();
        if (userIntent != null && userIntent.hasExtra("USERNAME")) {
            String username = userIntent.getStringExtra("USERNAME");
            Log.d("fortytwo", "Username in MainActivity: " + username);
            UserData.getInstance().setUsername(username);
            // Now you have the username, and you can use it as needed in your MainActivity
        } else {
            // Handle the case when the username is not available
            Log.d("fortytwo", "Username not found in intent");
        }


        // Set click listeners for buttons
        btnLogin.setOnClickListener(view -> {
            // Handle login button click
            String username = editTextUsername.getText().toString();
            String password = editTextPassword.getText().toString();

            // Validate input (you can add more validation as needed)
            if (username.isEmpty() || password.isEmpty()) {
                showToast("Username and password are required");
                return;
            }

            // If input is valid, call the login method in a separate thread
            ApiRepository apiRepository = new ApiRepository();
            apiRepository.login(Executors.newCachedThreadPool(), uiHandler, username, password);
        });

        btnSignup.setOnClickListener(view -> {
            // Handle signup button click
            // Start the SignupActivity and clear the back stack
            Intent intent = new Intent(MainActivity.this, SignupActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void moveToNextActivity() {
        // Start the ThreadMenuActivity
        Intent intent = new Intent(MainActivity.this, ThreadMenuActivity.class);
        startActivity(intent);
        // Finish the current activity to prevent going back to the login screen
        finish();
    }
}
