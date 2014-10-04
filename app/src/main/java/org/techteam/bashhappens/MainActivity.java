package org.techteam.bashhappens;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.techteam.bashhappens.api.LanguageEntry;
import org.techteam.bashhappens.fragments.LanguageListFragment;

public class MainActivity extends FragmentActivity implements LanguageListFragment.OnLanguageSelectedListener {

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

        //TODO:
        /*CityDetailsFragment newFragment = CityDetailsFragment.getInstance(cityInfo);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.cities_fragment_container, newFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(null);
        transaction.commit();*/
    }
}
