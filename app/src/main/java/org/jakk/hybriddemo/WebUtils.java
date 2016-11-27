package org.jakk.hybriddemo;

import android.content.Context;
import android.webkit.WebView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class WebUtils {

    private static final Pattern TRANSLATABLE = Pattern.compile("\\$\\{(.+)\\}");
    private static final String ENCODING = "UTF-8";
    private static final Gson GSON = new Gson();

    static String loadHtml(Context context, String htmlName) {
        StringBuilder htmlBuilder = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(htmlName), ENCODING));
            String line;
            while ((line = reader.readLine()) != null) {
                replaceTranslatablesInLine(context, line, htmlBuilder);
                htmlBuilder.append('\n');
            }
        } catch (IOException ix) {
            throw new RuntimeException(ix);
        }
        return htmlBuilder.toString();
    }

    static void invokeJsFunction(WebView webView, String fnName, Object... args) {
        String funCall = "javascript:" + fnName + "(" + buildArgList(args) + ");";
        webView.loadUrl(funCall);
    }

    private static String buildArgList(Object... args) {
        StringBuilder argsBuilder = new StringBuilder();
        for (Object arg : args) {
            if (arg instanceof Number) {
                argsBuilder.append(arg);
            } if (arg instanceof String) {
                argsBuilder.append("\'");
                argsBuilder.append(arg);
                argsBuilder.append("\'");
            } else {
                argsBuilder.append(GSON.toJson(arg));
            }
            argsBuilder.append(", ");
        }
        if (args.length > 0) {
            argsBuilder.setLength(argsBuilder.length() - 2);
        }
        return argsBuilder.toString();
    }

    private static void replaceTranslatablesInLine(Context context, String line, StringBuilder builder) {
        Matcher matcher = TRANSLATABLE.matcher(line);
        int lastEndIndex = 0;
        while (matcher.find()) {
            String stringName = matcher.group(1);
            builder.append(line.substring(lastEndIndex, matcher.start()));
            builder.append(getStringByName(context, stringName));
            lastEndIndex = matcher.end();
        }
        builder.append(line.substring(lastEndIndex, line.length()));
    }

    private static String getStringByName(Context context, String name) {
        String packageName = context.getPackageName();
        int resId = context.getResources().getIdentifier(name, "string", packageName);
        if (resId == 0) {
            throw new IllegalArgumentException("string not found with name: " + name);
        }
        return context.getString(resId);
    }
}
