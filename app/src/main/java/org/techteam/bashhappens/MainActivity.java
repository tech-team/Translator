package org.techteam.bashhappens;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.techteam.bashhappens.api.LanguageEntry;
import org.techteam.bashhappens.fragments.LanguagesListFragment;
import org.techteam.bashhappens.api.LanguagesList;
import org.techteam.bashhappens.api.Translation;
import org.techteam.bashhappens.fragments.MainFragment;
import org.techteam.bashhappens.services.Constants;

public class MainActivity extends FragmentActivity implements LanguagesListFragment.OnLanguageSelectedListener, MainFragment.OnLanguagesRequestedListener {

    private TranslationBroadcastReceiver translationBroadcastReceiver;
    private static LanguagesList languagesList;

    private TextView translatedText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerBroadcastReceiver();

        String languageListData = getIntent().getStringExtra("data");
        languagesList = LanguagesList.fromJsonString(languageListData);

        translatedText = (TextView) findViewById(R.id.translated_text);
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
                Toast.makeText(this.getBaseContext(), "Settings", Toast.LENGTH_SHORT).show();
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
    public void onLanguageSelected(LanguageEntry entry) {
        Toast.makeText(
                this.getBaseContext(),
                "Selected language: " + entry.getName(),
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onShowLanguages(boolean left) {
        LanguagesListFragment.getInstance(languagesList).show(getSupportFragmentManager(), "languagesList");
    }

    private final class TranslationBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String data = intent.getStringExtra("data");
            if (data != null) {
                Translation translation = Translation.fromJsonString(data);
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
