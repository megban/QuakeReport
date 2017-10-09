/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private static final String USGS_REQUEST = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=4&limit=10";
    EarthquakeAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);



        new EarthQuakeAsyncTask().execute();
        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        adapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());
        earthquakeListView.setAdapter(adapter);


        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Earthquake currentEarthquake = adapter.getItem(position);
                Uri webpage = Uri.parse(currentEarthquake.getWebpage());
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }

            }

        });

        // Create a new {@link ArrayAdapter} of earthquakes

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface



    }

    private class EarthQuakeAsyncTask extends AsyncTask<String, Void, List<Earthquake>> {
        protected List<Earthquake> doInBackground(String... urls) {
            URL url = createUrl(USGS_REQUEST);
            String JSON_RESPONSE = null;
            try {
                JSON_RESPONSE = makeHttpRequest(url);
            }
            catch(IOException e)
            {
                Log.e(LOG_TAG, "Error with getting JSONResponse", e);
                return null;
            }
            ArrayList<Earthquake> earthquakes = QueryUtils.extractEarthquakes(JSON_RESPONSE);
            return earthquakes;
        }


        @Override
        protected void onPostExecute(List<Earthquake> earthquakes) {
            adapter.clear();
            adapter.addAll(earthquakes);

        }


    }

    /**

      public class EarthquakeActivity extends AppCompatActivity
    implements LoaderCallbacks<List<Earthquake>> {

    ...

     @Override
     public Loader<List<Earthquake>> onCreateLoader(int i, Bundle bundle) {
     // TODO: Create a new loader for the given URL
     }

     @Override
     public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {
     // TODO: Update the UI with the result
     earthquakeListView.setAdapter(adapter);
     }

     @Override
     public void onLoaderReset(Loader<List<Earthquake>> loader) {
     // TODO: Loader reset, so we can clear out our existing data.
     adapter.clear();
     adapter.addAll(earthquakes);

     }
     }
     */

    private URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    private String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        if(url == null)
        {
            return jsonResponse;
        }
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            //urlConnection.setReadTimeout(10000 /* milliseconds */);
            //urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();

            if(urlConnection.getResponseCode() == 200)
            {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
            else
            {
                Log.e(LOG_TAG, "Connection is not equal to 200. Error code :" + urlConnection.getResponseCode());
            }

        } catch (IOException e) {

            Log.e(LOG_TAG, "Connection not being made",e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }



}
