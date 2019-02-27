package com.olabode33.displayjokelibrary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayJokeActivity extends AppCompatActivity {
    public static final String DISPLAY_JOKES_EXTRA = "com.olabode33.displayjokelibrary.JokeExtra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_joke);

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(DISPLAY_JOKES_EXTRA)) {
            String joke = intent.getStringExtra(DISPLAY_JOKES_EXTRA);
            TextView displayJokeTextView = findViewById(R.id.tv_displayJoke);
            displayJokeTextView.setText(joke);
        }
    }
}
