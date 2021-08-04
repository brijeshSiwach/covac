package com.example.covac;

public class ScreenItem {

    String title, description;
    int screenimg;

    public ScreenItem(String title, String description, int screenimg) {
        this.title = title;
        this.description = description;
        this.screenimg = screenimg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getScreenimg() {
        return screenimg;
    }

    public void setScreenimg(int screenimg) {
        this.screenimg = screenimg;
    }
}
