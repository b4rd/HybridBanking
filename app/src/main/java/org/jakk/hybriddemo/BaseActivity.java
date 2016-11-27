package org.jakk.hybriddemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.jakk.Constants;
import org.jakk.hybriddemo.network.GsonRequest;
import org.jakk.hybriddemo.screen.NativeScreen;
import org.jakk.hybriddemo.screen.Screen;
import org.jakk.hybriddemo.screen.WebScreen;

import java.io.Serializable;
import java.util.Map;

public class BaseActivity extends AppCompatActivity {

    private static final String PROGRESS_DIALOG_TAG = "progress_dialog";
    private static final String TAG = BaseActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void navigateTo(Screen screen, Map<String, Serializable> args) {
        Intent intent = new Intent();
        if (screen instanceof NativeScreen) {
            intent.setClass(this, ((NativeScreen) screen).getActivityClass());
        } else {
            intent.setClass(this, WebActivity.class);
            intent.putExtra(WebActivity.EXTRA_WEB_SCREEN, ((WebScreen) screen));
        }

        if (args != null) {
            for (Map.Entry<String, Serializable> entry : args.entrySet()) {
                intent.putExtra(entry.getKey(), entry.getValue());
            }
        }
        startActivity(intent);
    }

    public <T> void callService(final String path, Object request, Class<T> responseClass) {
        showProgressDialog();

        RequestQueue requestQueue = ((HybridApplication)getApplication()).getRequestQueue();
        requestQueue.add(new GsonRequest<>(Constants.REST_ENDPOINT_URL + path, request, responseClass, new Response.Listener<T>() {
            @Override
            public void onResponse(T response) {
                onServiceResponse(path, response);
                hideProgressDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onServiceErrorResponse(path, error);
                hideProgressDialog();
            }
        }));
    }

    protected void onServiceResponse(String path, Object response){}
    protected void onServiceErrorResponse(String path, VolleyError error){
        Log.e(TAG, "service error", error);
        Toast.makeText(this, R.string.loading_error, Toast.LENGTH_SHORT).show();
    }

    private void showProgressDialog() {
        ProgressDialogFragment.newInstance().show(getSupportFragmentManager(), PROGRESS_DIALOG_TAG);
    }

    private void hideProgressDialog() {
        Fragment dialogFragment = getSupportFragmentManager().findFragmentByTag(PROGRESS_DIALOG_TAG);
        getSupportFragmentManager().beginTransaction()
                .remove(dialogFragment)
                .commitAllowingStateLoss();
    }
}
