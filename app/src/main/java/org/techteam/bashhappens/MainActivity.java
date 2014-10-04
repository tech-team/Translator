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
import android.widget.Toast;

import org.techteam.bashhappens.api.LanguageEntry;
import org.techteam.bashhappens.api.LanguageList;
import org.techteam.bashhappens.api.Translation;
import org.techteam.bashhappens.fragments.LanguageListFragment;
import org.techteam.bashhappens.services.Constants;

public class MainActivity extends FragmentActivity implements LanguageListFragment.OnLanguageSelectedListener {

    private TranslationBroadcastReceiver translationBroadcastReceiver;
    private LanguageListBroadcastReceiver languageListBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.languages_fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.languages_fragment_container, new LanguageListFragment())
                    .commit();
        }

        //TODO: simplify?
        IntentFilter translationIntentFilter = new IntentFilter(
                Constants.TRANSLATE_BROADCAST_ACTION);
        IntentFilter languageListIntentFilter = new IntentFilter(
                Constants.LANGUAGE_LIST_BROADCAST_ACTION);
        translationBroadcastReceiver = new TranslationBroadcastReceiver();
        languageListBroadcastReceiver = new LanguageListBroadcastReceiver();
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(translationBroadcastReceiver, translationIntentFilter);
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(languageListBroadcastReceiver, languageListIntentFilter);
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
            case R.id.action_language_from:
                Toast.makeText(this.getBaseContext(), "From", Toast.LENGTH_SHORT).show();
                break;

            case R.id.action_language_to:
                Toast.makeText(this.getBaseContext(), "To", Toast.LENGTH_SHORT).show();
                break;

            case R.id.action_language_swap:
                Toast.makeText(this.getBaseContext(), "Swap", Toast.LENGTH_SHORT).show();
                break;

            case R.id.action_settings:
                Toast.makeText(this.getBaseContext(), "Settings", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLanguageSelected(LanguageEntry entry) {
        Toast.makeText(
                this.getBaseContext(),
                "Selected language: " + entry.getName(),
                Toast.LENGTH_SHORT)
                .show();

        //TODO: wut?
        /*CityDetailsFragment newFragment = CityDetailsFragment.getInstance(cityInfo);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.cities_fragment_container, newFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(null);
        transaction.commit();*/
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(this)
                .unregisterReceiver(translationBroadcastReceiver);
        LocalBroadcastManager.getInstance(this)
                .unregisterReceiver(languageListBroadcastReceiver);
    }

    private final class TranslationBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String data = intent.getStringExtra("data");
            if (data != null) {
                Translation translation = Translation.fromJsonString(data);
                //TODO: todo todooo todooo
            }
            else {
                String exception = intent.getStringExtra("exception");
                Translation translation = new Translation(exception);
                //TODO: what next, cap'n ?
            }
        }
    }

    private final class LanguageListBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String data = intent.getStringExtra("data");
            if (data != null) {
                LanguageList languageList = LanguageList.fromJsonString(data);
                //TODO: todo todooo todooo
            }
            else {
                String exception = intent.getStringExtra("exception");
                LanguageList languageList = new LanguageList(exception);
                //TODO: what next, cap'n ?
            }
        }
    }
}
