package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditbigger.Services.EndPointEventListener;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;

import java.io.IOException;

public class EndPointAsyncTask extends AsyncTask<Context, Void, String> {
    private static MyApi myApiService = null;
    private Context mContext;
    private EndPointEventListener<String> mCallback;
    public Exception mException;

    public EndPointAsyncTask(Context context, EndPointEventListener callback) {
        mContext = context;
        mCallback = callback;
    }


    @Override
    protected String doInBackground(Context... context) {
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
        Log.d(EndPointAsyncTask.class.getName().toString(), context.toString());
        //mContext = mC;

        try {
            return myApiService.tellJoke().execute().getData();
        } catch (IOException e) {
            mException = e;
            return e.getMessage();
        }
    }

    @Override
    protected void onPreExecute() {
        mCallback.isLoading(true);
    }

    @Override
    protected void onPostExecute(String s) {

        Log.d("EndPointAsync:", s);
        if(mCallback != null) {
            mCallback.isLoading(false);
            Log.d("EndPointAsync:", s);
            mCallback.onSuccess(s);
        } else {
            mCallback.onFailure(mException);
        }
    }
}
