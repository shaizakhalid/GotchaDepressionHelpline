package com.example.gotcha_depressionhelpline;

public class Preferences {
    String username;
    String startTime;
    String sessionDate;
    String endTime;

    public Preferences(String username, String startTime,String endTime)
    {
        this.username = username;
        this.startTime = startTime;
        this.endTime=endTime;
    }
    public Preferences(String username, String startTime,String endTime, String sessionDate)
    {
        this.username = username;
        this.startTime = startTime;
        this.endTime=endTime;
        this.sessionDate=sessionDate;
    }

    public String getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(String sessionDate) {
        this.sessionDate = sessionDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }


}
