package org.techteam.bashhappens.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
    private static final String LANGUAGES_LIST_KEY = "languages_list";
    private static final String LANGUAGES_DIRECTION_KEY = "languages_direction";
    private LanguagesList languages = null;
    private LangDirection langDirection = null;
    private OnLanguageSelectedListener mCallback;

    private List<LanguageEntry> displayList = null;
    private LanguageEntry fromLang = null;
    private LanguageEntry toLang = null;

    public static LanguagesListFragment getInstance(LanguagesList languagesList, LangDirection direction, LanguageEntry fromLang, LanguageEntry toLang) {
        LanguagesListFragment f = new LanguagesListFragment();
        f.languages = languagesList;
        f.langDirection = direction;
        f.fromLang = fromLang;
        f.toLang = toLang;

//        Bundle bundle = new Bundle();
//        bundle.putParcelableArrayList(LANGUAGES_LIST_KEY, languagesList.getFr);
//        bundle.putString(LANGUAGES_DIRECTION_KEY, direction.toString());
//        languagesListFragment.setArguments(bundle);
        return f;
    }

    public LanguagesListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Translucent);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
//        WindowManager.LayoutParams p = getDialog().getWindow().getAttributes();
//        p.width = ViewGroup.LayoutParams.MATCH_PARENT;
//        p.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;
//        p.x = 200;
//        getDialog().getWindow().setAttributes(p);
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

//        Bundle args = getArguments();
//        if (args != null) {
//            languages = args.getParcelableArrayList(LANGUAGES_LIST_KEY);
//            langDirection = LangDirection.valueOf(args.getString(LANGUAGES_DIRECTION_KEY));
//        }

        LinearLayout layout = (LinearLayout) view.findViewById(R.id.languages_list_layout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        ListView list = (ListView) view.findViewById(R.id.languages_list);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(list.getLayoutParams());
        switch (langDirection) {
            case FROM:
                lp.gravity = Gravity.LEFT;
//                if (toLang == null) {
//                    displayList = languages.getFromLangs();
//                } else {
//                    displayList = languages.getFromLangs(toLang);
//                }
                break;
            case TO:
                lp.gravity = Gravity.RIGHT;
//                if (fromLang == null) {
//                    displayList = languages.getToLangs();
//                } else {
//                    displayList = languages.getToLangs(fromLang);
//                }
                break;
        }

        displayList = languages.getLanguages();

        list.setLayoutParams(lp);
        list.setAdapter(new LanguageListAdapter(displayList));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                LanguageEntry entry = displayList.get(position);
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
