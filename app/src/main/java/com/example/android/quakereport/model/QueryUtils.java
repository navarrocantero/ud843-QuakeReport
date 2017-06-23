package com.example.android.quakereport.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    public static List<EarthQuake> fetchEarthquakeData(String requestUrl) {
        // Primero se crea un objeto URL
        URL url = createUrl(requestUrl);

        // Realiza una peticion HTTP y recibe un JSON
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {

            Log.e(LOG_TAG, "Error in making http request", e);
        }

        // Extrae los campos que necesita y crea una lista
        List<EarthQuake> earthquake = extractEarthquakes(jsonResponse);

        return earthquake;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem with the JSON results", e);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException, JSONException {
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

    /**
     * Return a list of {@link EarthQuake} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<EarthQuake> extractEarthquakes(String url) {
        ArrayList<EarthQuake> earthquakes = new ArrayList<>();
        if (url.isEmpty()) {
            return null;
        }

        try {
            JSONObject jsonObject = null;
            jsonObject = new JSONObject(url);
            JSONArray featureArray = jsonObject.getJSONArray("features");

            for (int i = 0; i < featureArray.length(); i++) {
                JSONObject currentEarthQuake = featureArray.getJSONObject(i);
                JSONObject properties = currentEarthQuake.getJSONObject("properties");

                String magnitude = properties.getString("mag");
                String cityName = properties.getString("place");
                long timeInMilliseconds = properties.getLong("time");
                Date dateObject = new Date(timeInMilliseconds);

                SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM DD, yyyy");
                String dateToDisplay = dateFormatter.format(dateObject);

                EarthQuake earthQuake = new EarthQuake(cityName, magnitude, dateToDisplay);
                earthquakes.add(earthQuake);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }

}