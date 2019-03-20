package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.util.Pair;
import android.test.ActivityInstrumentationTestCase2;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.udacity.gradle.builditbigger.Services.EndPointEventListener;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RunWith(AndroidJUnit4.class)
public class TellJokeAndroidTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mMainActivity;


    public TellJokeAndroidTest() {
        super(MainActivity.class);
    }

    @Before
    public void setup() throws Exception {
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mMainActivity = getActivity();
    }

    @Test
    public void testTellJokeTest() throws Throwable {

        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                EndPointAsyncTask task = new EndPointAsyncTask(mMainActivity, new EndPointEventListener() {
                    @Override
                    public void onSuccess(String s) {

                    }

                    @Override
                    public void onFailure(Exception e) {

                    }

                    @Override
                    public void isLoading(Boolean b) {

                    }
                });

                task.execute();
                String s = "";
                try {
                    s = task.get();

                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                Log.d("TestJOke", s);
                assertThat(s, notNullValue());
                assertEquals(s, "This joke is really funny and you are laughing hard.");
            }
        });
    }
}
