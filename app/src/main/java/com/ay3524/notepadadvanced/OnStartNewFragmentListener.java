package com.ay3524.notepadadvanced;

import android.support.v4.app.Fragment;

/**
 * Created by ashishyadav271 on 7/12/2016.
 */
public interface OnStartNewFragmentListener {
    void onStartNewFragment(Fragment fragment,String title);
    void onStartListFragment(Fragment fragment);
}
