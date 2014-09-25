package org.techteam.bashhappens.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.techteam.bashhappens.R;

public class CityDetailsFragment extends Fragment {
    private CityInfo cityInfo;

    public static CityDetailsFragment getInstance(CityInfo cityInfo) {
        CityDetailsFragment detailFragment = new CityDetailsFragment();
//        detailFragment.cityInfo = cityInfo;
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
