package com.example.weebturkishdep;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ApiRepository {

    private static final String BASE_API_ENDPOINT = "http://10.0.2.2:8080/api/auth/";

    public void signUp(ExecutorService executorService, Handler uiHandler, String username, String password) {
        executeTask(executorService, uiHandler, "signup", username, password);
    }

    public void login(ExecutorService executorService, Handler uiHandler, String username, String password) {
        executeTask(executorService, uiHandler, "login", username, password);
        UserData.getInstance().setUsername(username);
    }

    private void executeTask(ExecutorService executorService, Handler uiHandler, String endpoint, String username, String password) {
        executorService.execute(() -> {
            try {
                URL url = new URL(BASE_API_ENDPOINT + endpoint);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                // Set the request method to POST
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");

                // Construct the JSON payload manually
                String requestBody = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";
                conn.setDoOutput(true);
                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = requestBody.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                // Handle the response
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    StringBuilder buffer = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }
                    Message msg = new Message();
                    msg.obj = buffer.toString();
                    uiHandler.sendMessage(msg);
                }

                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    public void getThreads(ExecutorService executorService, Handler uiHandler) {
        executorService.execute(() -> {
            try {
                URL url = new URL(BASE_API_ENDPOINT + "threads");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                // Set the request method to GET for fetching threads
                conn.setRequestMethod("GET");

                // ... (same code as before)

                // Handle the response
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    StringBuilder buffer = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }

                    // Convert the JSON response to a list of Thread objects
                    List<Thread> threads = parseThreadJson(buffer.toString());

                    // Send the threads to the UI thread using the Handler
                    Message msg = new Message();
                    msg.obj = threads;
                    uiHandler.sendMessage(msg);
                }

                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    private List<Thread> parseThreadJson(String jsonString) {
        // Implement the logic to parse JSON into a list of Thread objects
        // You can use a JSON parsing library like Gson for this task
        // For simplicity, let's assume a basic implementation here

        List<Thread> threads = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonThread = jsonArray.getJSONObject(i);

                // Extract thread information from JSON and create a Thread object
                Thread thread = new Thread();
                thread.setId(jsonThread.getString("customId"));
                thread.setCustomId(jsonThread.getInt("customId"));
                thread.setTitle(jsonThread.getString("title"));
                thread.setContent(jsonThread.getString("content"));

                // Add the thread to the list
                threads.add(thread);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return threads;
    }

    public void getCommentsForThread(ExecutorService executorService, Handler uiHandler, int threadId) {
        executorService.execute(() -> {
            try {
                URL url = new URL(BASE_API_ENDPOINT + "threads/" + threadId + "/comments");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                // Set the request method to GET for fetching comments
                conn.setRequestMethod("GET");

                // ... (same code as before)

                // Handle the response
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    StringBuilder buffer = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }

                    // Convert the JSON response to a list of Comments objects
                    List<Comments> comments = parseCommentJson(buffer.toString());

                    // Send the comments to the UI thread using the Handler
                    Message msg = new Message();
                    msg.obj = comments;
                    uiHandler.sendMessage(msg);
                }

                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private List<Comments> parseCommentJson(String jsonString) {
        // Implement the logic to parse JSON into a list of Comments objects
        // You can use a JSON parsing library like Gson for this task
        // For simplicity, let's assume a basic implementation here

        List<Comments> comments = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonComment = jsonArray.getJSONObject(i);

                // Extract comment information from JSON and create a Comments object
                Comments comment = new Comments();


                comment.setComment(jsonComment.getString("comment"));
                comment.setAnon(jsonComment.getBoolean("anon"));
                if (comment.getAnon()==true){
                    comment.setUser("Anonymus");
                }
                else{
                    comment.setUser(jsonComment.getString("username"));
                }

                // Add the comment to the list
                comments.add(comment);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return comments;
    }


    public void addCommentToThread(ExecutorService executorService , int threadId, String commentContent, boolean isAnonymous, String username, ResultCallback<Boolean> callback) {
        executorService.execute(() -> {
            try {
                URL url = new URL(BASE_API_ENDPOINT + "threads/" + threadId + "/comments");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                // Set the request method to POST
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");

                // Construct the JSON payload manually
                JSONObject jsonComment = new JSONObject();

                String usernameFetched = UserData.getInstance().getUsername();

                //Log.d("ADD COMMENT", usernameFetched);
                Log.d("ADD COMMENT", commentContent);

                jsonComment.put("comment", commentContent);
                jsonComment.put("isAnon", isAnonymous);
                jsonComment.put("user", usernameFetched);


                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonComment.toString().getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                Log.d("Here inside try",   "myuy messahe");

                // Handle the response
                boolean success = conn.getResponseCode() == HttpURLConnection.HTTP_CREATED;

                if (!success) {
                    // Print or log the response body for debugging purposes
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                        StringBuilder buffer = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            buffer.append(line);
                        }
                        System.out.println("Response Body: " + buffer.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                // Notify the result to the callback
                callback.onResult(success);

                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();

                Log.e("AddCommentActivity", "Error adding comment: " + e.getMessage());

                // Notify the result to the callback in case of an error
                callback.onResult(false);
            }
        });
    }

    private ExecutorService executorService = Executors.newFixedThreadPool(1);
    public void addThread(String title, String content, ResultCallback<Boolean> callback) {
        executorService.execute(() -> {
            try {

                URL url = new URL(BASE_API_ENDPOINT + "create-thread");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                // Set the request method to POST
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");


                // Construct the JSON payload manually
                String username = UserData.getInstance().getUsername();
                JSONObject jsonThread = new JSONObject();
                jsonThread.put("username", UserData.getInstance().getUsername());
                jsonThread.put("title", title);
                jsonThread.put("content", content);

                Log.d("Here we are", content);
                Log.d("Here we are again", title);
                Log.d("For usernmae", username);


                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonThread.toString().getBytes("utf-8");
                    os.write(input, 0, input.length);
                    os.flush(); // Ensure that all data is sent before moving on
                }

                // Handle the response
                boolean success = conn.getResponseCode() == HttpURLConnection.HTTP_CREATED;

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    StringBuilder buffer = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }

                    // Log the response body for debugging purposes
                    Log.d("ApiRepository", "Response Body: " + buffer.toString());
                }


                // Notify the result to the callback
                callback.onResult(success);


                conn.disconnect();


            } catch (Exception e) {
                e.printStackTrace();

                Log.e("ApiRepository", "Error adding thread: " + e.getMessage());

                // Notify the result to the callback in case of an error
                callback.onResult(false);
            }
        });
    }

    // Define a callback interface for handling asynchronous results
    public interface ResultCallback<T> {
        void onResult(T result);
    }

}
