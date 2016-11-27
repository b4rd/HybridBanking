package org.jakk.hybriddemo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.jakk.hybriddemo.screen.WebScreen;

public class WebActivity extends BaseActivity {

    public static final String EXTRA_WEB_SCREEN = "hybriddemo.web_screen";
    private static final String JAVASCRIPT_INTERFACE_NAME = "android";

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        WebScreen webScreen = (WebScreen) getIntent().getSerializableExtra(EXTRA_WEB_SCREEN);
        setTitle(webScreen.getTitleRes());

        webView = (WebView) findViewById(R.id.web_view);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);


        webView.loadDataWithBaseURL("file:///android_asset/.", WebUtils.loadHtml(this, webScreen.getHtml()), "text/html; charset=utf-8", null, null);
        webView.addJavascriptInterface(new JavaScriptInterfaceImpl(this), JAVASCRIPT_INTERFACE_NAME);
        webView.setWebChromeClient(new CustomWebChromeClient());
    }

    @Override
    protected void onServiceResponse(String path, Object response) {
        super.onServiceResponse(path, response);
        WebUtils.invokeJsFunction(webView, "onServiceResponse", path, response);
    }

    public void showWebView() {
        Log.d("Web", "showWebView");
        webView.setVisibility(View.VISIBLE);
    }

    class CustomWebChromeClient extends WebChromeClient {
        private static final String TAG = "CustomWebChromeClient";

        @Override
        public boolean onConsoleMessage(ConsoleMessage cm) {
            Log.d(TAG, String.format("%s @ %d: %s", cm.message(), cm.lineNumber(), cm.sourceId()));
            return true;
        }
    }
}
