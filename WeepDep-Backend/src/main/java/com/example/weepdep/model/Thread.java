package com.example.weepdep.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;


@Document(collection = "threads")
public class Thread {
    @Id
    private String id;
    private int customId;
    private String title;
    private String content;
    boolean  isAnon;
    
    //@DBRef
    private String user;  // Reference to the user who created the thread

    @JsonIgnore
    @JsonManagedReference("thread-comments")
    @DBRef(lazy = false)
    private List<Comments> comments = new ArrayList<>();

    
    public Thread() {

    }
    
    public Thread(String id) {
        this.id = id;
    }
    
    // getters and setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCustomId() {
        return customId;
    }

    public void setCustomId(int customId) {
        this.customId = customId;
    }

    public void setUser(String user) {
        this.user = user;
    }
    public String getUser() {
        return user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
     public void addComment(Comments comment) {
        if (comments == null) {
            comments = new ArrayList<>();
        }
        comments.add(comment);
    }

    // Method to get comments of the thread
    public List<Comments> getComments() {
        return comments;
    }
   
    
    public void setAnon(boolean isAnon) {
        this.isAnon = isAnon;
    }
    
    public boolean getAnon() {

         return isAnon;
    }
    
    public String getAnonName () {
        return "Anonymus";
    }

}
