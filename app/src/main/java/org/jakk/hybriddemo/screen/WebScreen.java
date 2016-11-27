package org.jakk.hybriddemo.screen;

import org.jakk.hybriddemo.R;

public enum WebScreen implements Screen {

    LOGIN("login.html", R.string.login_title),
    WELCOME("welcome.html", R.string.welcome_title),
    CONTACTS("contacts.html", R.string.contacts_title),
    ACCOUNTS("accounts.html", R.string.accounts_title),
    TRANSACTIONS("transactions.html", R.string.transactions_title);

    private String html;
    private int titleRes;

    WebScreen(String html, int titleRes) {
        this.html = html;
        this.titleRes = titleRes;
    }

    public String getHtml() {
        return html;
    }

    public int getTitleRes() {
        return titleRes;
    }

    public static WebScreen tryParse(String name) {
        for (WebScreen webScreen : WebScreen.values()) {
            if (webScreen.name().equalsIgnoreCase(name)) {
                return webScreen;
            }
        }
        return null;
    }
}
