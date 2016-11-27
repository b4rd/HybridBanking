package org.jakk.hybriddemo.screen;

import android.app.Activity;

import org.jakk.hybriddemo.AtmSearchActivity;
import org.jakk.hybriddemo.R;

public enum NativeScreen implements Screen {

    ATM_SEARCH(AtmSearchActivity.class);

    private Class<? extends Activity> activityClass;

    NativeScreen(Class<? extends Activity> activityClass) {
        this.activityClass = activityClass;
    }

    public Class<? extends Activity> getActivityClass() {
        return activityClass;
    }

    public static NativeScreen tryParse(String name) {
        for (NativeScreen nativeScreen : NativeScreen.values()) {
            if (nativeScreen.name().equalsIgnoreCase(name)) {
                return nativeScreen;
            }
        }
        return null;
    }
}
