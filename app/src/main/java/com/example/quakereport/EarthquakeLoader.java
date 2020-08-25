package com.example.quakereport;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

public class EarthquakeLoader extends AsyncTaskLoader<List<Quake>> {
    String mUrl;
    public EarthquakeLoader(Context context, String url)
    {
        super(context);
        mUrl=url;
    }
    @Nullable
    @Override
    public List<Quake> loadInBackground() {
        if (mUrl==null) {
            return null;
        }

        List<Quake> result = QueryUtils.fetchEarthquakeData(mUrl);
        return result;
    }
}
