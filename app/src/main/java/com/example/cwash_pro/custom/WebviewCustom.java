package com.example.cwash_pro.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

public class WebviewCustom extends WebView {
    public WebviewCustom(Context context) {
        super( context );
    }

    public WebviewCustom(Context context, AttributeSet attrs) {
        super( context, attrs );
    }

    public WebviewCustom(Context context, AttributeSet attrs, int defStyleAttr) {
        super( context, attrs, defStyleAttr );
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged( l, t, oldl, oldt );
        if (onScrollingCallback!=null)
            onScrollingCallback.OnScrolling( l,t,oldl,oldt );
    }

    private OnScrollingCallback onScrollingCallback;

    public OnScrollingCallback getOnScrollingCallback(){
        return onScrollingCallback;
    }

    public void setOnScrollingCallback(OnScrollingCallback onScrollingCallback) {
        this.onScrollingCallback = onScrollingCallback;
    }

    public interface OnScrollingCallback {
        void OnScrolling(int l, int i, int oldl, int oldt);
    }
}
