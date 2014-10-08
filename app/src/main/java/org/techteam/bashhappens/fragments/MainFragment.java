package org.techteam.bashhappens.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.techteam.bashhappens.R;
import org.techteam.bashhappens.api.LangDirection;
import org.techteam.bashhappens.api.LanguageEntry;
import org.techteam.bashhappens.api.Translation;
import org.techteam.bashhappens.util.Keyboard;
import org.techteam.bashhappens.util.Toaster;

public class MainFragment extends Fragment
        implements TranslatorUI, SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String NAME = MainFragment.class.getName();

    private abstract class BundleKeys {
        public static final String FROM_LANGUAGE = "fromLanguage";
        public static final String TO_LANGUAGE = "toLanguage";
        public static final String TEXT_TO_TRANSLATE = "textToTranslate";
        public static final String TRANSLATED_TEXT = "translatedText";
        public static final String IS_TRANSLATING = "isTranslating";
    }

    private OnShowLanguagesListListener showLangsCallback;
    private OnTranslateListener translateCallback;

    private TextView translatedText;
    private EditText textToTranslate;
    private Button fromLanguageButton;
    private Button toLanguageButton;
    private Button translateButton;
    private ImageButton swapLanguagesButton;
    boolean isTranslating = false;


    private LanguageEntry fromLanguage;
    private LanguageEntry toLanguage;
    private boolean translateOnTheFly = true;

    public MainFragment() {
        super();
    }


    /************************** Lifecycle **************************/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.input_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            setFromLanguage((LanguageEntry) savedInstanceState.getParcelable(BundleKeys.FROM_LANGUAGE));
            setToLanguage((LanguageEntry) savedInstanceState.getParcelable(BundleKeys.TO_LANGUAGE));
            setTextToTranslate(savedInstanceState.getString(BundleKeys.TEXT_TO_TRANSLATE));
            setTranslatedText(savedInstanceState.getString(BundleKeys.TRANSLATED_TEXT));
            isTranslating = savedInstanceState.getBoolean(BundleKeys.IS_TRANSLATING);
        }
        if (isTranslating) {
            showProgress();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            showLangsCallback = (OnShowLanguagesListListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnItemSelectedListener");
        }

        try {
            translateCallback = (OnTranslateListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnTranslateListener");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(BundleKeys.FROM_LANGUAGE, fromLanguage);
        outState.putParcelable(BundleKeys.TO_LANGUAGE, toLanguage);
        outState.putString(BundleKeys.TEXT_TO_TRANSLATE, textToTranslate.getText().toString().trim());
        outState.putString(BundleKeys.TRANSLATED_TEXT, translatedText.getText().toString().trim());
        outState.putBoolean(BundleKeys.IS_TRANSLATING, isTranslating);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textToTranslate = (EditText) view.findViewById(R.id.text_to_translate);
        translatedText = (TextView) view.findViewById(R.id.translated_text);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        sharedPref.registerOnSharedPreferenceChangeListener(this);

        translateOnTheFly = sharedPref.getBoolean(getString(R.string.pref_translate_on_the_fly_key), true);

        textToTranslate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (MainFragment.this.translateOnTheFly)
                    translate();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        fromLanguageButton = (Button) view.findViewById(R.id.language_from_button);
        toLanguageButton = (Button) view.findViewById(R.id.language_to_button);
        translateButton = (Button) view.findViewById(R.id.translate_button);
        swapLanguagesButton = (ImageButton) view.findViewById(R.id.language_swap_button);

        fromLanguageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showList(LangDirection.FROM, fromLanguage, toLanguage);
            }
        });

        toLanguageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showList(LangDirection.TO, fromLanguage, toLanguage);
            }
        });

        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                translate();
            }
        });

        swapLanguagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swapLanguages();
            }
        });

        setFromLanguage(fromLanguage, false);
        setToLanguage(toLanguage, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //TODO: where we should unregister? here?
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        sharedPref.registerOnSharedPreferenceChangeListener(this);
    }

    /************************** TranslateUI  **************************/

    @Override
    public void setFromLanguage(LanguageEntry lang) {
        setFromLanguage(lang, translateOnTheFly);
    }

    @Override
    public void setToLanguage(LanguageEntry lang) {
        setToLanguage(lang, translateOnTheFly);
    }

    @Override
    public LanguageEntry getFromLanguage() {
        return fromLanguage;
    }

    @Override
    public LanguageEntry getToLanguage() {
        return toLanguage;
    }

    @Override
    public void setTextToTranslate(String text) {
        textToTranslate.setText(text);
    }

    @Override
    public void setTranslatedText(String text) {
        hideProgress();
        translatedText.setText(text);
    }

    @Override
    public void disableControls() {
        translatedText.setEnabled(false);
        textToTranslate.setEnabled(false);
        fromLanguageButton.setEnabled(false);
        toLanguageButton.setEnabled(false);
        translateButton.setEnabled(false);
        swapLanguagesButton.setEnabled(false);
    }

    /************************** Private stuff **************************/

    private void setFromLanguage(LanguageEntry lang, boolean instantTranslate) {
        if (lang != null) {
            fromLanguage = lang;
            fromLanguageButton.setText(lang.getName());
            if (instantTranslate) {
                translate();
            }
        }
    }
    private void setToLanguage(LanguageEntry lang, boolean translateOnTheFly) {
        if (lang != null) {
            toLanguage = lang;
            toLanguageButton.setText(lang.getName());
            if (translateOnTheFly) {
                translate();
            }
        }
    }

    private void translate() {
        String text = textToTranslate.getText().toString().trim();

        if (!text.equals("")) {
            if (fromLanguage == null) {
                Toaster.toast(getBaseContext(), R.string.from_language_not_selected);
            } else if (toLanguage == null) {
                Toaster.toast(getBaseContext(), R.string.to_language_not_selected);
            } else if (text.length() >= Translation.MAX_INPUT_TEXT_LENGTH) {
                Toaster.toast(getBaseContext(), R.string.text_is_too_long);
            } else {
                showProgress();
                translateCallback.onTranslate(text, fromLanguage, toLanguage);
            }
        }
        else {
            translatedText.setText("");
        }
    }

    private Context getBaseContext() {
        return MainFragment.this.getActivity().getBaseContext();
    }

    private void showList(LangDirection direction, LanguageEntry fromLanguage, LanguageEntry toLanguage) {
        Keyboard.hideSoftKeyboard(getActivity(), textToTranslate);
        showLangsCallback.onShowList(direction, fromLanguage, toLanguage);
    }

    private void swapLanguages() {
        if (fromLanguage != null && toLanguage != null) {
            LanguageEntry temp = fromLanguage;

            setFromLanguage(toLanguage, false);
            setToLanguage(temp, false);

            if (translateOnTheFly) {
                translate();
            }
        }
    }

    private void showProgress() {
        getActivity().setProgressBarIndeterminateVisibility(true);
        isTranslating = true;
    }

    private void hideProgress() {
        getActivity().setProgressBarIndeterminateVisibility(false);
        isTranslating = false;
    }



    public interface OnShowLanguagesListListener {
        void onShowList(LangDirection direction, LanguageEntry fromLanguage, LanguageEntry toLanguage);
    }

    public interface OnTranslateListener {
        void onTranslate(String text, LanguageEntry fromLanguage, LanguageEntry toLanguage);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        String expectedKey = getString(R.string.pref_translate_on_the_fly_key);

        if (key.equals(expectedKey)) {
            translateOnTheFly = sharedPreferences.getBoolean(expectedKey, true);
        }
    }
}
