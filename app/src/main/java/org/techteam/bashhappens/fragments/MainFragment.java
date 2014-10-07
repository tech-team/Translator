package org.techteam.bashhappens.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.techteam.bashhappens.R;
import org.techteam.bashhappens.api.LangDirection;
import org.techteam.bashhappens.api.LanguageEntry;
import org.techteam.bashhappens.api.Translation;
import org.techteam.bashhappens.services.IntentBuilder;
import org.techteam.bashhappens.util.Toaster;

public class MainFragment extends Fragment implements TranslatorUI {
    public static final String NAME = MainFragment.class.getName();

    private abstract class BundleKeys {
        public static final String FROM_LANGUAGE = "fromLanguage";
        public static final String TO_LANGUAGE = "toLanguage";
        public static final String TEXT_TO_TRANSLATE = "textToTranslate";
        public static final String TRANSLATED_TEXT = "translatedText";
    }

    private OnShowLanguagesListListener mCallback;
    private Intent translationIntent = null;

    private TextView translatedText;
    private EditText textToTranslate;
    private Button fromLanguageButton;
    private Button toLanguageButton;
    private Button translateButton;
    private ImageButton swapLanguagesButton;


    private LanguageEntry fromLanguage;
    private LanguageEntry toLanguage;
    private boolean instantTranslate = true;

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
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (OnShowLanguagesListListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnItemSelectedListener");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstancestate) {
        super.onSaveInstanceState(savedInstancestate);

        savedInstancestate.putParcelable(BundleKeys.FROM_LANGUAGE, fromLanguage);
        savedInstancestate.putParcelable(BundleKeys.TO_LANGUAGE, toLanguage);
        savedInstancestate.putString(BundleKeys.TEXT_TO_TRANSLATE, textToTranslate.getText().toString().trim());
        savedInstancestate.putString(BundleKeys.TRANSLATED_TEXT, translatedText.getText().toString().trim());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        translatedText = (TextView) view.findViewById(R.id.translated_text);
        textToTranslate = (EditText) view.findViewById(R.id.text_to_translate);
        textToTranslate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
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



    }

    /************************** TranslateUI  **************************/

    @Override
    public void setFromLanguage(LanguageEntry lang) {
        setFromLanguage(lang, instantTranslate);
    }

    @Override
    public void setToLanguage(LanguageEntry lang) {
        setToLanguage(lang, instantTranslate);
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
    public void setToLanguage(LanguageEntry lang, boolean instantTranslate) {
        if (lang != null) {
            toLanguage = lang;
            toLanguageButton.setText(lang.getName());
            if (instantTranslate) {
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
                if (translationIntent != null) {
                    getActivity().stopService(translationIntent);
                }
                translationIntent = IntentBuilder.translateIntent(getActivity(), text, fromLanguage.getUid(), toLanguage.getUid());
                getActivity().startService(translationIntent);
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
        hideSoftKeyboard(textToTranslate);
        mCallback.onShowList(direction, fromLanguage, toLanguage);
    }

    private void swapLanguages() {
        if (fromLanguage != null && toLanguage != null) {
            LanguageEntry temp = fromLanguage;

            setFromLanguage(toLanguage, false);
            setToLanguage(temp, false);

            if (instantTranslate) {
                translate();
            }
        }
    }

    private void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }



    public interface OnShowLanguagesListListener {
        void onShowList(LangDirection direction, LanguageEntry fromLanguage, LanguageEntry toLanguage);
    }
}
