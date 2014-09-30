package org.techteam.bashhappens.fragments;

import android.app.Activity;
import android.opengl.EGLExt;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.techteam.bashhappens.R;

import java.util.ArrayList;
import java.util.List;

public class CitiesListFragment extends Fragment {
    private List<CityInfo> cities = new ArrayList<CityInfo>();
    private OnCitySelectedListener mCallback;

    public CitiesListFragment() {
        super();

        cities.add(new CityInfo("Москва", 55.750934, 37.621319));
        cities.add(new CityInfo("Санкт-Петербург", 59.934516, 30.327412));
        cities.add(new CityInfo("Нижний Новгород", 56.326994, 44.006168));
        cities.add(new CityInfo("Казань", 55.796372, 49.111794));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cities_list_fragment, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnCitySelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnItemSelectedListener");
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView list = (ListView) view.findViewById(R.id.cities_list);
        list.setAdapter(new NewsListAdapter(cities));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                CityInfo city = cities.get(position);
                mCallback.onCitySelected(city);
            }
        });
    }

    // Container Activity must implement this interface
    public interface OnCitySelectedListener {
        public void onCitySelected(CityInfo cityInfo);
    }

    private class NewsListAdapter extends ArrayAdapter<CityInfo> {

        public NewsListAdapter(List<CityInfo> objects) {
            super(getActivity(), 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.text = (TextView) convertView.findViewById(android.R.id.text1);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.text.setText(getItem(position).getCity());
            return convertView;
        }

        private class ViewHolder {
            public TextView text;
        }
    }
}
