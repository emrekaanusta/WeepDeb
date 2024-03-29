package com.example.weepdep.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Document(collection = "comments")
public class Comments {

    @Id
    private String id;
    private String comment;
    boolean  isAnon;

    //@DBRef
    private String user;  // Reference to the user who posted the comment

    @JsonBackReference
    @DBRef
    private Thread thread;  // Reference to the thread to which the comment belongs


    @DBRef
    private Bread bread;

    public Comments(){

    }
    public Comments(String user, String comment, Thread thread,Bread bread, boolean  isAnon){
        this.comment = comment;
        this.user = user;
        this.thread = thread;
        this.isAnon = isAnon;
        this.bread = bread;
    }

    public String getComment () {
        return comment;
    }

    public void setComment (String comment) {
        this.comment = comment;
    }

    public String getUsername () {
        return user;
    }

    public void setUser (String user) {
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {

        this.thread = thread;

    }

    public Bread getBread() {
        return bread;
    }

    public void setBread(Bread bread) {

        this.bread = bread;
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