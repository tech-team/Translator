package org.techteam.bashhappens.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.techteam.bashhappens.R;
import org.techteam.bashhappens.net.HttpDownloader;

import java.io.IOException;

public class CityDetailsFragment extends Fragment {

    class BundleKeys {
        public static final String CITY_NAME = "CITY_NAME";
        public static final String LAT = "LAT";
        public static final String LNG = "LNG";
    }

    private static final String LOG_TAG = CityDetailsFragment.class.getName();
    private static final String WEATHER_URL = "";
    private ProgressBar progressBar;

    public static CityDetailsFragment getInstance(CityInfo cityInfo) {
        CityDetailsFragment detailFragment = new CityDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BundleKeys.CITY_NAME, cityInfo.city);
        bundle.putDouble(BundleKeys.LAT, cityInfo.lat);
        bundle.putDouble(BundleKeys.LNG, cityInfo.lng);
        detailFragment.setArguments(bundle);
        return detailFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.city_details_fragment, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }

        CityInfo cityInfo = new CityInfo(bundle.getString(BundleKeys.CITY_NAME),
                                         bundle.getDouble(BundleKeys.LAT),
                                         bundle.getDouble(BundleKeys.LNG));

        new FetchWeatherAsync().execute(cityInfo);
    }

    class FetchWeatherAsync extends AsyncTask<CityInfo, Void, CityWeatherInfo> {

        private Throwable exception = null;
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected CityWeatherInfo doInBackground(CityInfo... cityInfos) {
            for (CityInfo cityInfo : cityInfos) {

                try {
                    String res = HttpDownloader.httpGet(WEATHER_URL);
                } catch (IOException e) {
                    exception = e;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(CityWeatherInfo weather) {
            progressBar.setVisibility(View.INVISIBLE);
            if (exception != null) {
                exception.printStackTrace();
                String text = "Error happened: " + exception.getMessage();
                Toast.makeText(getActivity().getApplicationContext(), text, Toast.LENGTH_LONG).show();
            }
        }
    }
}
