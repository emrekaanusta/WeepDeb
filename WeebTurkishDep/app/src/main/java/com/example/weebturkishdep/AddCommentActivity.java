package com.example.weebturkishdep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import org.w3c.dom.Comment;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddCommentActivity extends AppCompatActivity {

    private EditText editTextComment;
    private Switch anonymousSwitch;

    private int threadId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);

        if (getIntent() != null && getIntent().hasExtra("THREAD_ID")) {
            threadId = getIntent().getIntExtra("THREAD_ID", -1);
        }

        editTextComment = findViewById(R.id.commentInput);
        anonymousSwitch = findViewById(R.id.switch1);

        Button addCommentButton = findViewById(R.id.button2);
        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("AddCommentActivity", "Button Clicked!");
                String username = getIntent().getStringExtra("USERNAME"); // Set a default username or fetch from user authentication
                String commentText = editTextComment.getText().toString();
                boolean isAnonymous = anonymousSwitch.isChecked();


                // Create a Comment object
                Comments comment = new Comments(username, commentText, isAnonymous);


                addComment(threadId, commentText, isAnonymous, username);
                printCommentDetails(comment);
            }
        });
    }

    private void addComment(int threadId, String commentContent, boolean isAnonymous, String username) {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        ApiRepository apiRepository = new ApiRepository();


        // Assuming you have a method in ApiRepository to add a comment to a thread
        apiRepository.addCommentToThread(executorService, threadId, commentContent, isAnonymous, username, result -> {
            if (result) {
                // Comment added successfully, you may want to finish the activity or update UI accordingly
                Intent intent = new Intent(AddCommentActivity.this, CommentsActivity.class);
                intent.putExtra("THREAD_ID", threadId);


                String usernameFetched = UserData.getInstance().getUsername();


                intent.putExtra("USERNAME", usernameFetched);
                intent.putExtra("COMMENT_CONTENT", commentContent);

                Log.d("Comment checker", commentContent);

                Log.d("Username checker", usernameFetched);


                startActivity(intent);
                Log.d("AddCommentActivity", "Started CommentsActivity");
                finish();
                Log.d("AddCommentActivity", "Finished AddCommentActivity");
            } else {
                Log.d("AddCommentActivity","Hreeeeeee");
                //Toast.makeText(this, "Could not add a comment!!!", Toast.LENGTH_SHORT).show();
                // Handle the case when adding a comment is unsuccessful
                // You can show a toast or any other error handling mechanism
            }
        });
    }

    private void printCommentDetails(Comments comment) {
        // Display comment details (for testing)
        String anonymityStatus = comment.getAnon() ? "Anonymous" : "Not Anonymous";
        String details = "Username: " + comment.getUsername() + "\nComment: " + comment.getComment() +
                "\nAnonymity: " + anonymityStatus;

    }


}
