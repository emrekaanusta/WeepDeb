package com.example.weebturkishdep;


public class Comments {

    private String comment;
    boolean  isAnon;

    //@DBRef
    private String user;  // Reference to the user who posted the comment


    public Comments(){

    }
    public Comments(String user, String comment, boolean  isAnon){
        this.comment = comment;
        this.user = user;
        this.isAnon = isAnon;
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
        this.user = "@" + user;
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