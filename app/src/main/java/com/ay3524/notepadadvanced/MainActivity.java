package com.ay3524.notepadadvanced;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements OnStartNewFragmentListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final SharedPreferences sharedPreferences = this.getSharedPreferences("com.maps.ashishyadav271.sharedpreferencesdemo",MODE_PRIVATE);
        String skipEnabled=sharedPreferences.getString("SKIPENABLED", "");

        String userAlreadyReg=sharedPreferences.getString("UserName", "");
        if(skipEnabled.equals("") && userAlreadyReg.equals("")){
            openSignInFragment(new GooglePlusSignIn());
        }else{
            openMainFragment(new NoteListFragment());
        }


    }

    private void openSignInFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.container,fragment).commit();
    }

    private void openFragment(Fragment fragment,String screenTitle){

        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.container,fragment)
                .addToBackStack(null).commit();

        getSupportActionBar().setTitle(screenTitle);

    }

    void openMainFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.container,fragment).commit();
                //.addToBackStack(null).commit();

        //getSupportActionBar().setTitle(screenTitle);
    }


    @Override
    public void onStartNewFragment(Fragment fragment, String title) {
        openFragment(fragment, title);
    }

    @Override
    public void onStartListFragment(Fragment fragment) {
        openMainFragment(fragment);
    }

}

