package org.techteam.bashhappens.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.techteam.bashhappens.R;
import org.techteam.bashhappens.api.LangDirection;
import org.techteam.bashhappens.api.LanguageEntry;
import org.techteam.bashhappens.api.Translation;
import org.techteam.bashhappens.services.IntentBuilder;

public class MainFragment extends Fragment implements TranslatorUI {
    public static final String NAME = MainFragment.class.getName();

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

    public MainFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.input_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            setFromLanguage((LanguageEntry) savedInstanceState.getParcelable("fromLanguage"));
            setToLanguage((LanguageEntry) savedInstanceState.getParcelable("toLanguage"));
            setTextToTranslate(savedInstanceState.getString("textToTranslate"));
            setTranslatedText(savedInstanceState.getString("translatedText"));
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
    public void onDestroy() {
        super.onDestroy();
        Log.d(NAME, "MainFragment.destroy");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstancestate) {
        super.onSaveInstanceState(savedInstancestate);

        savedInstancestate.putParcelable("fromLanguage", fromLanguage);
        savedInstancestate.putParcelable("toLanguage", toLanguage);
        savedInstancestate.putString("textToTranslate", textToTranslate.getText().toString().trim());
        savedInstancestate.putString("translatedText", translatedText.getText().toString().trim());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        translatedText = (TextView) view.findViewById(R.id.translated_text);
        textToTranslate = (EditText) view.findViewById(R.id.text_to_translate);

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
                String text = textToTranslate.getText().toString().trim();

                if (fromLanguage == null) {
                    showToast(getString(R.string.from_language_not_selected));
                } else if (toLanguage == null) {
                    showToast(getString(R.string.to_language_not_selected));
                } else if (text.equals("")) {
                    showToast(getString(R.string.text_to_translate_not_filled));
                } else if (text.length() >= Translation.MAX_INPUT_TEXT_LENGTH) {
                    showToast(getString(R.string.text_is_too_long));
                } else {
                    if (translationIntent != null) {
                        getActivity().stopService(translationIntent);
                    }
                    translationIntent = IntentBuilder.translateIntent(getActivity(), text, fromLanguage.getUid(), toLanguage.getUid());
                    getActivity().startService(translationIntent);
                }
            }
        });

        swapLanguagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swapLanguages();
            }
        });



    }

    private void showList(LangDirection direction, LanguageEntry fromLanguage, LanguageEntry toLanguage) {
        hideSoftKeyboard(textToTranslate);
        mCallback.onShowList(direction, fromLanguage, toLanguage);
    }

    public void setFromLanguage(LanguageEntry lang) {
        if (lang != null) {
            fromLanguage = lang;
            fromLanguageButton.setText(lang.getName());
        }
    }

    public void setToLanguage(LanguageEntry lang) {
        if (lang != null) {
            toLanguage = lang;
            toLanguageButton.setText(lang.getName());
        }
    }

    public LanguageEntry getFromLanguage() {
        return fromLanguage;
    }

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
        showToast(getString(R.string.unable_to_load_lang_list), true);
        translatedText.setEnabled(false);
        textToTranslate.setEnabled(false);
        fromLanguageButton.setEnabled(false);
        toLanguageButton.setEnabled(false);
        translateButton.setEnabled(false);
        swapLanguagesButton.setEnabled(false);
    }

    private void swapLanguages() {
        if (fromLanguage != null && toLanguage != null) {
            LanguageEntry temp = fromLanguage;

            setFromLanguage(toLanguage);
            setToLanguage(temp);
        }
    }


    private void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }




    private void showToast(String message) {
        showToast(message, false);
    }

    private void showToast(String message, boolean longToast) {
        int length = longToast ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;
        Toast.makeText(
                MainFragment.this.getActivity().getBaseContext(),
                message,
                length)
                .show();
    }


    public interface OnShowLanguagesListListener {
        void onShowList(LangDirection direction, LanguageEntry fromLanguage, LanguageEntry toLanguage);
    }
}
