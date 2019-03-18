package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.javajokeslib.JokerLib;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.olabode33.displayjokelibrary.DisplayJokeActivity;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    private InterstitialAd mInterstitialAd;
    private static ProgressBar mLoadingJokeProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadingJokeProgressBar = findViewById(R.id.pbLoadingJoke);
        mLoadingJokeProgressBar.setVisibility(View.GONE);

        if(!BuildConfig.IS_PAID) {
            MobileAds.initialize(this, "ca-app-pub-3940256099942544/1033173712");

            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void tellJoke(View view) {

        JokerLib joker = new JokerLib();
        Toast.makeText(this, joker.TellAJoke(), Toast.LENGTH_SHORT).show();
        if((BuildConfig.IS_PAID)) {
            new EndPointAsyncTask().execute(new Pair<Context, String>(this, "James Jamieson"));
        } else {
            if(mInterstitialAd.isLoaded()) {
                mInterstitialAd.setAdListener(new AdListener(){
                    @Override
                    public void onAdClosed() {
                        new EndPointAsyncTask().execute(new Pair<Context, String>(getApplicationContext(), "James Jamieson"));
                    }
                });
                mInterstitialAd.show();
            }
        }


//        Intent intent = new Intent(this, DisplayJokeActivity.class);
//        intent.putExtra(DisplayJokeActivity.DISPLAY_JOKES_EXTRA, joker.TellAJoke());
//        startActivity(intent);
    }

    static class EndPointAsyncTask extends AsyncTask<Pair<Context, String>, Void, String> {
        private static MyApi myApiService = null;
        private Context mContext;


        @Override
        protected String doInBackground(Pair<Context, String>... pairs) {
            if(myApiService == null) {
                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        .setRootUrl("http://10.0.2.2:8080/_ah/api")
                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(AbstractGoogleClientRequest<?> request) throws IOException {
                                request.setDisableGZipContent(true);
                            }
                        });

                myApiService = builder.build();
            }

            mContext = pairs[0].first;
            String joke = pairs[0].second;

            try {
                return myApiService.tellJoke().execute().getData();
            } catch (IOException e) {
                return e.getMessage();
            }
        }

        @Override
        protected void onPreExecute() {
            if(mLoadingJokeProgressBar != null && !mLoadingJokeProgressBar.isShown()) {
                mLoadingJokeProgressBar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if(mLoadingJokeProgressBar != null && mLoadingJokeProgressBar.isShown()) {
                mLoadingJokeProgressBar.setVisibility(View.GONE);
            }
            Intent intent = new Intent(mContext, DisplayJokeActivity.class);
            intent.putExtra(DisplayJokeActivity.DISPLAY_JOKES_EXTRA, s);
            mContext.startActivity(intent);
        }
    }


}
