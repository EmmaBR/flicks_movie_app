package me.emmabr.flicks;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MovieListActivity extends AppCompatActivity {

    // constants
    // base URL for API
    public final static String API_BASE_URL = "https://api.themoviedb.org/3";
    // parameter name for the API key
    public final static String API_KEY_PARAM = "api_key";
    //tag for all logging calls
    public final static String TAG = "MovieListActivity";

    // instance fields
    AsyncHttpClient client;
    //base url for loading images
    String imageBaseUrl;
    // poster size when fetching images
    String posterSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        //initialize the client
        client = new AsyncHttpClient();
        //get the configuration on app creation
        getConfiguration();
    }

    private void getConfiguration() {
        //create the URL
        String url = API_BASE_URL + "/configuration";
        //set the request parameters
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key)); // API key always required
        //execute a get request expecting JSON
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject images = response.getJSONObject("images");
                    // get the image base url
                    imageBaseUrl = images.getString("secure_base_url");
                    //get the poster size
                    JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
                    //use the option at index 3 or w342 as a fallback
                    posterSize = posterSizeOptions.optString(3, "w342");
                } catch (JSONException e) {
                    logError("Failed parsing configuration", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed gettig configuration", throwable, true);
            }
        });
    }

    //error logging helper, log and alert user
    private void logError(String message, Throwable error, boolean alertUser) {
        // always log error
        Log.e(TAG, message, error);
        // alert user to avoid silent errors
        if (alertUser) {
            // show toast with error message
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }

}
