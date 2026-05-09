package model;

public class Job {
    private int id;
    private String title;

    public Job(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId()       { return id; }
    public String getTitle() { return title; }
}