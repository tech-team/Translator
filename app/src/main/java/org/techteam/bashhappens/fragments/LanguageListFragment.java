package org.techteam.bashhappens.fragments;

import android.app.Activity;
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
import org.techteam.bashhappens.api.LanguageEntry;

import java.util.ArrayList;
import java.util.List;

public class LanguageListFragment extends Fragment {
    private List<LanguageEntry> languages = new ArrayList<LanguageEntry>();
    private OnLanguageSelectedListener mCallback;

    public LanguageListFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.language_list_fragment, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnLanguageSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnItemSelectedListener");
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView list = (ListView) view.findViewById(R.id.languages_list);
        list.setAdapter(new LanguageListAdapter(languages));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                LanguageEntry entry = languages.get(position);
                mCallback.onLanguageSelected(entry);
            }
        });
    }

    // Container Activity must implement this interface
    public interface OnLanguageSelectedListener {
        public void onLanguageSelected(LanguageEntry language);
    }

    private class LanguageListAdapter extends ArrayAdapter<LanguageEntry> {

        public LanguageListAdapter(List<LanguageEntry> objects) {
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

            viewHolder.text.setText(getItem(position).getName());
            return convertView;
        }

        private class ViewHolder {
            public TextView text;
        }
    }
}
