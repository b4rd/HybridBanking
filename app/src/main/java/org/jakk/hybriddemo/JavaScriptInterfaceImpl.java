package org.jakk.hybriddemo;

import android.os.Bundle;
import android.webkit.JavascriptInterface;

import com.google.gson.Gson;

import org.jakk.hybriddemo.screen.NativeScreen;
import org.jakk.hybriddemo.screen.Screen;
import org.jakk.hybriddemo.screen.WebScreen;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class JavaScriptInterfaceImpl implements JavaScriptInterface {

    private static final Gson GSON = new Gson();

    private WebActivity activity;

    public JavaScriptInterfaceImpl(WebActivity activity) {
        this.activity = activity;
    }

    @JavascriptInterface
    @Override
    public void requestExtras() {
        Bundle extras = activity.getIntent().getExtras();
        final Map<String, Serializable> extrasByKey = new HashMap<>();
        for (String key : extras.keySet()) {
            extrasByKey.put(key, extras.getSerializable(key));
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                WebUtils.invokeJsFunction(activity.webView, "takeExtras", extrasByKey);
            }
        });
    }

    @JavascriptInterface
    @Override
    public void navigateTo(String screenName, String argsObject) {
        Screen screen = NativeScreen.tryParse(screenName);
        if (screen == null) {
            screen = WebScreen.valueOf(screenName.toUpperCase());
        }
        activity.navigateTo(screen, GSON.fromJson(argsObject, Map.class));
    }

    @JavascriptInterface
    @Override
    public void callService(String path, String request) {
        activity.callService(path, GSON.fromJson(request, Map.class), Map.class);
    }

    @JavascriptInterface
    @Override
    public void onRenderingFinished() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.showWebView();
            }
        });
    }
}
