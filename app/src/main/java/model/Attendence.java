package model;

public class Attendence implements Comparable<Attendence> {
    private String date,time,subject,userId,name,date_userId;

    public Attendence() {
    }

    public Attendence(String date, String time, String subject, String userId, String name, String date_userId) {
        this.date = date;
        this.time = time;
        this.subject = subject;
        this.userId = userId;
        this.name = name;
        this.date_userId = date_userId;
    }

    public Attendence(String date, String time, String subject, String userId, String name) {
        this.date = date;
        this.time = time;
        this.subject = subject;
        this.userId = userId;
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate_userId() {
        return date_userId;
    }

    public void setDate_userId(String date_userId) {
        this.date_userId = date_userId;
    }

    @Override
    public String toString() {
        return "Attendence{" +
                "date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", subject='" + subject + '\'' +
                ", userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }


    @Override
    public int compareTo(Attendence attendence) {
        return this.date.compareTo(attendence.getDate());
    }
}
