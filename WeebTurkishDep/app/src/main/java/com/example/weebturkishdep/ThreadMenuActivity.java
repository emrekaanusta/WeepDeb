package com.example.weebturkishdep;

// ThreadMenuActivity.java

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadMenuActivity extends AppCompatActivity implements ThreadAdapter.ThreadListListener {

    private ThreadAdapter threadAdapter;
    private ThreadViewModel threadViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_threadmenu);


        // Initialize ViewModel
        threadViewModel = new ViewModelProvider(this).get(ThreadViewModel.class);

        // Initialize RecyclerView and Adapter
        RecyclerView recyclerView = findViewById(R.id.newRecyler); // Correct the ID to match your layout
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        threadAdapter = new ThreadAdapter(this, new ArrayList<>());
        threadAdapter.setListener(this);
        recyclerView.setAdapter(threadAdapter);

        // Example: Fetch thread data when the activity is created
        ExecutorService executorService = Executors.newFixedThreadPool(1); // Adjust the pool size accordingly
        fetchData(executorService);

        Button btnCreateThread = findViewById(R.id.createThread);
        btnCreateThread.setOnClickListener(view -> {
            // Navigate to CreateThreadActivity when the button is clicked
            Intent intent = new Intent(ThreadMenuActivity.this, AddThreadActivity.class);
            startActivity(intent);

        });

        findViewById(R.id.back_button_thread).setOnClickListener(v -> {
            Intent mainActivityIntent = new Intent(ThreadMenuActivity.this, MainActivity.class);
            mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainActivityIntent);
            finish(); // Finish the current activity to remove it from the back stack
        });

    }

    private void fetchData(ExecutorService executorService) {
        ApiRepository apiRepository = new ApiRepository();

        apiRepository.getThreads(executorService, new Handler(msg -> {
            if (msg.obj != null && msg.obj instanceof List<?>) {
                // If the message object is a list, it means we received threads
                List<Thread> threads = (List<Thread>) msg.obj;

                // Update the ViewModel with the received threads
                threadViewModel.getThreadData().setValue(threads);

                // Update the UI by notifying the adapter with the new data
                threadAdapter.setThreads(threads);
            } else {
                // Handle the case when the data retrieval is unsuccessful
                Toast.makeText(this, "Error fetching threads", Toast.LENGTH_SHORT).show();
            }
            return true;
        }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Fetch thread data when the activity is resumed
        ExecutorService executorService = Executors.newFixedThreadPool(1); // Adjust the pool size accordingly
        fetchData(executorService);
    }



    // ThreadListListener interface method
    @Override
    public void personClicked(Thread thread) {
        // Handle the click on a thread item
        // Example: Move to a detailed view or perform some action
        Toast.makeText(this, "Thread Clicked: " + thread.getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRowClick(Thread thread) {
        // Handle row click - navigate to the next activity or perform other actions
        Intent intent = new Intent(this, CommentsActivity.class);
        // Pass any data you need to the next activity
        intent.putExtra("THREAD_ID", thread.getCustomId());
        intent.putExtra("THREAD_NAME", thread.getTitle());
        intent.putExtra("THREAD_CONTENT", thread.getContent());
        //Toast.makeText(this, "Thread Clicked: " + thread.getCustomId(), Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

}