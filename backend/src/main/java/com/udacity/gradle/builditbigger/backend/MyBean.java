package com.udacity.gradle.builditbigger.backend;

import com.example.javajokeslib.JokerLib;

/** The object model for the data we are sending through endpoints */
public class MyBean {

    private String myData;

    public String getData() {
        return myData;
    }

    public void setData(String data) {
        myData = data;
    }

    public void tellJoke() {
        JokerLib joker = new JokerLib();
        myData = joker.TellAJoke();
    }
}