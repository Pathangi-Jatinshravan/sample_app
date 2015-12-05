package com.sample_app;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private ArrayAdapter<String> mAdapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new ArrayAdapter<String>(this, R.layout.food_listview, R.id.list_item_food_textview);
        setContentView(R.layout.app_bar_main);
        FetchTask task = new FetchTask();
        task.execute();
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(mAdapter);
    }

    public class FetchTask extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = FetchTask.class.getName();
        String[] resultStrs = null;

        @Override
        public String[] doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String jsonStr = null;
            try {
                URL url = new URL("http://frozen-tundra-3555.herokuapp.com/foods/");
                urlConnection = (HttpURLConnection) (url.openConnection());
                urlConnection.setRequestMethod("GET");

                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder builder = new StringBuilder();

                if (inputStream != null) {
                    reader = new BufferedReader (new InputStreamReader(inputStream));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                }
                jsonStr = builder.toString();
                jsonStr = jsonStr.replace("\\", "");
                jsonStr = jsonStr.replaceAll("^\"|\"$", "");

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error  " + e);
            }finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "Error closing reading stream " + e);
                    }
                }
            }

            try {
                Log.d(LOG_TAG, jsonStr);
                JSONArray foodJson =  new JSONArray(jsonStr);
                resultStrs = new String[foodJson.length()];
                for (int i=0; i < foodJson.length(); i++) {
                    JSONObject object = foodJson.getJSONObject(i);
                    JSONObject fields = object.getJSONObject("fields");
                    String name = fields.getString("name");
                    resultStrs[i] = name;
                }

                return resultStrs;
            } catch (JSONException e) {
                Log.e(LOG_TAG, "JSON error " + e);
            }
            return null;
        }

        @Override
        public void onPostExecute(String[] result) {
            if (result != null) {
                mAdapter.notifyDataSetChanged();
                mAdapter.clear();
                for (String s : result) {
                    mAdapter.add(s);
                }
            }
        }
    }
}
