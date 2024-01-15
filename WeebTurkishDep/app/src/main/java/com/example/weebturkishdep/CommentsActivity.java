package com.example.weebturkishdep;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CommentsActivity extends AppCompatActivity implements CommentsAdapter.CommentListListener {

    private CommentsAdapter commentsAdapter;
    private CommentsViewModel commentsViewModel;


    private int threadId; // Assuming you will pass the thread ID from the previous activity
    private String threadName;
    private String threadContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        // Assuming you passed the thread ID from the previous activity
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("THREAD_ID")) {
            threadId = intent.getIntExtra("THREAD_ID", -1);
            Toast.makeText(this, "Thread Clicked: " + threadId, Toast.LENGTH_SHORT).show();
            threadName = intent.getStringExtra("THREAD_NAME");
            threadContent = intent.getStringExtra("THREAD_CONTENT");

        }


        TextView threadContentTextView = findViewById(R.id.thread_content);
        threadContentTextView.setText(threadContent);
        // Initialize ViewModel
        commentsViewModel = new ViewModelProvider(this).get(CommentsViewModel.class);

        // Initialize RecyclerView and Adapter
        RecyclerView recyclerView = findViewById(R.id.recyclerView); // Correct the ID to match your layout
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentsAdapter = new CommentsAdapter(this, new ArrayList<>());
        commentsAdapter.setListener(this);
        recyclerView.setAdapter(commentsAdapter);

        // Example: Fetch comments data when the activity is created
        ExecutorService executorService = Executors.newFixedThreadPool(1); // Adjust the pool size accordingly
        fetchData(executorService);

        // Example: Handle the "add comment" button click
        findViewById(R.id.button).setOnClickListener(v -> {
            // Perform the action when the button is clicked
            Intent addCommentIntent = new Intent(CommentsActivity.this, AddCommentActivity.class);
            // Pass any data you need to the next activity
            addCommentIntent.putExtra("THREAD_ID", threadId);
            startActivity(addCommentIntent);
        });
    }

    private void fetchData(ExecutorService executorService) {
        ApiRepository apiRepository = new ApiRepository();

        // Assuming you have a method in ApiRepository to get comments for a specific thread
        apiRepository.getCommentsForThread(executorService, new Handler(msg -> {
            if (msg.obj != null && msg.obj instanceof List<?>) {
                // If the message object is a list, it means we received comments
                List<Comments> comments = (List<Comments>) msg.obj;

                // Update the ViewModel with the received comments
                commentsViewModel.getCommentData().setValue(comments);

                // Update the UI by notifying the adapter with the new data
                commentsAdapter.setComments(comments);
            } else {
                // Handle the case when the data retrieval is unsuccessful
                Toast.makeText(this, "Error fetching comments", Toast.LENGTH_SHORT).show();
            }
            return true;
        }), threadId);
    }

    // CommentListListener interface method
    @Override
    public void onCommentClick(Comments comment) {
        // Handle the click on a comment item if needed
        Toast.makeText(this, "Comment Clicked: " + comment.getComment(), Toast.LENGTH_SHORT).show();
    }



}
