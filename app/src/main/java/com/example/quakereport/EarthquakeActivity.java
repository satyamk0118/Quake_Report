package com.example.quakereport;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import java.util.ArrayList;
import java.util.List;


public class EarthquakeActivity extends AppCompatActivity {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";
    private QuakeAdapter mAdapter;
    private static final int EARTHQUAKE_LOADER_ID = 1;
    private TextView emptyView;
    private ProgressBar bar;
    boolean isConnected;
    LoaderManager.LoaderCallbacks<List<Quake>> loaderCallbacks= new LoaderManager.LoaderCallbacks<List<Quake>>() {
        @NonNull
        @Override
        public Loader<List<Quake>> onCreateLoader(int id, @Nullable Bundle args) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(EarthquakeActivity.this);
            String minMagnitude = sharedPrefs.getString(
                    getString(R.string.settings_min_magnitude_key),
                    getString(R.string.settings_min_magnitude_default));
            String orderBy = sharedPrefs.getString(
                    getString(R.string.settings_order_by_key),
                    getString(R.string.settings_order_by_default));
            Uri baseUri = Uri.parse(USGS_REQUEST_URL);
            Uri.Builder uriBuilder = baseUri.buildUpon();

            uriBuilder.appendQueryParameter("format", "geojson");
            uriBuilder.appendQueryParameter("limit", "10");
            uriBuilder.appendQueryParameter("minmag", minMagnitude);
            uriBuilder.appendQueryParameter("orderby", orderBy);

            return new EarthquakeLoader(EarthquakeActivity.this, uriBuilder.toString());
        }

        @Override
        public void onLoadFinished(@NonNull Loader<List<Quake>> loader, List<Quake> data) {
            bar.setVisibility(View.GONE);
            if(isConnected)
            emptyView.setText(R.string.empty_state);
            else
                emptyView.setText("No internet connection");

            mAdapter.clear();
            // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
            // data set. This will trigger the ListView to update.
            if (data != null && !data.isEmpty()) {
                mAdapter.addAll(data);
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<List<Quake>> loader) {
            mAdapter.clear();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        // Create a fake list of earthquake locations.
        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        emptyView = (TextView)findViewById(R.id.empty_view);
        earthquakeListView.setEmptyView(emptyView);
        bar = (ProgressBar)findViewById(R.id.progress_circular);
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork!=null && activeNetwork.isConnectedOrConnecting();
        mAdapter = new QuakeAdapter(this, new ArrayList<Quake>());
        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Quake quake = mAdapter.getItem(i);
                Uri earthquake = Uri.parse(quake.getUrl());
                Intent webIntent = new Intent(Intent.ACTION_VIEW,earthquake);
                startActivity(webIntent);
            }
        });
        getSupportLoaderManager().initLoader(EARTHQUAKE_LOADER_ID,null,loaderCallbacks).forceLoad();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
