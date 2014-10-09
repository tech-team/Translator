package org.techteam.bashhappens.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.techteam.bashhappens.R;
import org.techteam.bashhappens.api.LangDirection;
import org.techteam.bashhappens.api.LanguageEntry;
import org.techteam.bashhappens.api.LanguagesList;

import java.util.ArrayList;
import java.util.List;

public class LanguagesListFragment extends Fragment {
    public static final String NAME = LanguagesListFragment.class.getName();

    private abstract class BundleKeys {
        public static final String LANGUAGES_LIST = "languages_list";
        public static final String LANG_DIRECTION = "languages_direction";
    }

    private ArrayList<LanguageEntry> languages = null;
    private LangDirection langDirection = null;
    private OnLanguageSelectedListener mCallback;


    public static LanguagesListFragment newInstance(LanguagesList languagesList, LangDirection direction) {
        LanguagesListFragment f = new LanguagesListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(BundleKeys.LANGUAGES_LIST, languagesList.getLanguages());
        bundle.putString(BundleKeys.LANG_DIRECTION, direction.toString());
        f.setArguments(bundle);
        return f;
    }

    public LanguagesListFragment() {
    }

    /************************** Lifecycle **************************/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.languages_list_fragment, container, false);
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

        Bundle args = getArguments();
        if (args != null) {
            languages = args.getParcelableArrayList(BundleKeys.LANGUAGES_LIST);
            langDirection = LangDirection.valueOf(args.getString(BundleKeys.LANG_DIRECTION));
        }

        LinearLayout listLayout = (LinearLayout) view.findViewById(R.id.languages_list_layout);
        listLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getFragmentManager().popBackStack();
            }
        });

        LinearLayout langChooser = (LinearLayout) getActivity().findViewById(R.id.language_chooser_panel);
        langChooser.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int height = langChooser.getMeasuredHeight();

        listLayout.setPadding(listLayout.getPaddingLeft(),
                              height,
                              listLayout.getPaddingRight(),
                              listLayout.getPaddingBottom());

        ListView list = (ListView) view.findViewById(R.id.languages_list);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(list.getLayoutParams());
        switch (langDirection) {
            case FROM:
                lp.gravity = Gravity.LEFT;
                break;
            case TO:
                lp.gravity = Gravity.RIGHT;
                break;
        }

        list.setLayoutParams(lp);
        list.setAdapter(new LanguageListAdapter(languages));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                LanguageEntry entry = languages.get(position);
                mCallback.onLanguageSelected(entry, langDirection);
            }
        });
    }





    // Container Activity must implement this interface
    public interface OnLanguageSelectedListener {
        public void onLanguageSelected(LanguageEntry language, LangDirection direction);
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
