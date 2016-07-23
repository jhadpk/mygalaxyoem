package learn2crack.customlistview;

import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.inmobi.mygalaxydemo.singlead.R;

public class MainActivity extends FragmentActivity implements SettingsFragment.OnSettingsSavedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        MyGalaxyFragment fragment = new MyGalaxyFragment();
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, fragment)
                //.addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId() == R.id.action_settings) {
//            SettingsFragment fragment = new SettingsFragment();
//
//            getFragmentManager().beginTransaction()
//                    .replace(android.R.id.content, fragment)
//                    .addToBackStack(null)
//                    .commit();
//
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onSettingsSaved() {
        getFragmentManager().popBackStack();
    }
}
