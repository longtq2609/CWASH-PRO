package com.example.cwash_pro.ui.customer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cwash_pro.R;
import com.example.cwash_pro.custom.WebViewSetting;
import com.example.cwash_pro.custom.WebviewCustom;

public class WebViewActivity extends AppCompatActivity {
    private WebviewCustom webView;
    private ImageView imgBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        webView = findViewById(R.id.webView);
        imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> onBackPressed());

        String url = "https://cwash-pro.blogspot.com/2021/10/chinh-sach-bao-mat-thong-tin.html";
        webView.loadUrl(url);
        settingWebAccess();
        settingWebClient();

    }
    private void settingWebClient() {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Toast.makeText(WebViewActivity.this, "Cannot Load Page! Please check your internet!", Toast.LENGTH_LONG).show();

            }
        });
    }
    private void settingWebAccess() {
        WebViewSetting.settingWebView(webView);
        WebViewSetting.setMobile(webView);
    }
}