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

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.android.quakereport.model.EarthQuake;
import com.example.android.quakereport.model.EarthQuakeAdapter;
import com.example.android.quakereport.model.QueryUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class EarthquakeActivity extends AppCompatActivity {

    private static final String USGS =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&limit=1000";
    private EarthQuakeAdapter earthQuakeAdapter;
    EarthQuakeAsync ea = new EarthQuakeAsync();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ArrayList<EarthQuake> earthQuakes = new ArrayList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);


        ea.execute(USGS);

        try {
           earthQuakes = (ArrayList<EarthQuake>) ea.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        ListView earthQuakeListView = (ListView) findViewById(R.id.list);
        EarthQuakeAdapter earthQuakeAdapter = new EarthQuakeAdapter(this, earthQuakes);

        earthQuakeListView.setAdapter(earthQuakeAdapter);


    }

    private class EarthQuakeAsync extends AsyncTask<String, Void, List<EarthQuake>> {

        private ProgressDialog progressDialog;

        @Override
        protected List<EarthQuake> doInBackground(String... urls) {
            List<EarthQuake> result = QueryUtils.fetchEarthquakeData(USGS);
            return result;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(EarthquakeActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<EarthQuake> result) {

            try {
                progressDialog.dismiss();
                    earthQuakeAdapter.addAll(result);
                    ea.cancel(true);

            } catch (NullPointerException e) {
            }
        }
    }
}
