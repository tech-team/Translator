package org.techteam.bashhappens;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.techteam.bashhappens.api.LangDirection;
import org.techteam.bashhappens.api.LanguageEntry;
import org.techteam.bashhappens.api.TranslateErrors;
import org.techteam.bashhappens.fragments.TranslatorUI;
import org.techteam.bashhappens.fragments.LanguagesListFragment;
import org.techteam.bashhappens.api.LanguagesList;
import org.techteam.bashhappens.api.Translation;
import org.techteam.bashhappens.fragments.MainFragment;
import org.techteam.bashhappens.services.Constants;

public class MainActivity extends FragmentActivity implements LanguagesListFragment.OnLanguageSelectedListener, MainFragment.OnShowLanguagesListListener {

    private TranslationBroadcastReceiver translationBroadcastReceiver;
    private LanguagesList languagesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerBroadcastReceiver();

        String languageListData = getIntent().getStringExtra("data");
        languagesList = LanguagesList.fromJsonString(languageListData);


        TranslatorUI mainFragment = getTranslatorUIFragment();

        if (languagesList == null || languagesList.getLanguages().size() < 2) {
            mainFragment.disableControls();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefs = getPreferences(0);

        TranslatorUI f = getTranslatorUIFragment();

        f.setFromLanguage(new LanguageEntry(prefs.getString("fromLanguageName", languagesList.getLanguages().get(0).getName()),
                prefs.getString("fromLanguageUid", languagesList.getLanguages().get(0).getUid())));
        f.setToLanguage(new LanguageEntry(prefs.getString("toLanguageName", languagesList.getLanguages().get(1).getName()),
                prefs.getString("toLanguageUid", languagesList.getLanguages().get(1).getUid())));
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private TranslatorUI getTranslatorUIFragment() {

        // TODO: check for ClassCast
        return (TranslatorUI) getSupportFragmentManager().findFragmentByTag(getString(R.string.main_fragment_tag));
    }

    private void showToast(String message) {
        showToast(message, false);
    }

    private void showToast(String message, boolean longToast) {
        int length = longToast ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;
        Toast.makeText(
                this.getBaseContext(),
                message,
                length)
                .show();
    }

    private void registerBroadcastReceiver() {
        IntentFilter translationIntentFilter = new IntentFilter(
                Constants.TRANSLATE_BROADCAST_ACTION);
        translationBroadcastReceiver = new TranslationBroadcastReceiver();
        LocalBroadcastManager.getInstance(MainActivity.this)
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
                showToast(getString(R.string.action_settings));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstancestate) {
        super.onSaveInstanceState(savedInstancestate);
    }

    @Override
    public void onPause() {
        super.onPause();

        TranslatorUI f = getTranslatorUIFragment();

        SharedPreferences prefs = getPreferences(0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("fromLanguageUid", f.getFromLanguage().getUid());
        editor.putString("fromLanguageName", f.getFromLanguage().getName());
        editor.putString("toLanguageUid", f.getToLanguage().getUid());
        editor.putString("toLanguageName", f.getToLanguage().getName());
        editor.apply();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(MainActivity.this)
                .unregisterReceiver(translationBroadcastReceiver);
    }

    @Override
    public void onLanguageSelected(LanguageEntry entry, LangDirection direction) {
        getSupportFragmentManager().popBackStack();

        TranslatorUI f = getTranslatorUIFragment();

        switch (direction) {
            case FROM:
                f.setFromLanguage(entry);
                break;
            case TO:
                f.setToLanguage(entry);
                break;
        }
    }


    @Override
    public void onShowList(LangDirection direction, LanguageEntry fromLanguage, LanguageEntry toLanguage) {
        LanguagesListFragment listFragment = LanguagesListFragment.getInstance(languagesList, direction, fromLanguage, toLanguage);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, listFragment)
                .addToBackStack(LanguagesListFragment.NAME)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    private final class TranslationBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String data = intent.getStringExtra("data");
            if (data != null) {
                Translation translation = Translation.fromJsonString(data);

                if (translation.getCode() != TranslateErrors.ERR_OK) {
                    showToast(TranslateErrors.getErrorMessage(translation.getCode()));
                } else {
                    getTranslatorUIFragment().setTranslatedText(translation.getText());
                }
            }
            else {
                String exception = intent.getStringExtra("exception");
                Translation translation = new Translation(exception);
                showToast(translation.getException());
            }
        }
    }
}
