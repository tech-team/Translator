package org.techteam.bashhappens.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.techteam.bashhappens.R;

public class CityDetailsFragment extends Fragment {

    class BundleKeys {
        public static final String CITY_NAME = "CITY_NAME";
        public static final String LAT = "LAT";
        public static final String LNG = "LNG";
    }

    private static final String LOG_TAG = CityDetailsFragment.class.getName();

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
        TextView text = (TextView) view.findViewById(R.id.city_details_name);
        text.setText("HI!!!");
    }
}
