package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.olabode33.displayjokelibrary.DisplayJokeActivity;
import com.udacity.gradle.builditbigger.BuildConfig;
import com.udacity.gradle.builditbigger.EndPointAsyncTask;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.Services.EndPointEventListener;

public class MainActivityFragment extends Fragment {
    private InterstitialAd mInterstitialAd;
    private ProgressBar mLoadingJokeProgressBar;
    private Button mTellJokeButton;

    public MainActivityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main_activity, container, false);

        mLoadingJokeProgressBar = root.findViewById(R.id.pbLoadingJoke);
        mTellJokeButton = root.findViewById(R.id.tellJokeButton);

        mLoadingJokeProgressBar.setVisibility(View.GONE);

        mTellJokeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tellJoke();
            }
        });

        AdView mAdView = (AdView) root.findViewById(R.id.adView);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        if(BuildConfig.IS_PAID) {
            mAdView.setVisibility(View.GONE);
        } else {
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .build();
            mAdView.loadAd(adRequest);

            MobileAds.initialize(getContext(), getString(R.string.banner_ad_unit_id));

            mInterstitialAd = new InterstitialAd(getContext());
            mInterstitialAd.setAdUnitId(getString(R.string.banner_ad_unit_id));
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }

        return root;

    }

    public void tellJoke() {
        if(mInterstitialAd.isLoaded()) {
            mInterstitialAd.setAdListener(new AdListener(){
                @Override
                public void onAdClosed() {
                    displayJoke();
                }
            });
            mInterstitialAd.show();
        } else {
            displayJoke();
        }

    }

    private void displayJoke() {
        EndPointAsyncTask endPointAsyncTask = new EndPointAsyncTask(getContext(), new EndPointEventListener() {
            @Override
            public void onSuccess(String result) {

                Intent intent = new Intent(getContext(), DisplayJokeActivity.class);
                intent.putExtra(DisplayJokeActivity.DISPLAY_JOKES_EXTRA, result);
                getContext().startActivity(intent);
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getContext(), "Error retrieving joke" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void isLoading(Boolean b) {
                if (b) {
                    if(mLoadingJokeProgressBar != null && !mLoadingJokeProgressBar.isShown()) {
                        mLoadingJokeProgressBar.setVisibility(View.VISIBLE);
                    }
                } else {
                    if(mLoadingJokeProgressBar != null && mLoadingJokeProgressBar.isShown()) {
                        mLoadingJokeProgressBar.setVisibility(View.GONE);
                    }
                }
            }
        });
        endPointAsyncTask.execute();
    }
}
