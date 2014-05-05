package com.github.gfx.leveldbexample.app;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

import im.amomo.leveldb.DBFactory;
import im.amomo.leveldb.LevelDB;


public class MainActivity extends Activity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


       textView = (TextView) findViewById(R.id.hello);
    }


    @Override
    protected void onResume() {
        super.onResume();

        textView.setText("");

        performLevelDB(1);
        performLevelDB(1);
        performLevelDB(10);
        performLevelDB(100);


        performShaedPreferences(1);
        performShaedPreferences(1);
        performShaedPreferences(10);
        performShaedPreferences(100);
    }

    private void performShaedPreferences(int n) {
        long t0 = System.currentTimeMillis();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        for (int i = 0; i < n; i++) {
            long value = sharedPrefs.getLong("foo", 0L);
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putLong("foo", value + 1L);
            editor.commit();
        }

        textView.append("SharedPreferences open, (get, put)*"+n+": " + (System.currentTimeMillis() - t0) + "ms\n");
    }

    private void performLevelDB(int n) {
        long t0 = System.currentTimeMillis();

        LevelDB db = DBFactory.open(this);

        for (int i = 0; i < n; i++) {
            if (db.exists("foo")) {
                long value = db.getLong("foo");
                db.put("foo", value + 1L);
            } else {
                db.put("foo", 1L);
            }
        }

        db.close();

        textView.append("LebelDB open, (get, put)*" + n + ", and close: " + (System.currentTimeMillis() - t0) + "ms\n");
    }
}
