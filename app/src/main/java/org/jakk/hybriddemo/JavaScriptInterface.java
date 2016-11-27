package org.jakk.hybriddemo;


public interface JavaScriptInterface {
    void navigateTo(String screenName, String argsObject);
    void callService(String path, String request);
    void requestExtras();
    void onRenderingFinished();
}
