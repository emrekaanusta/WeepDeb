package com.example.weebturkishdep;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AddThreadActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_thread);

        EditText threadTitle = findViewById(R.id.threadTitle);
        EditText threadContent = findViewById(R.id.threadContent);
        Button submitThread = findViewById(R.id.createThreadButton);

        findViewById(R.id.back_button).setOnClickListener(v -> {
                Intent mainActivityIntent = new Intent(AddThreadActivity.this, ThreadMenuActivity.class);
            mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainActivityIntent);
            finish(); // Finish the current activity to remove it from the back stack
        });

        submitThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the entered title and content
                String title = threadTitle.getText().toString();
                String content = threadContent.getText().toString();

                // Validate input
                if (title.isEmpty() || content.isEmpty()) {
                    // Show an error message or handle the case where the input is not valid
                    return;
                }

                // Assuming you have a method in ApiRepository to add a thread
                ApiRepository apiRepository = new ApiRepository();
                apiRepository.addThread(title, content, result -> {
                    if (result) {
                        // Thread added successfully, navigate back to ThreadMenuActivity
                        Intent intent = new Intent(AddThreadActivity.this, ThreadMenuActivity.class);


                        startActivity(intent);
                        finish();
                    } else {
                        // Handle the case when adding a thread is unsuccessful
                        // You can show a toast or any other error handling mechanism
                        runOnUiThread(() -> {
                            //Toast.makeText(AddThreadActivity.this, "Error adding thread", Toast.LENGTH_SHORT).show();
                        });
                        finish();
                    }
                });
            }
        });
    }

}
