package com.example.weebturkishdep;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class ThreadViewModel extends ViewModel {

    MutableLiveData<List<Thread>> threadData = new MutableLiveData<>();

    MutableLiveData<Thread> selectedThread = new MutableLiveData<>();


    public ThreadViewModel(){

    }


    public void setSelectedThread(Thread thread){
        selectedThread.setValue(thread);
    }

    public MutableLiveData<Thread> getSelectedThread() {
        return selectedThread;
    }

    public MutableLiveData<List<Thread>> getThreadData() {
        return threadData;
    }


    public void setThreadData(MutableLiveData<List<Thread>> threadData) {
        this.threadData = threadData;
    }


}
