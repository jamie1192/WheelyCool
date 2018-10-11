package com.jastley.wheelycool;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.jastley.wheelycool.ui.main.MainFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {

            /**default project setup sets this as .commitNow() instead, but there is no guarantee
            that the fragment will be added to the backstack, changed to commit() instead to allow backstack
             */
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {

        if(getSupportFragmentManager().getBackStackEntryCount() >= 1) {
            getSupportFragmentManager().popBackStack();
        }
        else{
            super.onBackPressed();
        }
    }
}
