package com.example.cwash_pro.custom;

import android.annotation.SuppressLint;
import android.webkit.WebView;

public class WebViewSetting {

    public WebViewSetting() {

    }

    private static final String MOBILE = "Mozilla/5.0 (Linux; U; Android 4.4; en-us; Nexus 4 Build/JOP24G) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30";

    public static void settingWebView(WebView webView){
        webView.getSettings().setBuiltInZoomControls( true );
        webView.getSettings().setDisplayZoomControls( false );
        webView.getSettings().setUseWideViewPort( true );
        webView.getSettings().setLoadWithOverviewMode( true );

        webView.setScrollBarStyle( WebView.SCROLLBARS_OUTSIDE_OVERLAY );
        webView.setScrollbarFadingEnabled( false );
    }
    @SuppressLint("SetJavaScriptEnabled")
    public static void setMobile(WebView webView){

        webView.getSettings().setAllowContentAccess( true );
        webView.getSettings().setAppCacheEnabled( true );
        webView.getSettings().setLoadWithOverviewMode( true );
        webView.getSettings().setSupportZoom( true );
        webView.getSettings().setJavaScriptEnabled( true );

        webView.getSettings().setSaveFormData( true );
        webView.getSettings().setDomStorageEnabled( true );
        webView.getSettings().setUserAgentString( MOBILE );
    }
}
