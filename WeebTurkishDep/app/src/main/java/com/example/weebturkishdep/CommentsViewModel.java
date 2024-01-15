package com.example.weebturkishdep;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class CommentsViewModel extends ViewModel {

    MutableLiveData<List<Comments>> commentData = new MutableLiveData<>();
    MutableLiveData<Comments> selectedComment = new MutableLiveData<>();

    public CommentsViewModel() {
        // Initialize your ViewModel, load data, etc. if needed
    }

    public void setSelectedComment(Comments comment) {
        selectedComment.setValue(comment);
    }

    public MutableLiveData<Comments> getSelectedComment() {
        return selectedComment;
    }

    public MutableLiveData<List<Comments>> getCommentData() {
        return commentData;
    }

    public void setCommentData(MutableLiveData<List<Comments>> commentData) {
        this.commentData = commentData;
    }

    // You can add methods here for other operations like adding, deleting comments, etc.
    // Example:

    public void addComment(Comments comment) {
        List<Comments> currentComments = commentData.getValue();
        if (currentComments == null) {
            currentComments = new ArrayList<>();
        }
        currentComments.add(comment);
        commentData.setValue(currentComments);
    }
}
