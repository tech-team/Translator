package org.techteam.bashhappens;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.techteam.bashhappens.api.LangDirection;
import org.techteam.bashhappens.api.LanguageEntry;
import org.techteam.bashhappens.fragments.LanguagesListFragment;
import org.techteam.bashhappens.api.LanguagesList;
import org.techteam.bashhappens.api.Translation;
import org.techteam.bashhappens.services.Constants;
import org.techteam.bashhappens.services.IntentBuilder;

public class MainActivity extends FragmentActivity implements LanguagesListFragment.OnLanguageSelectedListener {

    private TranslationBroadcastReceiver translationBroadcastReceiver;
    private LanguagesList languagesList;

    private TextView translatedText;
    private EditText textToTranslate;
    private Button fromLanguageButton;
    private Button toLanguageButton;

    private LanguageEntry fromLanguage;
    private LanguageEntry toLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerBroadcastReceiver();

        String languageListData = getIntent().getStringExtra("data");
        languagesList = LanguagesList.fromJsonString(languageListData);

        translatedText = (TextView) findViewById(R.id.translated_text);
        textToTranslate = (EditText) findViewById(R.id.text_to_translate);

        fromLanguageButton = (Button) findViewById(R.id.language_from_button);
        toLanguageButton = (Button) findViewById(R.id.language_to_button);
        Button translateButton = (Button) findViewById(R.id.translate_button);
        ImageButton swapLanguagesButton = (ImageButton) findViewById(R.id.language_swap_button);

        fromLanguageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onShowLanguages(LangDirection.FROM);
            }
        });

        toLanguageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onShowLanguages(LangDirection.TO);
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
                } else {
                    startService(IntentBuilder.translateIntent(MainActivity.this,
                                text, fromLanguage.getUid(), toLanguage.getUid()));
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

    private void swapLanguages() {
        if (fromLanguage != null && toLanguage != null) {
            LanguageEntry temp = fromLanguage;
            fromLanguage = toLanguage;
            toLanguage = temp;

            fromLanguageButton.setText(fromLanguage.getName());
            toLanguageButton.setText(toLanguage.getName());
        }
    }

    private void showToast(String message) {
        Toast.makeText(
                this.getBaseContext(),
                message,
                Toast.LENGTH_SHORT)
                .show();
    }

    private void registerBroadcastReceiver() {
        IntentFilter translationIntentFilter = new IntentFilter(
                Constants.TRANSLATE_BROADCAST_ACTION);
        translationBroadcastReceiver = new TranslationBroadcastReceiver();
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(translationBroadcastReceiver, translationIntentFilter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                showToast("Settings");
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this)
                .unregisterReceiver(translationBroadcastReceiver);
    }

    @Override
    public void onLanguageSelected(LanguageEntry entry, LangDirection direction) {
        switch (direction) {
            case FROM:
                fromLanguage = entry;
                fromLanguageButton.setText(entry.getName());
                break;
            case TO:
                toLanguage = entry;
                toLanguageButton.setText(entry.getName());
                break;
        }
        getSupportFragmentManager().popBackStack();
    }

    public void onShowLanguages(LangDirection direction) {
        LanguagesListFragment listFragment = LanguagesListFragment.getInstance(languagesList, direction);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, listFragment)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    private final class TranslationBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String data = intent.getStringExtra("data");
            if (data != null) {
                Translation translation = Translation.fromJsonString(data);
                // TODO: handle error codes
                translatedText.setText(translation.getText());
                //TODO: todo todooo todooo
            }
            else {
                String exception = intent.getStringExtra("exception");
                Translation translation = new Translation(exception);
                //TODO: what next, cap'n ?
            }
        }
    }
}
