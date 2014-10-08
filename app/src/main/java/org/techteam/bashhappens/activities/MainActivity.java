package org.techteam.bashhappens.activities;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import org.techteam.bashhappens.R;
import org.techteam.bashhappens.api.LangDirection;
import org.techteam.bashhappens.api.LanguageEntry;
import org.techteam.bashhappens.api.LanguagesList;
import org.techteam.bashhappens.api.TranslateErrors;
import org.techteam.bashhappens.api.Translation;
import org.techteam.bashhappens.fragments.LanguagesListFragment;
import org.techteam.bashhappens.fragments.MainFragment;
import org.techteam.bashhappens.fragments.SplashFragment;
import org.techteam.bashhappens.fragments.TranslatorUI;
import org.techteam.bashhappens.services.BroadcastIntents;
import org.techteam.bashhappens.services.IntentBuilder;
import org.techteam.bashhappens.services.ResponseKeys;
import org.techteam.bashhappens.util.Toaster;

public class MainActivity extends Activity
    implements
        LanguagesListFragment.OnLanguageSelectedListener,
        MainFragment.OnShowLanguagesListListener,
        MainFragment.OnTranslateListener {

    private abstract class PrefsKeys {
        public static final String FROM_LANGUAGE_UID = "fromLanguageUid";
        public static final String TO_LANGUAGE_UID = "toLanguageUid";
    }

    private LanguageListBroadcastReceiver languageListBroadcastReceiver;
    private TranslationBroadcastReceiver translationBroadcastReceiver;
    private Intent translationIntent = null;
    private LanguagesList languagesList;

    /************************** Lifecycle **************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        registerBroadcastReceivers();

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        String languageListData = getIntent().getStringExtra(ResponseKeys.DATA);
        languagesList = LanguagesList.fromJsonString(languageListData);

        if (findViewById(android.R.id.content) != null) {
            if (savedInstanceState != null) {
                return;
            }

            getFragmentManager().beginTransaction()
                    .add(android.R.id.content, new SplashFragment(), SplashFragment.NAME)
                    .commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        TranslatorUI f = getTranslatorUIFragment();

        if (languagesList == null || languagesList.getLanguages().size() < 2) {
            Toaster.toastLong(MainActivity.this.getBaseContext(), R.string.unable_to_load_lang_list);
            f.disableControls();
        } else {
            SharedPreferences prefs = getPreferences(0);

            String fromUid = prefs.getString(PrefsKeys.FROM_LANGUAGE_UID, languagesList.getLanguages().get(0).getUid());
            String toUid = prefs.getString(PrefsKeys.TO_LANGUAGE_UID, languagesList.getLanguages().get(1).getUid());

            LanguageEntry fromLang = new LanguageEntry(languagesList.getLanguageName(fromUid), fromUid);
            LanguageEntry toLang = new LanguageEntry(languagesList.getLanguageName(toUid), toUid);

            f.setFromLanguage(fromLang);
            f.setToLanguage(toLang);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        TranslatorUI f = getTranslatorUIFragment();

        SharedPreferences prefs = getPreferences(0);
        SharedPreferences.Editor editor = prefs.edit();

        LanguageEntry fromLang = f.getFromLanguage();
        LanguageEntry toLang = f.getToLanguage();

        if (fromLang != null) {
            editor.putString(PrefsKeys.FROM_LANGUAGE_UID, fromLang.getUid());
        }
        if (toLang != null) {
            editor.putString(PrefsKeys.TO_LANGUAGE_UID, toLang.getUid());
        }
        editor.apply();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        unRegisterBroadcastReceivers();
    }

    private void unRegisterBroadcastReceivers() {
        LocalBroadcastManager.getInstance(MainActivity.this)
                .unregisterReceiver(translationBroadcastReceiver);

        LocalBroadcastManager.getInstance(MainActivity.this)
                .unregisterReceiver(languageListBroadcastReceiver);
    }

    /************************** Private stuff **************************/

    private void registerBroadcastReceivers() {
        languageListBroadcastReceiver = new LanguageListBroadcastReceiver();
        IntentFilter languageListIntentFilter = new IntentFilter(
                BroadcastIntents.LANGUAGE_LIST_BROADCAST_ACTION);
        LocalBroadcastManager.getInstance(MainActivity.this)
                .registerReceiver(languageListBroadcastReceiver, languageListIntentFilter);

        if (translationBroadcastReceiver == null) {
            IntentFilter translationIntentFilter = new IntentFilter(
                    BroadcastIntents.TRANSLATE_BROADCAST_ACTION);
            translationBroadcastReceiver = new TranslationBroadcastReceiver();
            LocalBroadcastManager.getInstance(MainActivity.this)
                    .registerReceiver(translationBroadcastReceiver, translationIntentFilter);
        }
    }

    private TranslatorUI getTranslatorUIFragment() {

        // TODO: check for ClassCast
        return (TranslatorUI) getFragmentManager().findFragmentByTag(MainFragment.NAME);
    }

    /************************** Menu **************************/

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
                showSettings();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSettings() {
        Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(settingsIntent);
    }

    /************************** Callbacks **************************/

    @Override
    public void onLanguageSelected(LanguageEntry entry, LangDirection direction) {
        TranslatorUI f = getTranslatorUIFragment();

        switch (direction) {
            case FROM:
                f.setFromLanguage(entry);
                break;
            case TO:
                f.setToLanguage(entry);
                break;
        }

        getFragmentManager().popBackStack();
    }


    @Override
    public void onShowList(LangDirection direction, LanguageEntry fromLanguage, LanguageEntry toLanguage) {
        LanguagesListFragment listFragment = LanguagesListFragment.getInstance(languagesList, direction);
        getFragmentManager().beginTransaction()
                .add(android.R.id.content, listFragment, LanguagesListFragment.NAME)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    @Override
    public void onTranslate(String text, LanguageEntry fromLanguage, LanguageEntry toLanguage) {
        if (translationIntent != null) {
            stopService(translationIntent);
        }
        translationIntent = IntentBuilder.translateIntent(MainActivity.this, text, fromLanguage.getUid(), toLanguage.getUid());
        startService(translationIntent);
    }






    private final class TranslationBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String data = intent.getStringExtra(ResponseKeys.DATA);
            if (data != null) {
                Translation translation = Translation.fromJsonString(data);

                if (translation.getCode() != TranslateErrors.ERR_OK) {
                    Toaster.toast(MainActivity.this.getBaseContext(),
                            TranslateErrors.getErrorMessage(translation.getCode()));
                } else {
                    getTranslatorUIFragment().setTranslatedText(translation.getText());
                }
            }
            else {
                String exception = intent.getStringExtra(ResponseKeys.ERROR);
                Translation translation = new Translation(exception);
                Toaster.toastLong(MainActivity.this.getBaseContext(),
                        translation.getException());
            }
        }
    }


    private final class LanguageListBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            onLanguageListFetched(intent.getExtras());
        }
    }

    private void onLanguageListFetched(Bundle bundle) {
        //TODO: hide splash

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new MainFragment(), MainFragment.NAME)
                .commit();
    }
}
