package com.example.poster.calendardemo;

/**
 * Created by POSTER on 05.04.2017.
 */

public class TimeModel {
    private int currentTime;
    private String description;
    private int isNotif;
    private int idModel;


    public TimeModel(int currentTime, String description, int isNotif, int idModel) {
        this.currentTime = currentTime;
        this.description = description;
        this.isNotif = isNotif;
        this.idModel = idModel;
    }

    public TimeModel() {
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIsNotif() {
        return isNotif;
    }

    public void setNotif(int notif) {
        isNotif = notif;
    }

    public int getIdModel() {
        return idModel;
    }

    public void setIdModel(int idModel) {
        this.idModel = idModel;
    }

    @Override
    public String toString() {
        return currentTime + " " + description + " " + isNotif;
    }
}
