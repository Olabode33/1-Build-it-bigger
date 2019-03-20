package com.udacity.gradle.builditbigger.Services;

public interface EndPointEventListener<T> {
    public void onSuccess(String s);
    public void onFailure(Exception e);
    public void isLoading(Boolean b);
}
