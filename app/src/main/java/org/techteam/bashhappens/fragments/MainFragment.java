package org.techteam.bashhappens.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.techteam.bashhappens.R;
import org.techteam.bashhappens.services.IntentBuilder;

public class MainFragment extends Fragment {
    private OnLanguagesRequestedListener mCallback;

    public MainFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final EditText textToTranslate = (EditText) getActivity().findViewById(R.id.text_to_translate);

        Button fromLanguageButton = (Button) getActivity().findViewById(R.id.language_from_button);
        Button toLanguageButton = (Button) getActivity().findViewById(R.id.language_to_button);
        Button translateButton = (Button) getActivity().findViewById(R.id.translate_button);

        fromLanguageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onShowLanguages(true);
            }
        });

        toLanguageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onShowLanguages(false);
            }
        });

        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity = getActivity();
                activity.startService(
                        IntentBuilder.translateIntent(activity,
                                                      textToTranslate.getText().toString(),
                                                      "en", "ru"));
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (OnLanguagesRequestedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnLanguagesRequestedListener");
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public interface OnLanguagesRequestedListener {
        void onShowLanguages(boolean left);
    }
}
