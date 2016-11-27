package org.jakk.hybriddemo;

import android.os.Bundle;

import org.jakk.hybriddemo.screen.WebScreen;

import java.io.Serializable;
import java.util.Collections;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigateTo(WebScreen.LOGIN, null);
        finish();
    }
}
