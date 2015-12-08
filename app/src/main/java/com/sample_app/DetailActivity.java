package com.sample_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jatin1 on 12/8/15.
 */
public class DetailActivity extends AppCompatActivity {
    private String mItemName;
    private String jsonStr;
    public static String LOG_TAG = DetailActivity.class.getName();

    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
        if (saved == null) {
            mItemName = getIntent().getStringExtra(Intent.EXTRA_TEXT);
            jsonStr = getIntent().getStringExtra("JSON String");
        }
        setContentView(R.layout.detail_activity);

        TextView textView = (TextView) findViewById(R.id.text_view);
        textView.setText(mItemName);

        textView = (TextView) findViewById(R.id.text_view2);
        TextView textView1 = (TextView) findViewById(R.id.text_view3);
        try {
            JSONArray foodJson =  new JSONArray(jsonStr);
            for (int i=0; i<foodJson.length(); i++) {
                JSONObject object = foodJson.getJSONObject(i).getJSONObject("fields");
                if (object.getString("name").equals(mItemName)) {
                    textView.setText("Calories " + object.getString("calorie_val"));
                    textView1.setText("Local Name " + object.getString("local_name"));
                    break;
                }
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "JSON Error due to " + e);
        }
    }
}
